package com.juliedeng.mdbsocials;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

/**
 * Class that contains useful functions used repeatedly throughout the application.
 */

public class Utils {

    public static final int PICTURE_UPLOAD = 1;

    public static void attemptSignup(EditText mEmail, EditText mPassword, EditText mConfirmPassword, FirebaseAuth mAuth, final SignupActivity signupActivity) {
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        String confirmPassword = mConfirmPassword.getText().toString();
        if (password.length() < 6) {
            Toast.makeText(signupActivity, R.string.bad_pw_length,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(signupActivity, R.string.pw_match,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (!email.equals("") && !password.equals("")) {

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(signupActivity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(signupActivity, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            } else {
                                signupActivity.getApplication().startActivity(new Intent(signupActivity, MainActivity.class));
                            }
                        }
                    });
        } else {
            Toast.makeText(signupActivity, R.string.unfilled_fields,
                    Toast.LENGTH_SHORT).show();
        }
    }

    public static void attemptLogin(EditText mEmail, EditText mPassword, FirebaseAuth mAuth, final LoginActivity loginActivity) {
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        if (!email.equals("") && !password.equals("")) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(loginActivity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(loginActivity, task.getException().toString(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                loginActivity.getApplication().startActivity(new Intent(loginActivity, MainActivity.class));
                            }
                        }
                    });
        } else {
            Toast.makeText(loginActivity, R.string.unfilled_fields,
                    Toast.LENGTH_SHORT).show();
        }
    }

    public static void submitSocial(final DatabaseReference ref, StorageReference socialsRef, final String key, Uri selectedImageUri, final AddSocialActivity addSocialActivity, EditText mName, EditText mDate, EditText mDescription) {

        if (mName.getText().toString().equals("") || mDate.getText().toString().equals("") || mDescription.getText().toString().equals("")) {
            Toast.makeText(addSocialActivity, R.string.unfilled_fields, Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedImageUri == null) {
            Toast.makeText(addSocialActivity, R.string.upload_image, Toast.LENGTH_SHORT).show();
            return;

        }

        final String name = mName.getText().toString();
        final String date = mDate.getText().toString();
        final String description = mDescription.getText().toString();
        final String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        socialsRef.putFile(selectedImageUri).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(addSocialActivity, R.string.storage_failure, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Long timestamp = (new Date()).getTime();
                String imageURL = taskSnapshot.getDownloadUrl().toString();

                Social social = new Social(name, description, email, imageURL, timestamp, date, key);
                ref.child(key).setValue(social);
                addSocialActivity.getApplication().startActivity(new Intent(addSocialActivity, MainActivity.class));
            }
        });
    }

    public static void progressBar(Context context, String message) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage(message);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();

        final int totalProgressTime = 100;
        final Thread t = new Thread() {
            @Override
            public void run() {
                int jumpTime = 0;

                while (jumpTime < totalProgressTime) {
                    try {
                        sleep(1000);
                        jumpTime += 5;
                        progress.setProgress(jumpTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
    }
}
