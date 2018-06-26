package com.example.mirry.mywallet.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.mirry.mywallet.R;
import com.example.mirry.mywallet.database.MyOpenHelper;

public class AddWishActivity extends Activity implements View.OnClickListener {
    private ImageButton addPic;
    private EditText addWish;
    private EditText addMoney;
    private EditText addRemark;
    private Button wishMake;
    private Button back;

    private void assignViews() {
        addPic = (ImageButton) findViewById(R.id.add_pic);
        addWish = (EditText) findViewById(R.id.add_wish);
        addMoney = (EditText) findViewById(R.id.add_money);
        addRemark = (EditText) findViewById(R.id.add_remark);
        wishMake = (Button) findViewById(R.id.wish_make);
        back = (Button) findViewById(R.id.back);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wish);

        assignViews();

        addPic.setOnClickListener(this);
        wishMake.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_pic:
                // TODO: 2016/9/11 打开菜单  选择相机拍照，图片或取消  提交时保存
                break;
            case R.id.wish_make:
                String wish = addWish.getText().toString().trim();
                String mMoney = addMoney.getText().toString().trim();
                String remark = addRemark.getText().toString().trim();

                if (TextUtils.isEmpty(wish) || TextUtils.isEmpty(mMoney)) {
                    Toast.makeText(AddWishActivity.this, "愿望或愿望资金不能为空", Toast.LENGTH_SHORT).show();
                }else {
                    int money = Integer.parseInt(mMoney);
                    MyOpenHelper helper = new MyOpenHelper(getApplicationContext());
                    SQLiteDatabase db = helper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("wish",wish);
                    values.put("wishMoney",money);
                    // TODO: 2016/9/14 设为选择的图片
                    values.put("wishPic",R.drawable.add);
                    values.put("remark",remark);
                    db.insert("wishes",null,values);
                    finish();
                }
                break;
            case R.id.back:
                finish();
                break;
        }
    }
}
