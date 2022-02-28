package com.example.dummyvillebank;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class UserHomeActivity extends AppCompatActivity {

    private int enter_state = 0;

    // This case of overriding is obvious and concrete, no confusion
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Creating a new instance of an activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
    }

    public void showMoney(View v){
        // Get money qty
        TextView money = findViewById(R.id.moneyQuantity);
        String money_text = money.getText().toString();

        money.setVisibility(View.VISIBLE);

        Button show = (Button) v;
        Button hide = (Button) findViewById(R.id.hideButton);

        show.setEnabled(false);
        show.setClickable(false);
        show.setVisibility(View.INVISIBLE);
        hide.setEnabled(true);
        hide.setClickable(true);
        hide.setVisibility(View.VISIBLE);
    }
    public void hideMoney(View v){
        // Get money qty
        TextView money = findViewById(R.id.moneyQuantity);
        String money_text = money.getText().toString();

        money.setVisibility(View.INVISIBLE);

        Button hide = (Button) v;
        Button show = (Button) findViewById(R.id.toggle);

        hide.setEnabled(false);
        hide.setClickable(false);
        hide.setVisibility(View.INVISIBLE);
        show.setEnabled(true);
        show.setClickable(true);
        show.setVisibility(View.VISIBLE);
    }

    // Deposit
    // Moving deposit_handler and withdraw_handler into their respective
    // and changing those events' void type to Integer would be poor
    // design. These handlers solve this flaw
    private Integer deposit(Integer base, Integer dep){ // Do NOT ignore these return values!!
        if(dep > 0){
            return base + dep;
        }
        else{
            return base;
        }
    }
//    private double deposit(double base, double dep){
//        if(dep > 0){
//            return base + dep;
//        }
//        else{
//            return base;
//        }
//    }
//    private int deposit(int base, int dep){
//        if(dep > 0){
//            return base + dep;
//        }
//        else{
//            return base;
//        }
//    }
    // ^^^Unnecessary Overloading causing confusion and differentiating between value types^^^
    // Violates Recommendations MET50-J and MET51-J
    private Integer withdraw(Integer base, Integer wit){ // Do NOT ignore these return values!!
        if(base >= wit){
            return base - wit;
        }
        else{
            return base;
        }
    }
    // Ignoring return values above violates EXP00-J

    public void deposit_button(View v){
        Button dep = (Button) v;
        Button wit = (Button) findViewById(R.id.withdraw);
        Button ntr = (Button) findViewById(R.id.enter);

        TextView dep_amt = findViewById(R.id.depositAmt);

        dep.setVisibility(View.INVISIBLE);
        dep.setEnabled(false);
        dep.setClickable(false);
        wit.setVisibility(View.INVISIBLE);
        wit.setEnabled(false);
        wit.setClickable(false);

        ntr.setVisibility(View.VISIBLE);
        ntr.setEnabled(true);
        ntr.setClickable(true);

        dep_amt.setVisibility(View.VISIBLE);
        dep_amt.setEnabled(true);
        dep_amt.setClickable(true);

        enter_state = 1;
    }

    public void withdraw_button(View v){
        Button wit = (Button) v;
        Button dep = (Button) findViewById(R.id.deposit);
        Button ntr = (Button) findViewById(R.id.enter);

        TextView wit_amt = findViewById(R.id.withdrawAmt);

        wit.setVisibility(View.INVISIBLE);
        wit.setEnabled(false);
        wit.setClickable(false);
        dep.setVisibility(View.INVISIBLE);
        dep.setEnabled(false);
        dep.setClickable(false);

        ntr.setVisibility(View.VISIBLE);
        ntr.setEnabled(true);
        ntr.setClickable(true);

        wit_amt.setVisibility(View.VISIBLE);
        wit_amt.setEnabled(true);
        wit_amt.setClickable(true);

        enter_state = 2;
    }

    public void enter_button(View v){
        if(enter_state == 1){
            TextView dep_amt = findViewById(R.id.depositAmt); // amount the user inputs
            TextView base_amt = findViewById(R.id.moneyQuantity); // current account qty

            // input validation
            String dep_amt_text = dep_amt.getText().toString();
            String base_amt_text = base_amt.getText().toString();

            if(!dep_amt_text.equals("")){ // Uses String.equals() instead of Object.equals()
                Integer amount = Integer.parseInt(dep_amt_text);
                Integer base = Integer.parseInt(base_amt_text);
                Integer newAmt = deposit(base, amount);
                base_amt.setText(newAmt.toString());
            }

            // set the amount back to default invisible
            dep_amt.setVisibility(View.INVISIBLE);
            dep_amt.setEnabled(false);
            dep_amt.setClickable(false);
        }
        else if(enter_state == 2){
            TextView wit_amt = findViewById(R.id.withdrawAmt); // amount the user inputs
            TextView base_amt = findViewById(R.id.moneyQuantity); // current account qty

            String wit_amt_text = wit_amt.getText().toString();
            String base_amt_text = base_amt.getText().toString();

            if(!wit_amt_text.equals("")){
                Integer amount = Integer.parseInt(wit_amt_text);
                Integer base = Integer.parseInt(base_amt_text);
                Integer newAmt = withdraw(base, amount);
                base_amt.setText(newAmt.toString());
            }
            // set the amount back to default invisible
            wit_amt.setVisibility(View.INVISIBLE);
            wit_amt.setEnabled(false);
            wit_amt.setClickable(false);
        }
        // set back to original state
        Button dep = findViewById(R.id.deposit);
        Button wit = findViewById(R.id.withdraw);
        Button ntr = (Button) v;

        dep.setVisibility(View.VISIBLE);
        dep.setEnabled(true);
        dep.setClickable(true);
        wit.setVisibility(View.VISIBLE);
        wit.setEnabled(true);
        wit.setClickable(true);

        ntr.setVisibility(View.INVISIBLE);
        ntr.setEnabled(false);
        ntr.setClickable(false);
    }

}