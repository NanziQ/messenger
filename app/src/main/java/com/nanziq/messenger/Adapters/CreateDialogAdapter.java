package com.nanziq.messenger.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nanziq.messenger.Model.Contact;
import com.nanziq.messenger.R;

import java.util.List;

/**
 * Created by Konstantin on 10.09.2017.
 */

public class CreateDialogAdapter extends RecyclerView.Adapter<CreateDialogAdapter.ViewHolder> {

    private List<Contact> contactList;
    private boolean[] itemSelect;
    private Context context;
    private CreateDialogAdapter.Listener listener;
    private StorageReference storageReference;


    public CreateDialogAdapter(List<Contact> contactList, Context context){
        this.contactList = contactList;
        this.itemSelect = new boolean[getItemCount()];
        this.context = context;
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    public interface Listener{
        void onClick(int position);
    }

    public void setOnClickListener(CreateDialogAdapter.Listener listener){
        this.listener = listener;
    }

    public boolean[] getItemSelect(){
        return itemSelect;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_create_dialog, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        ImageView createDialogImageView = (ImageView) holder.mView.findViewById(R.id.createDialogImageView);
        Glide
                .with(context)
                .using(new FirebaseImageLoader())
                .load(storageReference.child("images/" + contact.getImage()))
                .placeholder(R.drawable.ic_account_black_48dp)
                .into(createDialogImageView);
        TextView createDialogTextView = (TextView) holder.mView.findViewById(R.id.createDialogTextView);
        createDialogTextView.setText(contact.getName());
        final CheckBox createDialogCheckBox = (CheckBox) holder.mView.findViewById(R.id.createDialogCheckBox);
        createDialogCheckBox.setChecked(itemSelect[holder.getAdapterPosition()]);
        createDialogCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemSelect[holder.getAdapterPosition()] = !itemSelect[holder.getAdapterPosition()];
                createDialogCheckBox.setChecked(itemSelect[holder.getAdapterPosition()]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }


}
