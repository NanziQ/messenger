package com.nanziq.messenger.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nanziq.messenger.R;

/**
 * Created by Konstantin on 06.08.2017.
 */

public class DialogsAdapter extends RecyclerView.Adapter<DialogsAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public View mView;
        public ViewHolder(View view){
            super(view);
            mView = view;
        }
    }
    public DialogsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_dialogs, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DialogsAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
