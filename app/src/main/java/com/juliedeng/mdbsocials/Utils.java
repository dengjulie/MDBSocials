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

//can utils extend appcompatactviity
public class Utils extends AppCompatActivity {

    public static final int PICTURE_UPLOAD = 1;

    public static void attemptSignup(EditText mEmail, EditText mPassword, EditText mConfirmPassword, FirebaseAuth mAuth, final SignupActivity signupActivity) {
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        String confirmPassword = mConfirmPassword.getText().toString();
        if (email.length() == 0) {
            Toast.makeText(signupActivity, "Input an email.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() == 0) {
            Toast.makeText(signupActivity, "Input a password.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(signupActivity, "Passwords must be at least 6 characters.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(signupActivity, "Passwords do not match.",
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
                                Toast.makeText(loginActivity, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                loginActivity.getApplication().startActivity(new Intent(loginActivity, MainActivity.class));
                            }
                        }
                    });
        } else {
            Toast.makeText(loginActivity, "All fields must be filled in.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public static void submitSocial(final DatabaseReference ref, StorageReference socialsRef, final String key, Uri selectedImageUri, final AddSocialActivity addSocialActivity, EditText mName, EditText mDate, EditText mDescription) {

        if (mName.getText().toString().equals("") || mDate.getText().toString().equals("") || mDescription.getText().toString().equals("")) {
            Toast.makeText(addSocialActivity, "Please fill out all the fields.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedImageUri == null) {
            Toast.makeText(addSocialActivity, "Please add an image.", Toast.LENGTH_SHORT).show();
            return;

        }

        final String name = mName.getText().toString();
        final String date = mDate.getText().toString();
        final String description = mDescription.getText().toString();
        final String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        socialsRef.putFile(selectedImageUri).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(addSocialActivity, "Cannot upload file into storage.", Toast.LENGTH_SHORT).show();
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

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
