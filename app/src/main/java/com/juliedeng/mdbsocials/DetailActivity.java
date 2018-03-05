package com.juliedeng.mdbsocials;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.ArrayList;

/**
 * Activity page that displays the specifics of a single event. Also allows users to RSVP to the event.
 */

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Button interested;
    private ImageButton back_button;
    private TextView description, mEmail, event_name, num_interested;
    private ImageView image;
    private boolean rsvp = false;
    private final ArrayList<String> interestedEmails = new ArrayList<>();
    private String firebaseKey, userEmail;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;
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

        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference(getString(R.string.socials_reference));

        /**
         * Receive event details from previous activity
         */
        String name = getIntent().getStringExtra("event_name");
        String email = getIntent().getStringExtra("email");
        String event_description = getIntent().getStringExtra("description");
        String imageURL = getIntent().getStringExtra("imageURL");
        firebaseKey = getIntent().getStringExtra("firebaseKey");
        ArrayList<String> intentArray = getIntent().getStringArrayListExtra("interestedEmails");
        interestedEmails.clear();
        if (intentArray != null) {
            for (int i = 0; i < intentArray.size(); i++) {
                interestedEmails.add(intentArray.get(i));
            }
        }

        userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        interestedRef = firebaseDatabase.getReference("/socials" + firebaseKey + "/interestedEmails");
        rsvp = interestedEmails.contains(userEmail);

        /**
         * Load information onto screen
         */
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

        back_button.setOnClickListener(this);
        interested.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                startActivity(new Intent(DetailActivity.this, MainActivity.class));
                break;
            case R.id.interested:
                interestTransaction();
                break;
            default:
        }
    }

    public void interestTransaction() {
        if (rsvp) {
            interestedRef.runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {
                    interestedEmails.remove(userEmail);
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
                    interestedEmails.add(userEmail);
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
}
