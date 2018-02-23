package com.juliedeng.mdbsocials;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class DetailActivity extends AppCompatActivity {

    Button interested;
    ImageButton back_button;
    TextView description, mEmail, event_name, num_interested;
    ImageView image;
    boolean rsvp = false;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference ref = firebaseDatabase.getReference("/socials");
    DatabaseReference interestedRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        back_button = findViewById(R.id.back_button);
        interested = findViewById(R.id.interested);
        description = findViewById(R.id.description);
        mEmail = findViewById(R.id.email);
        event_name = findViewById(R.id.event_name);
        num_interested = findViewById(R.id.num_interested);
        image = findViewById(R.id.image);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetailActivity.this,MainActivity.class);
                startActivity(i);
            }
        });

        String name = getIntent().getStringExtra("event_name");
        final String email = getIntent().getStringExtra("email");
        final String number_interested = getIntent().getStringExtra("num_interested"); //this is just the number
        String event_description = getIntent().getStringExtra("description");
        String imageURL = getIntent().getStringExtra("imageURL");
        final ArrayList<String> interestedEmails;
        if (getIntent().getStringArrayListExtra("interestedEmails")!=null) {
            interestedEmails = getIntent().getStringArrayListExtra("interestedEmails");
        } else {
            interestedEmails = new ArrayList<>();
        }
        final String firebaseKey = getIntent().getStringExtra("firebaseKey");

        interestedRef = firebaseDatabase.getReference("/socials/" + firebaseKey + "/interestedEmails");

        rsvp = interestedEmails.contains(email);
        num_interested.setText(interestedEmails.size() + " interested!");

        if (!rsvp) {
            interested.setText("RSVP");
            interested.setBackgroundColor(Color.parseColor("#ff69b4"));
        } else {
            interested.setText("RSVP'd");
            interested.setBackgroundColor(Color.parseColor("#32C7C7"));
        }

        event_name.setText(name);
        mEmail.setText("Created by: " + email);
        description.setText("Description:\n" + event_description);
        Glide.with(getApplicationContext()).load(imageURL).into(image);

        interested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rsvp) {
                    interestedRef.runTransaction(new Transaction.Handler() {
                        @Override
                        public Transaction.Result doTransaction(MutableData mutableData) {
                            interestedEmails.remove(email);
                            ref.child(firebaseKey).child("interestedEmails").setValue(interestedEmails);
                            ref.child(firebaseKey).child("numInterested").setValue(interestedEmails.size());
                            return Transaction.success(mutableData);
                        }
                        @Override
                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                            interested.setText("RSVP");
                            interested.setBackgroundColor(Color.parseColor("#ff69b4"));
                            num_interested.setText(interestedEmails.size() + " interested!");
                            rsvp = !rsvp;
                        }
                    });
                } else {
                    interestedRef.runTransaction(new Transaction.Handler() {
                        @Override
                        public Transaction.Result doTransaction(MutableData mutableData) {
                            interestedEmails.add(email);
                            ref.child(firebaseKey).child("interestedEmails").setValue(interestedEmails);
                            ref.child(firebaseKey).child("numInterested").setValue(interestedEmails.size());
                            return Transaction.success(mutableData);
                        }
                        @Override
                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                            interested.setText("RSVP'd");
                            interested.setBackgroundColor(Color.parseColor("#32C7C7"));
                            num_interested.setText(interestedEmails.size() + " interested!");
                            rsvp = !rsvp;
                        }
                    });
                }
            }
        });
    }
}
