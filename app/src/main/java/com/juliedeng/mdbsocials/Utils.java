package com.juliedeng.mdbsocials;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

/**
 * Class that contains useful functions used repeatedly throughout the application.
 */

public class Utils {

    public static final int PICTURE_UPLOAD = 1;

    /**
     * Attempt to sign up/create a user with Firebase Authentication services.
     * @param mEmail
     * @param mPassword
     * @param mConfirmPassword
     * @param mAuth
     * @param signupActivity
     */
    public static void attemptSignup(EditText mEmail, EditText mPassword, EditText mConfirmPassword, FirebaseAuth mAuth, final SignupActivity signupActivity) {
        Log.d("Signup", "Check if fields are valid");
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
            Log.d("Signup", "Begin attempt to sign user up through Firebase.");
            Utils.progressBar(signupActivity, signupActivity.getApplication().getString(R.string.signup_loading));
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

    /**
     * Attempt to login a user with Firebase Authentication services.
     * @param mEmail
     * @param mPassword
     * @param mAuth
     * @param loginActivity
     */
    public static void attemptLogin(EditText mEmail, EditText mPassword, FirebaseAuth mAuth, final LoginActivity loginActivity) {
        Log.d("Login", "Check if fields are valid");
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        if (!email.equals("") && !password.equals("")) {
            Log.d("Login", "Begin attempt to login user up through Firebase.");
            Utils.progressBar(loginActivity, loginActivity.getApplication().getString(R.string.login_loading));
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

    /**
     * Add a new social event's details to the Firebase Database if the fields are filled in correctly.
     *
     * @param ref
     * @param socialsRef
     * @param key
     * @param selectedImageUri
     * @param addSocialActivity
     * @param mName
     * @param mDate
     * @param mDescription
     */
    public static void submitSocial(final DatabaseReference ref, StorageReference socialsRef, final String key, Uri selectedImageUri, final AddSocialActivity addSocialActivity, EditText mName, EditText mDate, EditText mDescription) {
        Log.d("Add social", "Check if fields are valid.");

        if (mName.getText().toString().equals("") || mDate.getText().toString().equals("") || mDescription.getText().toString().equals("")) {
            Toast.makeText(addSocialActivity, R.string.unfilled_fields, Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedImageUri == null) {
            Toast.makeText(addSocialActivity, R.string.upload_image, Toast.LENGTH_SHORT).show();
            return;

        }

        Utils.progressBar(addSocialActivity, addSocialActivity.getApplication().getString(R.string.create_social_loading));
        final String name = mName.getText().toString();
        final String date = mDate.getText().toString();
        final String description = mDescription.getText().toString();
        final String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        Log.d("Add social", "Begin attempt to upload image to Firebase Storage.");
        socialsRef.putFile(selectedImageUri).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(addSocialActivity, R.string.storage_failure, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("Add social", "Begin attempt to add new social with details to Firebase database.");
                Long timestamp = (new Date()).getTime();
                String imageURL = taskSnapshot.getDownloadUrl().toString();
                Social social = new Social(name, description, email, imageURL, timestamp, date, key);
                ref.child(key).setValue(social);
                addSocialActivity.getApplication().startActivity(new Intent(addSocialActivity, MainActivity.class));
            }
        });
    }

    /**
     * Progress bar to be displayed throughout the application when loading.
     *
     * @param context
     * @param message
     */
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

//    class DownloadFilesTask extends AsyncTask<String, Void, Bitmap> {
//        protected Bitmap doInBackground(String... strings) {
//            try {return Glide.
//                    with(getApplicationContext()).
//                    load(strings[0]).
//                    asBitmap().
//                    into(100, 100). // Width and height
//                    get();}
//            catch (Exception e) {return null;}
//        }
//
//        protected void onProgressUpdate(Void... progress) {}
//
//        protected void onPostExecute(Bitmap result) {
//            imageDetail.setImageBitmap(result);
//        }
//    }
}
