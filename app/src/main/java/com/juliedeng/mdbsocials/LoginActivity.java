package com.juliedeng.mdbsocials;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * The initial screen where users input login information. Can also redirect to a sign up page.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    private EditText login_email, login_password;
    private TextView forgot_password, signup;
    private Button login_button;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        login_button = findViewById(R.id.login_button);
        forgot_password = findViewById(R.id.forgot_password_link);
        signup = findViewById(R.id.signup_link);
        ConstraintLayout layout=(ConstraintLayout)findViewById(R.id.background);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
            }
        };

        login_button.setOnClickListener(this);
        forgot_password.setOnClickListener(this);
        signup.setOnClickListener(this);
        layout.setOnClickListener(this);

        login_email.setOnFocusChangeListener(this);
        login_password.setOnFocusChangeListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            mAuth.addAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    /**
     * Hides the keyboard.
     * @param view
     */
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(LoginActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.login_button):

                Utils.attemptLogin(login_email, login_password, mAuth, this);
                break;
            case (R.id.forgot_password_link):
                reset_password();
                break;
            case (R.id.signup_link):
                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(i);
                break;
            case (R.id.background):
                login_email.clearFocus();
                login_password.clearFocus();
                findViewById(R.id.background).requestFocus();
                break;
            default:
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch(v.getId()) {
            case (R.id.login_email):
                if (!hasFocus) {
                    hideKeyboard(v);
                }
                break;
            case (R.id.login_password):
                if (!hasFocus) {
                    hideKeyboard(v);
                }
                break;
            default:
        }
    }

    /**
     * Attempts to send a reset password link to the user, and displays information to the user
     * accordingly on whether Firebase was successful or not.
     */
    public void reset_password() {
        if (login_email.getText().length() == 0) {
            Toast.makeText(LoginActivity.this, R.string.no_email,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.sendPasswordResetEmail(login_email.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, R.string.email_sent,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, R.string.reset_pw_fail,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
