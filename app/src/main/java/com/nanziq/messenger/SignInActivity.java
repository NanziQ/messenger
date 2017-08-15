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

import java.util.concurrent.TimeUnit;

public class SignInActivity extends AppCompatActivity {

    private static final String ENTER_PHONE= "enter_phone";
    private static final String ENTER_CODE= "enter_code";

    private FirebaseAuth firebaseAuth;

    private MaskedEditText phoneNumber;
    private Button button;
    private TextView labelEnterPhone;

    private TextInputLayout inputCodeLayout;
    private TextInputEditText textCode;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String verificationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        firebaseAuth = FirebaseAuth.getInstance();
        phoneNumber = (MaskedEditText) findViewById(R.id.phoneNumber);
        phoneNumber.setInputType(InputType.TYPE_CLASS_PHONE);
        textCode = (TextInputEditText) findViewById(R.id.code);
        button = (Button) findViewById(R.id.button);
        labelEnterPhone = (TextView) findViewById(R.id.labelEnterPhone);
        inputCodeLayout = (TextInputLayout) findViewById(R.id.inputCodeLayout);
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
                           startActivity(new Intent(getApplicationContext(), MessagesActivity.class));
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
            enableViews(inputCodeLayout, textCode, button);
            disableViews(phoneNumber, labelEnterPhone);
            enableOnClickListener(ENTER_CODE);
        } else if(s == ENTER_PHONE){
            disableViews(inputCodeLayout, textCode);
            enableViews(phoneNumber, button, labelEnterPhone);
            enableOnClickListener(ENTER_PHONE);
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
}
