package com.example.dummyvillebank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.util.Log;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity {
    private final String[] adminInfo = {"admin", "password"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

//    No need for return value that is easily ignorable
//    public boolean loginHandler(View v){
//        // Get username and password from the login
//        TextView userName = findViewById(R.id.usernameLogin);
//        TextView passWord = findViewById(R.id.passwordLogin);
//        TextView errorMsg = findViewById(R.id.errorMsg);
//
//        // The input from the user
//        String uName = userName.getText().toString();
//        String pWord = passWord.getText().toString();
//        String[] loginInfo = {uName, pWord};
//
//        // Calls Arrays.equals() instead of Object.equals()
//        if(Arrays.equals(loginInfo, adminInfo)){
//            userName.setText("");
//            passWord.setText("");
//            errorMsg.setVisibility(View.INVISIBLE);
//            Intent i = new Intent(this, UserHomeActivity.class);
//            startActivity(i);
//            return true;
//        }
//        else{
//            errorMsg.setVisibility(View.VISIBLE);
//            return false;
//        }
//    }
    // No return values to ignore
    public void loginHandler(View v){
        // Get username and password from the login
        TextView userName = findViewById(R.id.usernameLogin);
        TextView passWord = findViewById(R.id.passwordLogin);
        TextView errorMsg = findViewById(R.id.errorMsg);

        // The input from the user
        String uName = userName.getText().toString();
        String pWord = passWord.getText().toString();
        String[] loginInfo = {uName, pWord};

        // Calls Arrays.equals() instead of Object.equals()
        if(Arrays.equals(loginInfo, adminInfo)){
            userName.setText("");
            passWord.setText("");
            errorMsg.setVisibility(View.INVISIBLE);
            Intent i = new Intent(this, UserHomeActivity.class);
            startActivity(i);
        }
        else{
            errorMsg.setVisibility(View.VISIBLE);
        }
    }
}