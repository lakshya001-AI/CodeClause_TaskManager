package com.example.taskmanager_codeclause;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText signupemail, signuppassword;

    private GoogleSignInClient client;

    private ImageView googlesignup;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        getSupportActionBar().hide();


        auth = FirebaseAuth.getInstance();
        signupemail = findViewById(R.id.emailedittextsignup);
        signuppassword = findViewById(R.id.passwordedittextsignup);
        Button signupbutton = findViewById(R.id.signupbutton);

        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = signupemail.getText().toString().trim();
                String pass = signuppassword.getText().toString().trim();

                if(user.isEmpty()){
                    signupemail.setError("Email cannot be empty");
                }
                if(pass.isEmpty()){
                    signuppassword.setError("Password cannot be empty");
                }else {
                    auth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(task -> {

                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Signup Successful",Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(SignUpActivity.this,IntroScreen.class);
                            startActivity(i);
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Signup Failed" + Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_SHORT).show();
                        }

                    });
                }
            }
        });

        googlesignup = findViewById(R.id.signupwithgoogleimage);
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        client = GoogleSignIn.getClient(this,options);


        googlesignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = client.getSignInIntent();
                startActivityForResult(i,1234);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode  == 1234){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
                FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(SignUpActivity.this,IntroScreen.class);
                                    startActivity(i);
                                    finish();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
            catch (ApiException e){
                e.printStackTrace();

            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            Intent i = new Intent(this,MainPage.class);
            startActivity(i);
        }
    }
}