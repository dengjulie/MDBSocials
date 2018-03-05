package com.juliedeng.mdbsocials;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

/**
 * Activity page that allows user to input information to add a new social to the list of existing socials.
 */

public class AddSocialActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText new_name, new_description, new_date;
    private Button create_button;
    private ImageButton back_button;
    private ImageView new_image;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref = database.getReference("/socials");
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

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        create_button.setOnClickListener(this);
        new_image.setOnClickListener(this);
        back_button.setOnClickListener(this);

        View.OnFocusChangeListener keyboardHider = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        };
        new_name.setOnFocusChangeListener(keyboardHider);
        new_date.setOnFocusChangeListener(keyboardHider);
        new_description.setOnFocusChangeListener(keyboardHider);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Utils.PICTURE_UPLOAD && resultCode == RESULT_OK) {
            selectedImageUri = data.getData();
            if (null != selectedImageUri) {
                new_image.setImageURI(selectedImageUri);
            }
        }
    }

    public void submit() {
        ref = FirebaseDatabase.getInstance().getReference();

        final String key = ref.child("socials").push().getKey();
        storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(getString(R.string.storage_URL));
        StorageReference socialsRef = storageRef.child(key + ".png");

        socialsRef.putFile(selectedImageUri).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddSocialActivity.this, R.string.storage_failure, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String name = new_name.getText().toString();
                String date = new_date.getText().toString();
                String description = new_description.getText().toString();
                String email = mUser.getEmail();
                Long timestamp = (new Date()).getTime();
                String imageURL = taskSnapshot.getDownloadUrl().toString();

                Social social = new Social(name, description, email, imageURL, timestamp, date, key);
                ref.child("socials").child(key).setValue(social);
                startActivity(new Intent(AddSocialActivity.this, MainActivity.class));
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.create_button):
                submit();
                break;
            case (R.id.new_image):
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), Utils.PICTURE_UPLOAD);
                break;
            case (R.id.back_button):
                startActivity(new Intent(AddSocialActivity.this, MainActivity.class));
                break;
            default:
        }
    }
}
