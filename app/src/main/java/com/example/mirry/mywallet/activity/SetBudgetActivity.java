package com.example.mirry.mywallet.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mirry.mywallet.R;
import com.example.mirry.mywallet.utils.SharedPreferencesUtils;

public class SetBudgetActivity extends Activity implements View.OnClickListener {
    private Button back;
    private Button confirm;
    private EditText budgetMoney;
    private EditText dateSettle;

    private void assignViews() {
        back = (Button) findViewById(R.id.back);
        confirm = (Button) findViewById(R.id.confirm);
        budgetMoney = (EditText) findViewById(R.id.budget_money);
        dateSettle = (EditText) findViewById(R.id.date_settle);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_budget);

        assignViews();

        back.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        int money = SharedPreferencesUtils.getInt(SetBudgetActivity.this, "money", 0);
        if (money > 0 ) {
            budgetMoney.setText(money + "");
        }
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.confirm:
                String budget = budgetMoney.getText().toString().trim();
                int money = Integer.parseInt(budget);
                if (money >= 0) {
                    SharedPreferencesUtils.setInt(SetBudgetActivity.this,"money",money);
                    finish();
                }else {
                    Toast.makeText(SetBudgetActivity.this, "预算资金不能小于0", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }

    }
}
