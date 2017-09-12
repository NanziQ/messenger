package com.nanziq.messenger.Adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nanziq.messenger.Model.Contact;
import com.nanziq.messenger.R;
import com.nanziq.messenger.databinding.CardviewDialogsBinding;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Konstantin on 12.09.2017.
 */

public class EditDialogMembersAdapter extends RecyclerView.Adapter<EditDialogMembersAdapter.ViewHolder> {
    private List<Contact> contactList;
    private Context context;
    private StorageReference storageReference;

    public EditDialogMembersAdapter(List<Contact> list, Context context){
        this.contactList = list;
        this.context = context;
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public View mView;
        public ViewHolder(View view){
            super(view);
            mView = view;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_dialog_members, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        TextView textView = holder.mView.findViewById(R.id.dialog_member_name);
        textView.setText(contact.getName());
        CircleImageView circleImageView = holder.mView.findViewById(R.id.circleImageView);
        Glide
                .with(context)
                .using(new FirebaseImageLoader())
                .load(storageReference.child("images/" + contact.getImage()))
                .into(circleImageView);
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

}
