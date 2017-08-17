package com.nanziq.messenger.Adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.nanziq.messenger.databinding.CardviewContactsBinding;
import com.nanziq.messenger.databinding.CardviewMessageBinding;

/**
 * Created by Konstantin on 17.08.2017.
 */

public class MessageViewHolder extends RecyclerView.ViewHolder{
    public View mView;
    public CardviewMessageBinding binding;
    public MessageViewHolder(View view){
        super(view);
        mView = view;
        binding = DataBindingUtil.bind(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(view, getAdapterPosition());
            }
        });
    }
    private MessageViewHolder.ClickListener clickListener;

    public interface ClickListener{
        public void onItemClick(View view, int position);
    }

    public void setOnClickListener(MessageViewHolder.ClickListener clickListener){
        this.clickListener = clickListener;
    }
}