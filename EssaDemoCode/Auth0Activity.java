package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.callback.Callback;
import com.auth0.android.provider.WebAuthProvider;
import com.auth0.android.result.Credentials;

public class Rule07Activity extends AppCompatActivity {

    private TextView token;
    private Auth0 auth0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule07);
        token = (TextView) findViewById(R.id.token);
        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        auth0 = new Auth0(Rule07Activity.this);
    }

    // Android Rule 07. Miscellaneous - For OAuth, use a secure Android method to deliver access tokens.
    private void login() {
        token.setText("Not logged in");
        WebAuthProvider.login(auth0)
                .withScheme("app")
                .withAudience("https://dev-zop826d9.us.auth0.com/userinfo")
                .start(Rule07Activity.this, new Callback<Credentials, AuthenticationException>() {
                    @Override
                    public void onFailure(final AuthenticationException exception) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Rule07Activity.this, "Error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    @Override
                    public void onSuccess(@NonNull final Credentials credentials) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Test with line below and you will get access token back
                                //token.setText("Logged in successfully!: " + credentials.getAccessToken());
                                token.setText("Logged in successfully!");
                                Intent intent = new Intent(Rule07Activity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                });
    }
}
