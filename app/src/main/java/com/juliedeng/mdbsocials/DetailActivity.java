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

import java.util.Collections;

public class DetailActivity extends AppCompatActivity {

    Button interested;
    ImageButton back_button;
    TextView description, mEmail, event_name, num_interested;
    ImageView image;
    boolean rsvp = false;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference ref = firebaseDatabase.getReference("/socials");

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
        String email = getIntent().getStringExtra("email");
        final String number_interested = getIntent().getStringExtra("num_interested");
        String event_description = getIntent().getStringExtra("description");
        String imageURL = getIntent().getStringExtra("imageURL");
        event_name.setText(name);
        mEmail.setText("Created by: " + email);
        num_interested.setText(number_interested + " interested!");
        description.setText("Description:\n" + event_description);
        Glide.with(getApplicationContext()).load(imageURL).into(image);

        interested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rsvp) {
                    interested.setText("RSVP");
                    interested.setBackgroundColor(Color.parseColor("#32C7C7"));
                    num_interested.setText(number_interested + " interested!");
                } else {
                    interested.setText("Un-RSVP");
                    interested.setBackgroundColor(Color.parseColor("#ff0000"));
                    int curr_num_interested = Integer.parseInt(number_interested) + 1;
                    num_interested.setText(curr_num_interested + " interested!");
                }
                rsvp = !rsvp;
            }
        });

//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
//                    socials.add(dataSnapshot2.getValue(Social.class));
//                }
//                Collections.sort(socials, new SocialComparator());
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                Log.w("Database", "Failed to read value.", error.toException());
//            }
//        });
//
//        interested.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ref.runTransaction(new Transaction.Handler() {
//                    @Override
//                    public Transaction.Result doTransaction(MutableData mutableData) {
//                        DataSnapshot snapshot = transaction.get(ref);
//                    }
//
//                    @Override
//                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
//
//                    }
//
//                    @Override
//                    public Void apply(Transaction transaction) throws FirebaseFirestoreException {
//                        DataSnapshot snapshot = transaction.get(sfDocRef);
//                        double newPopulation = snapshot.getDouble("population") + 1;
//                        transaction.update(sfDocRef, "population", newPopulation);
//
//                        // Success
//                        return null;
//                    }
//                }).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d(TAG, "Transaction success!");
//                    }
//                })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.w(TAG, "Transaction failure.", e);
//                            }
//                        });
//
//            }
//        });
    }
}
