package com.juliedeng.mdbsocials;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Page for users to sign up for an account.
 */

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText signup_email, signup_password, signup_confirmpassword;
    private TextView login_link;
    private Button signup_button;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signup_email = findViewById(R.id.signup_email);
        signup_password = findViewById(R.id.signup_password);
        signup_confirmpassword = findViewById(R.id.signup_confirmpassword);
        signup_button = findViewById(R.id.signup_button);
        login_link = findViewById(R.id.login_link);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    startActivity(new Intent(SignupActivity.this, MainActivity.class));
                }
            }
        };

        View.OnFocusChangeListener keyboardHider = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        };
        signup_button.setOnClickListener(this);
        login_link.setOnClickListener(this);
        signup_email.setOnFocusChangeListener(keyboardHider);
        signup_password.setOnFocusChangeListener(keyboardHider);
        signup_button.setOnFocusChangeListener(keyboardHider);
        signup_confirmpassword.setOnFocusChangeListener(keyboardHider);

    }

    /**
     * Hides the keyboard.
     * @param view
     */
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup_button:
                Utils.attemptSignup(signup_email, signup_password, signup_confirmpassword, mAuth, this);
                break;
            case R.id.login_link:
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                break;
            default:
        }
    }
}
