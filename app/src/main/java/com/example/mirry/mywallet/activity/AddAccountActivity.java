package com.example.mirry.mywallet.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mirry.mywallet.R;
import com.example.mirry.mywallet.bean.CategoryData;
import com.example.mirry.mywallet.database.MyOpenHelper;
import com.example.mirry.mywallet.views.IconFontTextView;

import java.util.ArrayList;
import java.util.Calendar;

public class AddAccountActivity extends Activity implements View.OnClickListener {
    private static String INCOME = "收入";
    private static String EXPENSE = "支出";

    private int totalMoney;

    private Button back;               //返回按钮
    private RadioGroup rgType;        //支出 OR 收入
    private Button confirm;           // 提交按钮
    private Button date;              //日期
    private GridView gvDetail;        //消费类别列表
    private EditText money;           //消费金额
    private Button remark;            //备注

    private String type = EXPENSE;

    private ArrayList<CategoryData> categoryLists = null;
    private String[] expenseName = new String[]{
            "一般","餐饮","交通","水果","零食",
            "饮品","护肤彩妆","话费", "房租","学习",
            "礼物","红包","药品","旅行","衣服鞋包",
            "生活用品","电影"
    };
    private int[] expensePic = new int[]{
            R.string.defpic,R.string.repast,R.string.traffic,R.string.fruit,R.string.snacks,
            R.string.drink,R.string.cosmetics,R.string.phone,R.string.home,R.string.study,
            R.string.gift,R.string.redpacket,R.string.medicine,R.string.travel,R.string.clothing,
            R.string.life,R.string.movie
    };
    private int[] expenseColors = new int[]{
            R.color.golden, R.color.green, R.color.blueGrey, R.color.lightGreen, R.color.magenta,
            R.color.lightYellow, R.color.orange, R.color.red, R.color.azure, R.color.purple,
            R.color.orange, R.color.red, R.color.blueGrey, R.color.lightGreen, R.color.magenta,
            R.color.green, R.color.purple
    };
    private String[] incomeName = new String[]{
            "一般","工资","零花钱","红包","兼职"
    };
    private int[] incomePic = new int[]{
            R.string.defpic,R.string.salary,R.string.pocket,R.string.redpacket,R.string.job
    };
    private int[] incomeColors = new int[]{
            R.color.golden, R.color.blueGrey, R.color.magenta, R.color.red, R.color.lightYellow
    };
    private String categoryName;
    private int categoryPic;
    private int mColor;
    private TextView remarkContent;
    private Button remarkAccomplish;
    private RelativeLayout rlRemark;
    private String myRemark;
    private RelativeLayout rlMoney;
    private AccountCategoryAdapter adapter;

    private void assignViews() {
        rgType = (RadioGroup) findViewById(R.id.rg_type);

        back = (Button) findViewById(R.id.back);
        confirm = (Button) findViewById(R.id.confirm);

        date = (Button) findViewById(R.id.date);

        gvDetail = (GridView) findViewById(R.id.gv_detail);

        money = (EditText) findViewById(R.id.money);
        remark = (Button) findViewById(R.id.remark);

        remarkContent = (TextView) findViewById(R.id.content_remark);
        remarkAccomplish = (Button) findViewById(R.id.accomplish_remark);

        rlRemark = (RelativeLayout) findViewById(R.id.rl_remark);
        rlMoney = (RelativeLayout) findViewById(R.id.rl_money);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);

        assignViews();

        initData();
        date.setOnClickListener(this);
        back.setOnClickListener(this);
        confirm.setOnClickListener(this);
        remark.setOnClickListener(this);

        rgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.income:
                        if (type == EXPENSE) {
                            type = INCOME;
                            setListsData();
                            adapter.setSelection(0);
                            getAccountCategory(0);
                        }
                        break;
                    case R.id.expense:
                        if (type == INCOME) {
                            type = EXPENSE;
                            setListsData();
                            adapter.setSelection(0);
                            getAccountCategory(0);
                        }
                        break;
                    default:
                        break;
                }
            }
        });

        gvDetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setSelection(position);
                adapter.notifyDataSetChanged();
                getAccountCategory(position);
            }
        });

    }

    private void getAccountCategory(int position) {
        CategoryData data = categoryLists.get(position);
        categoryName = data.getCategoryName();
        categoryPic = data.getCategoryPic();
        mColor = data.getColor();
    }

    private void initData() {
        rgType.check(R.id.expense);

        categoryLists = new ArrayList<>();
        setListsData();

        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        date.setText(month+"月"+day+"日");
        adapter.setSelection(0);
        getAccountCategory(0);
    }

    private void setListsData() {

        if (categoryLists != null) {
            categoryLists.clear();
        }
        if (type.equals(EXPENSE)) {
            for(int i = 0; i < expenseName.length ;i++ ){
                CategoryData data = new CategoryData();
                data.setCategoryName(expenseName[i]);
                data.setCategoryPic(expensePic[i]);
                data.setColor(expenseColors[i]);
                categoryLists.add(data);
            }
        } else {
            for (int i = 0; i < incomeName.length; i++) {
                CategoryData data = new CategoryData();
                data.setCategoryName(incomeName[i]);
                data.setCategoryPic(incomePic[i]);
                data.setColor(incomeColors[i]);
                categoryLists.add(data);
            }
        }
        adapter = new AccountCategoryAdapter();
        gvDetail.setAdapter(adapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.date:
                // TODO: 2016/9/11   打开日历  选择日期   提交时保存选择的日期
                break;
            case R.id.back:
                finish();
                break;
            case R.id.confirm:
                String myMoney = this.money.getText().toString();
                if (!TextUtils.isEmpty(myMoney)) {
                    int mMoney = Integer.parseInt(myMoney);
                    if (mMoney > 0 ) {
                        MyOpenHelper helper = new MyOpenHelper(getApplicationContext());
                        SQLiteDatabase db = helper.getWritableDatabase();
                        ContentValues accountsValues = new ContentValues();
                        accountsValues.put("type",type);
                        accountsValues.put("categoryName",categoryName);
                        accountsValues.put("categoryPic",categoryPic);
                        accountsValues.put("money", mMoney);
                        accountsValues.put("remark",myRemark);
                        accountsValues.put("color",mColor);
                        db.insert("accounts",null,accountsValues);

                        //插入或更新图表数据库
                        db = helper.getReadableDatabase();
                        ContentValues statementValues = new ContentValues();
                        Cursor cursor = db.query("statement", new String[]{"categoryMoney"}, "categoryName = ? and type = ?", new String[]{categoryName,type}, null, null, null);
                        if (!cursor.moveToNext()) {
                            totalMoney = 0;
                            totalMoney += mMoney;
                            statementValues.put("type",type);
                            statementValues.put("categoryName",categoryName);
                            statementValues.put("categoryPic",categoryPic);
                            statementValues.put("categoryMoney",totalMoney);
                            db.insert("statement",null,statementValues);
                        }else {
                            totalMoney = cursor.getInt(cursor.getColumnIndex("categoryMoney"));
                            totalMoney += mMoney;
                            statementValues.put("categoryMoney",totalMoney);
                            db.update("statement",statementValues,"categoryName = ? and type = ?",new String[]{categoryName,type});
                        }

                        //返回数据
                        Intent intent = new Intent();
                        intent.putExtra("isUpdate",true);
                        setResult(0,intent);

                        finish();
                    }else {
                        Toast.makeText(AddAccountActivity.this, "金额不能小于等于0", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(AddAccountActivity.this, "请输入金额", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.remark:
                rlMoney.setVisibility(View.GONE);
                rlRemark.setVisibility(View.VISIBLE);
                remarkAccomplish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myRemark = remarkContent.getText().toString().trim();
                        rlRemark.setVisibility(View.GONE);
                        rlMoney.setVisibility(View.VISIBLE);
                    }
                });
                break;
            default:
                break;
        }
    }

    private class AccountCategoryAdapter extends BaseAdapter {
        private int clickTemp = -1;
        public void setSelection(int position){
            clickTemp = position;
        }
        @Override
        public int getCount() {
            return categoryLists.size();
        }

        @Override
        public CategoryData getItem(int position) {
            return categoryLists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = new ViewHolder();
            if(convertView == null){
                convertView = View.inflate(AddAccountActivity.this,R.layout.item_grid_type,null);
                holder.categoryPic = (IconFontTextView) convertView.findViewById(R.id.type_pic);
                holder.categoryName = (TextView) convertView.findViewById(R.id.type_name);

                convertView.setTag(holder);
            }else {
               holder = (ViewHolder) convertView.getTag();
            }

            CategoryData data = getItem(position);
            holder.categoryName.setText(data.getCategoryName());
            holder.categoryPic.setText(getString(data.getCategoryPic()));
            holder.categoryPic.setTextColor(getColor(data.getColor()));
            if (clickTemp == position) {
                holder.categoryName.setTextColor(0xFFFF7D00);
            }else{
                holder.categoryName.setTextColor(0xFF444444);
            }

            return convertView;
        }

        private class ViewHolder {
            public IconFontTextView categoryPic;
            public TextView categoryName;
        }
    }
}
