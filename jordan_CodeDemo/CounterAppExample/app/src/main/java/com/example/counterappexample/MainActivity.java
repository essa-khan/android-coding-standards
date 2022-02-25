package com.example.counterappexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText edittext;
    private TextView txv;
    private final ButtonCounter pressCounter = new ButtonCounter(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button increaseBtn = findViewById(R.id.inbt);
        Button decreaseBtn = findViewById(R.id.dcbt);
        edittext = findViewById(R.id.numin);
        txv = findViewById(R.id.tx);

        increaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int input = 0;
                // Get user input
                try {
                    input = Integer.parseInt(edittext.getText().toString());
                }
                catch (NumberFormatException n) {
                    txv.setText("Enter a valid integer");
                }

                // Increase counter
                pressCounter.increaseCount(input);
                txv.setText(Integer.toString(pressCounter.getCount()));
            }
        });

        decreaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int input = 0;
                // Get user input
                try {
                    input = Integer.parseInt(edittext.getText().toString());
                }
                catch (NumberFormatException n) {
                    txv.setText("Enter a valid integer");
                }

                // Decrease counter
                try {
                    pressCounter.decreaseCount(input);
                    txv.setText(Integer.toString(pressCounter.getCount()));
                }
                catch (IllegalArgumentException i) {
                    txv.setText("Can't decrease below zero!");
                }
            }
        });

        /*
        **Recommendation 05 OBJ54-J. OBJ54-J Do not attempt to help the garbage collector
                                          by setting local reference variables to null**

        Setting local reference variables to null adds clutter and makes maintenance more difficult

        NON-COMPLIANT SOLUTION
        increaseBtn = null;
        decreaseBtn = null;

        COMPLIANT SOLUTION
        Doing nothing when increaseBtn and decreaseBtn are no longer needed is a better solution

        End of OBJ54-J
        */
    }
}