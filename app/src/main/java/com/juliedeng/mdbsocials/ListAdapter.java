package com.juliedeng.mdbsocials;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
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
    private ArrayList<Social> socials;

    public ListAdapter(Context context, ArrayList<Social> socials) {
        this.context = context;
        this.socials = socials;
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view, parent, false);
        return new CustomViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final CustomViewHolder holder, int position) {
        Social social = socials.get(position);
        holder.event_name.setText(social.getName());
        holder.num_interested.setText(String.valueOf(social.getNumInterested()));
        holder.email.setText(social.getEmail());
        Glide.with(context).load(social.getImageURL()).into(holder.image);
        holder.bind(socials.get(position));
    }

    @Override
    public int getItemCount() {
        return socials.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView event_name, num_interested, email;
        ImageView image;
        ConstraintLayout card;

        public CustomViewHolder (View view) {
            super(view);
            this.event_name = (TextView) view.findViewById(R.id.event_name);
            this.num_interested = (TextView) view.findViewById(R.id.num_interested);
            this.email = (TextView) view.findViewById(R.id.email);
            this.image = (ImageView) view.findViewById(R.id.image);
            this.card = (ConstraintLayout) view.findViewById(R.id.card);
        }

        void bind (Social social) {
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, DetailActivity.class);
                    i.putExtra("num_interested", num_interested.getText().toString());
                    i.putExtra("email", email.getText());
                    i.putExtra("event_name", event_name.getText());
                    i.putExtra("event_name", event_name.getText());
                    i.putExtra("event_name", event_name.getText());
                    context.startActivity(i);
                }
            });
        }
    }
}