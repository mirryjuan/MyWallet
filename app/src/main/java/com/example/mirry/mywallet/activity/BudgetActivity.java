package com.example.mirry.mywallet.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mirry.mywallet.R;
import com.example.mirry.mywallet.utils.SharedPreferencesUtils;
import com.romainpiel.titanic.library.Titanic;
import com.romainpiel.titanic.library.TitanicTextView;

public class BudgetActivity extends Activity implements View.OnClickListener {
    private Button back;
    private TextView budgetMoney;
    private Button addBudget;
    private TitanicTextView restMoney;
    private int totalExpense;

    private void assignViews() {
        back = (Button) findViewById(R.id.back);
        budgetMoney = (TextView) findViewById(R.id.money_budget);
        addBudget = (Button) findViewById(R.id.add_budget);
        restMoney = (TitanicTextView) findViewById(R.id.money_rest);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        assignViews();

        Titanic titanic = new Titanic();
        titanic.start(restMoney);

        back.setOnClickListener(this);
        addBudget.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        int money = SharedPreferencesUtils.getInt(BudgetActivity.this, "money", 0);
        budgetMoney.setText(money + "");

        totalExpense = SharedPreferencesUtils.getInt(BudgetActivity.this, "totalExpense", 0);
        SharedPreferencesUtils.setInt(BudgetActivity.this,"restMoney",money - totalExpense);
        int mMoney = SharedPreferencesUtils.getInt(BudgetActivity.this, "restMoney", 0);
        restMoney.setText(mMoney+"");

        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.add_budget:
                Intent intent = new Intent();
                intent.setClass(BudgetActivity.this,SetBudgetActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

}
