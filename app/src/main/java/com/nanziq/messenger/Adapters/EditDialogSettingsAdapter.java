package com.nanziq.messenger.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.nanziq.messenger.R;

import java.util.List;

/**
 * Created by Konstantin on 14.09.2017.
 */

public class EditDialogSettingsAdapter extends RecyclerView.Adapter<EditDialogSettingsAdapter.ViewHolder> {

    private List<String> itemList;
    private EditDialogSettingsAdapter.Listener listener;

    public EditDialogSettingsAdapter(List<String> list) {
        this.itemList = list;
    }

    public interface Listener {
        void onClick(int position);
    }

    public void setOnClickListener(EditDialogSettingsAdapter.Listener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
        }
    }

    @Override
    public EditDialogSettingsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_dialog_settings, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final EditDialogSettingsAdapter.ViewHolder holder, final int position) {
        TextView textView = holder.mView.findViewById(R.id.dialog_settings_name);
        textView.setText(itemList.get(position));
        holder.mView.setOnClickListener(new View.OnClickListener() {
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
        return itemList.size();
    }
}
