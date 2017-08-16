package com.nanziq.messenger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.reinaldoarrosi.maskededittext.MaskedEditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nanziq.messenger.Model.Contact;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class SignInActivity extends AppCompatActivity {

    private static final String ENTER_PHONE= "enter_phone";
    private static final String ENTER_CODE= "enter_code";
    private static final String ENTER_NAME= "enter_name";

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    private MaskedEditText phoneNumber;
    private Button button;
    private TextView labelEnterPhone;

    private TextInputLayout inputCodeLayout;
    private TextInputEditText textCode;

    private TextView labelEnterName;
    private TextInputLayout inputNameLayout;
    private TextInputEditText textName;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String verificationId;

    private List<String> collectionPhoneNumbers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("contacts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                collectionPhoneNumbers = collectAllPhoneNumbers((Map<String, Object>) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        phoneNumber = (MaskedEditText) findViewById(R.id.phoneNumber);
        phoneNumber.setInputType(InputType.TYPE_CLASS_PHONE);
        textCode = (TextInputEditText) findViewById(R.id.code);
        button = (Button) findViewById(R.id.button);
        labelEnterPhone = (TextView) findViewById(R.id.labelEnterPhone);
        inputCodeLayout = (TextInputLayout) findViewById(R.id.inputCodeLayout);
        labelEnterName = (TextView) findViewById(R.id.labelEnterName);
        inputNameLayout = (TextInputLayout) findViewById(R.id.inputNameLayout);
        textName = (TextInputEditText) findViewById(R.id.name);
        updateUI(ENTER_PHONE);
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationId = s;
                updateUI(ENTER_CODE);
            }
        };
    }

    private void enableOnClickListener(String tag){
        if (tag == ENTER_CODE) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startVerificationWithCode(textCode.getText().toString());
                }
            });
        }else if(tag == ENTER_PHONE){
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startPhoneNumberAuth("+" + phoneNumber.getText(true).toString());
                }
            });
        } else if(tag == ENTER_NAME){
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Contact contact = new Contact(textName.getText().toString(), "+" + phoneNumber.getText(true).toString(), "", null);
                    databaseReference.child("contacts").push().setValue(contact);
                    startActivity(new Intent(getApplicationContext(), MessagesActivity.class));
                }
            });
        }
    }

    private void startPhoneNumberAuth(String phoneNumber){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                callbacks);
    }

    public void startVerificationWithCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            boolean check = false;
                            for(String phone: collectionPhoneNumbers){
                                if(phone.equals("+" + phoneNumber.getText(true).toString())){
                                    check = true;
                                    break;
                                }
                            }
                            if(check) {
                                startActivity(new Intent(getApplicationContext(), MessagesActivity.class));
                            } else {
                                updateUI(ENTER_NAME);
                            }

                        } else {
                            // Sign in failed, display a message and update the UI

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    private void updateUI(String s){
        if(s == ENTER_CODE){
            disableViews(phoneNumber, labelEnterPhone, labelEnterName, inputNameLayout, textName);
            enableViews(inputCodeLayout, textCode, button);
            enableOnClickListener(ENTER_CODE);
        } else if(s == ENTER_PHONE){
            disableViews(inputCodeLayout, textCode, labelEnterName, inputNameLayout, textName);
            enableViews(phoneNumber, button, labelEnterPhone);
            enableOnClickListener(ENTER_PHONE);
        } else if (s == ENTER_NAME){
            disableViews(phoneNumber, labelEnterPhone, inputCodeLayout, textCode);
            enableViews(labelEnterName, inputNameLayout, textName, button);
            enableOnClickListener(ENTER_NAME);
        }
    }

    private void enableViews(View... views) {
        for (View v : views) {
            v.setEnabled(true);
            v.setVisibility(View.VISIBLE);
        }
    }

    private void disableViews(View... views) {
        for (View v : views) {
            v.setEnabled(false);
            v.setVisibility(View.INVISIBLE);
        }
    }

    private List<String> collectAllPhoneNumbers(Map<String,Object> users){
        ArrayList<String> phoneNumbers = new ArrayList<>();

        for (Map.Entry<String, Object> entry : users.entrySet()){
            Map singleUser = (Map) entry.getValue();
            phoneNumbers.add((String) singleUser.get("phone"));
        }
        return phoneNumbers;
    }
}
