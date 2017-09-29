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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nanziq.messenger.Firebase.ContactFB;
import com.nanziq.messenger.Model.Contact;
import com.nanziq.messenger.Model.Dialog;
import com.nanziq.messenger.Model.DialogView;
import com.nanziq.messenger.R;
import com.nanziq.messenger.databinding.CardviewDialogsBinding;

import java.util.List;

/**
 * Created by Konstantin on 06.08.2017.
 */

public class DialogsAdapter extends RecyclerView.Adapter<DialogsAdapter.ViewHolder> {

    List<Dialog> dialogList;
    Context context;
    private DialogsAdapter.Listener listener;
    private StorageReference storageReference;
    private ContactFB contactFB;

    public DialogsAdapter(List<Dialog> dialogList, Context context) {
        this.dialogList = dialogList;
        this.context = context;
        storageReference = FirebaseStorage.getInstance().getReference();
        contactFB = ContactFB.getInstance();
    }

    public static interface Listener {
        public void onClick(int position);
    }

    public void setListener(DialogsAdapter.Listener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public CardviewDialogsBinding binding;
        private ViewHolder.Listener listener;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            binding = DataBindingUtil.bind(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(view, getAdapterPosition());
                }
            });
        }


        public static interface Listener {
            public void onClick(View view, int position);
        }

        public void setListener(ViewHolder.Listener listener) {
            this.listener = listener;
        }

    }

    public DialogsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_dialogs, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final DialogsAdapter.ViewHolder holder, int position) {
        Dialog dialog = dialogList.get(position);
        holder.binding.setDialog(dialog);
        holder.binding.executePendingBindings();
        if (!dialog.getSolo()) {
            Glide
                    .with(context)
                    .using(new FirebaseImageLoader())
                    .load(storageReference.child("images/" + dialog.getImage()))
                    .into(holder.binding.imageView);
        } else {
            Contact contact = null;
            if (dialog.getContacts().size() == 1) {
                contact = contactFB.getContactFromUid(dialog.getContacts().get(0));
            } else {
                for (String contactUid : dialog.getContacts()) {
                    if (!contactUid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        contact = contactFB.getContactFromUid(contactUid);
                        break;
                    }
                }
            }
            if (contact != null) {
                Glide
                        .with(context)
                        .using(new FirebaseImageLoader())
                        .load(storageReference.child("images/" + contact.getImage()))
                        .into(holder.binding.imageView);
            }
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dialogList.size();
    }
}
