package com.juliedeng.mdbsocials;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Displays individual event information in each card of the list of socials in MainActivity.
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
        Social social = socials.get(socials.size() - position - 1);
        holder.event_name.setText(social.getName());
        holder.num_interested.setText(String.valueOf(social.getNumInterested()) + " interested!");
        holder.email.setText(social.getEmail());
        Glide.with(context).load(social.getImageURL()).into(holder.image);
        if (position % 2 == 0) {
            holder.email.setTextColor(Color.parseColor("#32C7C7"));
            holder.num_interested.setTextColor(Color.parseColor("#32C7C7"));
            holder.event_name.setTextColor(Color.parseColor("#32C7C7"));
            holder.card.setBackgroundColor(Color.parseColor("#ffffff"));

        }
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

        public CustomViewHolder(View v) {
            super(v);
            this.event_name = v.findViewById(R.id.event_name);
            this.num_interested = v.findViewById(R.id.num_interested);
            this.email = v.findViewById(R.id.email);
            this.image = v.findViewById(R.id.image);
            this.card = v.findViewById(R.id.card);
        }

        void bind(Social social) {
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Social current_social = socials.get(socials.size() - getAdapterPosition() - 1);
                    Intent i = new Intent(context, DetailActivity.class);
                    i.putExtra("event_name", current_social.getName());
                    i.putExtra("num_interested", String.valueOf(current_social.getNumInterested()));
                    i.putExtra("email", current_social.getEmail());
                    i.putExtra("description", current_social.getDescription());
                    i.putExtra("imageURL", current_social.getImageURL());
                    i.putExtra("firebaseKey", current_social.getFirebaseKey());
                    i.putExtra("interestedEmails", current_social.getInterestedEmails());
                    context.startActivity(i);
                }
            });
        }
    }
}
