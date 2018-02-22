package com.juliedeng.mdbsocials;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;

public class AddSocialActivity extends AppCompatActivity {

    private static final int PICTURE_UPLOAD = 1;
    EditText new_name, new_description, new_date;
    Button create_button, back_button;
    ImageView new_image;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("/socials");
    private StorageReference storageRef;

    private static FirebaseAuth mAuth;
    private static FirebaseUser mUser;

    Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_social);

        new_name = findViewById(R.id.new_name);
        new_date = findViewById(R.id.new_date);
        new_description = findViewById(R.id.new_description);
        new_image = findViewById(R.id.new_image);
        create_button = findViewById(R.id.create_button);
        back_button = findViewById(R.id.back_button);
        View.OnFocusChangeListener keyboardHider = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        };

        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        new_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICTURE_UPLOAD);
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddSocialActivity.this, MainActivity.class));
            }
        });
        new_name.setOnFocusChangeListener(keyboardHider);
        new_date.setOnFocusChangeListener(keyboardHider);
        new_description.setOnFocusChangeListener(keyboardHider);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            Log.d("AddSocialActivity", "????");
        }

        if (requestCode == PICTURE_UPLOAD && resultCode == RESULT_OK) {
            selectedImageUri = data.getData();
            if (null != selectedImageUri) {
                String path = selectedImageUri.getPath();
                Log.e("image path", path + "");
                new_image.setImageURI(selectedImageUri);
            }
        }
    }

    public void submit() {
        ref = FirebaseDatabase.getInstance().getReference();

        final String key = ref.child("socials").push().getKey();
        storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://mdbsocials-ecfac.appspot.com");
        StorageReference socialsRef = storageRef.child(key + ".png");

        if (selectedImageUri == null) {
            Log.d("SUBMIT", "image null");
        }
        socialsRef.putFile(selectedImageUri).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddSocialActivity.this, "Cannot upload file into storage.", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String name = new_name.getText().toString();
                String date = new_date.getText().toString();
                String description = new_description.getText().toString();
                String email  = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                Long timestamp = (new Date()).getTime();
                String imageURL = taskSnapshot.getDownloadUrl().toString();

                Social social = new Social(name, description, email, imageURL, timestamp, date);
                ref.child("socials").child(key).setValue(social);
                startActivity(new Intent(AddSocialActivity.this, MainActivity.class));
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
