package com.example.apple.calendargo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    // define datafields
    private EditText etEmail;
    private EditText etPassword;
    private EditText etPassword_confirm;

    private ProgressBar progressBar;
    private FirebaseAuth auth;

    private List<String> userProfile;

    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        final Button registerButton = (Button) findViewById(R.id.bReg);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //initialize the database parameters
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");
        userProfile = new LinkedList<String>();

        //initialize the user interface parameters
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPassword_confirm = (EditText) findViewById(R.id.etPasswordConfirm);

        //set onclickListener for registerButton
        registerButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerSubmit();

            }
        });

    }

    //submit the register
    private void registerSubmit(){



        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String confirm_password = etPassword_confirm.getText().toString();

        userProfile.add(email);

        //do all the check for the input
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (! password.equals(confirm_password)){
            Toast.makeText(getApplicationContext(), "Passwords must match.", Toast.LENGTH_SHORT).show();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);

        //creat the user
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Toast.makeText(RegisterActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            user = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = user.getUid();
                            myRef.child(uid).setValue(userProfile);

                            Toast.makeText(RegisterActivity.this,user.getEmail()+" registered successfully. ",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            intent.putExtra("login",true);
                            startActivity(intent);
                            finish();
                        }
                    }
                });



    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}
