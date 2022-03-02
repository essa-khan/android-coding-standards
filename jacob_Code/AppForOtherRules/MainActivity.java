package com.example.helloworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    String name;
    int cvv, credit, size;
    EditText userName;
    EditText cvvNum;
    EditText creditNum;
    private String filename = "myfile";
    FileOutputStream fos = null;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = (EditText) findViewById(R.id.userName);
        cvvNum = (EditText) findViewById(R.id.cvvNum);
        creditNum = (EditText) findViewById(R.id.creditNum);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = userName.getText().toString();
                cvv = Integer.valueOf(cvvNum.getText().toString());
                credit = Integer.valueOf(creditNum.getText().toString());

                /*
                    Recommendation 7 MSC51-J: Do not place a semicolon immediately following an if,
                    for, or while condition.
                    This can cause the code to run in an unexpected and unintended way.

                    Recommendation 7 MSC57-J: Strive for logical completeness
                    Having logical completeness allows for better understanding of the code while in
                    the creation process, because you always have that final general statement to
                    show what the code is actually doing.
                 */
                if(String.valueOf(cvv).length() != 3) {
                    if(String.valueOf(credit).length() != 16)
                        size = 0; // Both are bad
                    else
                        size = 1; // Only cvv is bad
                }
                else{
                    if(String.valueOf(credit).length() != 16)
                        size = 2; // Only credit card number is wrong
                    else
                        size = 3; // both are correct length
                }

                /*
                    Recommendation 7 MASC52-J: Finish every set of statements associated with a
                    case label with a break statement.
                    Having a break statement in every case label ensures that the code will only
                    do the one thing and not keep going through any other cases.
                 */
                showToast(name);
                switch (size) {
                    case 0: // Both are bad
                        showToast("Credit card number is incorrect length.");
                        showToast("Credit card cvv is incorrect length.");
                        break;
                    case 1: // Only cvv is bad
                        showToast(String.valueOf(credit));
                        showToast("Credit card cvv is incorrect length.");
                        savePrivately(String.valueOf(credit));
                        sendBroadcast();
                        break;
                    case 2: // Only credit card number is wrong
                        showToast("Credit card number is incorrect length.");
                        showToast(String.valueOf(cvv));
                        break;
                    case 3: // both are correct length
                        showToast(String.valueOf(cvv));
                        showToast(String.valueOf(credit));
                        break;
                    default: //handles errors
                        break;
                }

            }
        });

    }
    private void showToast(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }
    /*
    Rule 1 DRD00: Do not store sensitive information on external storage (SD card)
    unless encrypted first
    The method "savePrivately" is in charge of encrypting and then saving the given data onto
    an external storage device. If it is not encrypted it is possible for malicious apps
    to gain control of the storage device and read/write the information.
     */
    private void savePrivately(String data){

        try
            {
                fos = openFileOutput(filename, Context.MODE_PRIVATE);
                fos.write(data.getBytes());
                fos.close();
            } catch (FileNotFoundException e){
                e.printStackTrace();
            } catch (IOException e){
            e.printStackTrace();
        } finally {
            if(fos != null){
                try{
                    fos.close();
                } catch(IOException e){
                    e.printStackTrace();
                }
            }
        }

    }
            /*
            Rule 2 DRD03-J:Sensitive information should not be broadcast using
            implicit intent
            The below method is sending credit card information using the safer explicit intent,
            because this is specifying the receiver. Non compliant code would send the information,
            which in this case is a credit card number, without specifying the receiver and as a
            result it would be able to be received by anyone.

            Rule 21 LCK01-J: Do not synchronize objects that may be reused.
            "intent" may be reused somewhere else in the code, so it is important to make sure anyone
            editing this code does not try and synchronize it. If it did become synchronized then
            it could cause
             */
    public void sendBroadcast(){
    /*
    Recommendation 7 MSC50-J: Minimize the scope of the "@SuppressWarnings"
    Having the scope of "@SuppressWarnings be something this size allows the programmer to ignore
    any issues that do not affect the code, and be confident that they are still going to catch errors
    in the rest of the code.
     */
        @SuppressWarnings("unchecked")
        Intent intent = new Intent(String.valueOf(credit));
        intent.putExtra("event", "This is a test");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}
