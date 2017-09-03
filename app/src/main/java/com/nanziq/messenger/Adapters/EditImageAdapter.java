package com.nanziq.messenger.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nanziq.messenger.R;

import java.util.List;

/**
 * Created by Konstantin on 01.09.2017.
 */

public class EditImageAdapter extends RecyclerView.Adapter<EditImageAdapter.ViewHolder> {

    List<String> editImageList;
    public ProfileAdapter.Listener listener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public ViewHolder(final View itemView) {
            super(itemView);
            this.mView = itemView;
        }
    }

    public EditImageAdapter(List<String> list){
        this.editImageList = list;
    }

    public interface Listener{
        public void OnClick(int position);
    }

    public void setOnClickListener(ProfileAdapter.Listener listener){
        this.listener = listener;
    }

    @Override
    public EditImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_edit_image, parent,false);
        return new EditImageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EditImageAdapter.ViewHolder holder, final int position) {
        String item = editImageList.get(position);
        TextView textView = holder.mView.findViewById(R.id.editImage_text);
        textView.setText(item);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return editImageList.size();
    }

}
