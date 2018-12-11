package com.example.assignmenttest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity {
    private Button button_signin;
    private TextView textView_signUp;
    LoginDataBaseAdapter loginDataBaseAdapter;
    private final int RC_SIGN_IN = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        final GoogleSignInClient mGoogleSignInClient;
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();

        button_signin = findViewById(R.id.button_signin);
        button_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("username", "Aaron");
                startActivity(intent);
                //SignIn();
            }
        });

        textView_signUp = findViewById(R.id.textView_signUp);
        textView_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });


    }

    @Override
    protected void onStart(){
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null){
            Toast.makeText(getApplicationContext(), "User already signed in. Welcome back, " + account.getDisplayName(), Toast.LENGTH_LONG).show();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
        else{

        }
    }

    public void SignIn(){
        try{
            String userName = ((EditText) findViewById(R.id.editText_username)).getText().toString();
            String userPassword = ((EditText) findViewById(R.id.editText_password)).getText().toString();
            if(userName.equals("") || userPassword.equals(""))
                Toast.makeText(LoginActivity.this, "Fill All Fields", Toast.LENGTH_SHORT).show();

            if(!userName.equals("")){
                String storedPassword = loginDataBaseAdapter.getSingleEntry(userName);

                if(userPassword.equals(storedPassword)){
                    Toast.makeText(LoginActivity.this, "Login Succesfull", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("username", userName);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(LoginActivity.this, "User does not exist, please sign up", Toast.LENGTH_LONG).show();
                }
            }
        }catch (Exception e){
            Log.e("Error", "error login");
        }
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        loginDataBaseAdapter.close();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try{
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            Toast.makeText(getApplicationContext(), "Google SignIn Succesfull!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("username", account.getDisplayName());
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }catch (ApiException e){
            Log.w("GoogleSignInFailed", "signInResult:failed code=" + e.getStatusCode());
        }
    }

}
