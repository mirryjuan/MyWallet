package com.example.mirry.mywallet.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mirry.mywallet.R;
import com.example.mirry.mywallet.activity.MainActivity;
import com.example.mirry.mywallet.utils.SharedPreferencesUtils;

public class MeFragment extends Fragment {
    private Activity myActivity;

    private ImageButton picHead;
    private TextView nameHead;
    private TextView moneyRest;
    private int money;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                int totalIncome = SharedPreferencesUtils.getInt(myActivity, "totalIncome", 0);
                int totalExpense = SharedPreferencesUtils.getInt(myActivity, "totalExpense", 0);
                money = totalIncome - totalExpense;

                moneyRest.setText(money+"");
            }
        }
    };

    public Handler getHandler(){
        return handler;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        findAllViews(view);

        return view;
    }

    private void findAllViews(View view) {
        picHead = (ImageButton) view.findViewById(R.id.pic_head);
        nameHead = (TextView) view.findViewById(R.id.name_head);
        moneyRest = (TextView) view.findViewById(R.id.money_rest);
    }

}
