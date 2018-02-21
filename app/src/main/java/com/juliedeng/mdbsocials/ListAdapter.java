package com.juliedeng.mdbsocials;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by juliedeng on 2/20/18.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.CustomViewHolder> {
    private Context context;
    private ArrayList<Social> data;

    public ListAdapter(Context context, ArrayList<Social> data) {
        this.context = context;
        this.data = data;
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view, parent, false);
        return new CustomViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final CustomViewHolder holder, int position) {
        Social social = data.get(position);
        holder.event_name.setText(social.getName());
        holder.num_interested.setText(social.getNumInterested());
        holder.email.setText(social.getEmail());
        Glide.with(context).load(social.getImageURL()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView event_name, num_interested, email;
        ImageView image;

        public CustomViewHolder (View view) {
            super(view);
            this.event_name = (TextView) view.findViewById(R.id.event_name);
            this.num_interested = (TextView) view.findViewById(R.id.num_interested);
            this.email = (TextView) view.findViewById(R.id.email);
            this.image = (ImageView) view.findViewById(R.id.image);
        }
    }
}
