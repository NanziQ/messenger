package com.nanziq.messenger.Adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nanziq.messenger.Model.Dialog;
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

    public DialogsAdapter(List<Dialog> dialogList, Context context){
        this.dialogList = dialogList;
        this.context = context;
    }
    public static interface Listener{
        public void onClick(int position);
    }

    public void setListener(DialogsAdapter.Listener listener){
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public View mView;
        CardviewDialogsBinding binding;
        public ViewHolder(View view){
            super(view);
            mView = view;
            binding = DataBindingUtil.bind(view);
        }

    }
    public DialogsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_dialogs, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DialogsAdapter.ViewHolder holder, final int position) {
        Dialog dialog = dialogList.get(position);
        holder.binding.setDialog(dialog);
        holder.binding.executePendingBindings();
        Glide
                .with(context)
                .load(dialog.getImage())
                .into(holder.binding.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null){
                    listener.onClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dialogList.size();
    }
}
