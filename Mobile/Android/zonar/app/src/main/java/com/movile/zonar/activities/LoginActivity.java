package com.movile.zonar.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.movile.zonar.R;

public class LoginActivity extends AppCompatActivity {


    private LoginButton loginButton;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);

        loginButton = (LoginButton) findViewById(R.id.login_button);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Facebook onSuccess", "User ID: " + loginResult.getAccessToken().getUserId() + " Auth Token: " + loginResult.getAccessToken().getToken());
                Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                startActivity(intent);

            }

            @Override
            public void onCancel() {
                Log.d("Facebook onCancel","Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                Log.d("Facebook onError","Login attempt failed.");

                Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                startActivity(intent);

            }
        });

        final Button button = (Button) findViewById(R.id.btnLoginCelular);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });
    }


}
