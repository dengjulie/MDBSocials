package com.juliedeng.mdbsocials;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    Button back, interested;
    TextView description, mEmail, event_name, num_interested;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        back = findViewById(R.id.back_button);
        interested = findViewById(R.id.interested);
        description = findViewById(R.id.description);
        mEmail = findViewById(R.id.email);
        event_name = findViewById(R.id.event_name);
        num_interested = findViewById(R.id.num_interested);
        image = findViewById(R.id.image);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetailActivity.this,MainActivity.class);
                startActivity(i);
            }
        });

        String name = getIntent().getStringExtra("event_name");
        String email = getIntent().getStringExtra("email");
        String number_interested = getIntent().getStringExtra("num_interested");
        event_name.setText(name);
        mEmail.setText(email);
        num_interested.setText(number_interested);
    }
}
