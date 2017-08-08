package com.nanziq.messenger.Adapters;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.nanziq.messenger.Model.Contact;
import com.nanziq.messenger.Model.Dialog;
import com.nanziq.messenger.R;
import com.nanziq.messenger.databinding.CardviewContactsBinding;

import java.util.List;

/**
 * Created by Konstantin on 08.08.2017.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {
    List<Contact> contactList;
    private Context context;
    private ContactsAdapter.Listener listener;

    public ContactsAdapter(List<Contact> contactList, Context context){
        this.contactList = contactList;
        this.context = context;
    }

    public interface Listener{
        public void onClick(int position);
    }
    public void setListener(ContactsAdapter.Listener listener){
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public View mView;
        CardviewContactsBinding binding;
        public ViewHolder(View view){
            super(view);
            mView = view;
            binding = DataBindingUtil.bind(view);
        }
    }
    public ContactsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_contacts,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactsAdapter.ViewHolder holder, final int position) {
        Contact contact = contactList.get(position);
        holder.binding.setContact(contact);
        holder.binding.executePendingBindings();
        Glide
                .with(context)
                .load(contact.getImage())
                .into(holder.binding.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener !=null){
                    listener.onClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }
}
