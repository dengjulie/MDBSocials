package com.juliedeng.mdbsocials;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by juliedeng on 2/18/18.
 */

public class SignupActivity extends AppCompatActivity {

    private EditText signup_email, signup_password, signup_confirmpassword;
    private TextView login_link;
    private Button signup_button;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private final static String TAG = "SignupActivity";

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
        View.OnFocusChangeListener keyboardHider = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        };

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSignup();
            }
        });
        login_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
        signup_email.setOnFocusChangeListener(keyboardHider);
        signup_password.setOnFocusChangeListener(keyboardHider);
        signup_button.setOnFocusChangeListener(keyboardHider);
        signup_confirmpassword.setOnFocusChangeListener(keyboardHider);

    }

    private void attemptSignup() {
        Log.d(TAG, "enter  func");
        if (signup_email.getText().length() == 0) {
            Toast.makeText(SignupActivity.this, "Input an email.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (signup_password.getText().length() == 0) {
            Toast.makeText(SignupActivity.this, "Input a password.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        String email = signup_email.getText().toString();
        String password = signup_password.getText().toString();
        if (password.length() < 6) {
            Log.d(TAG, "enter  bad pw check");
            Toast.makeText(SignupActivity.this, "Passwords must be longer than 6 characters.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "pass bad pw check");
        if (!email.equals("") && !password.equals("")) {

            Log.d(TAG, "enter  if statement");
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                            if (!task.isSuccessful()) {
//                                check toast message
                                Toast.makeText(SignupActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            } else {
                                startActivity(new Intent(SignupActivity.this, MainActivity.class));
                            }
                        }
                    });
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
