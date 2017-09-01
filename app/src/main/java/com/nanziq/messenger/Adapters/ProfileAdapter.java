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

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {
    List<String> profileList;
    public ProfileAdapter.Listener listener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public ViewHolder(final View itemView) {
            super(itemView);
            this.mView = itemView;
        }

    }

    public ProfileAdapter(List<String> list){
        this.profileList = list;
    }

    public interface Listener{
        public void OnClick(int position);
    }

    public void setOnClickListener(ProfileAdapter.Listener listener){
        this.listener = listener;
    }

    @Override
    public ProfileAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_profile, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProfileAdapter.ViewHolder holder, final int position) {
        String item = profileList.get(position);
        TextView textView = holder.mView.findViewById(R.id.profile_textView);
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
        return profileList.size();
    }


}
