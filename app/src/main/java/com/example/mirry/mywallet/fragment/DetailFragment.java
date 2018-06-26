package com.example.mirry.mywallet.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.mirry.mywallet.R;
import com.example.mirry.mywallet.activity.AddAccountActivity;
import com.example.mirry.mywallet.activity.BudgetActivity;
import com.example.mirry.mywallet.activity.MainActivity;
import com.example.mirry.mywallet.bean.AccountData;
import com.example.mirry.mywallet.bean.WishData;
import com.example.mirry.mywallet.database.MyOpenHelper;
import com.example.mirry.mywallet.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by mirry on 2016/9/8 21:33.
 */
public class DetailFragment extends Fragment implements View.OnClickListener {
    private static String INCOME = "收入";
    private static String EXPENSE = "支出";

    private Activity myActivity;

    private ImageButton addAccounts;             //添加按钮
    private TextView incomeItem;        //收入项目名
    private TextView expenseItem;      //支出项目名
    private TextView income;           //收入金额
    private TextView expense;         //支出金额
    private ImageButton budget;       //预算
    private ListView detail;          //账目列表
    private TextView budgetMoney;    //预算金额

    private int totalIncome = 0;          //总收入
    private int totalExpense = 0;         //总支出

    //获取当前的年月日
    private int year;
    private int month;
    private int day;

    private View alter;
    private View delete;

    private boolean isUpdate = false;

    private boolean isOpened = false;

    private ArrayList<AccountData> accountLists;
    private AccountDetailAdapter adapter;

    private SQLiteDatabase db;
    private TextView hint;
    private int money;
    private int mMoney;
    private int headerViewHeight;
    private SQLiteDatabase writableDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity = getActivity();
        MyOpenHelper helper = new MyOpenHelper(myActivity);
        db = helper.getReadableDatabase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(myActivity, R.layout.fragment_detail, null);        //fragment的布局
        findAllViews(view);         //找到需要的控件

        addAccounts.setOnClickListener(this);
        budget.setOnClickListener(this);

        initData();

        return view;
    }

    private void initData() {
        //获取系统的当前时间
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        day = calendar.get(Calendar.DAY_OF_MONTH);

        incomeItem.setText(month +"月份收入");
        expenseItem.setText(month +"月份支出");

        adapter = new AccountDetailAdapter();
        refreshListView();

        initHeaderView();
        detail.addFooterView(View.inflate(myActivity,R.layout.footer_list_detail,null));

        detail.setAdapter(adapter);
        detail.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            private int categoryMoney;

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(myActivity).setMessage("删除该条记录?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AccountData accountData = (AccountData) detail.getItemAtPosition(position);
                        int id = accountData.get_id();
                        String categoryName = accountData.getItemTypeName();
                        String type = accountData.getType();
                        db.delete("accounts","_id=?",new String[]{""+id});
                        // TODO: 2016/10/14 删除图表数据库的内容
                        System.out.println(type + ":" + categoryName + ":" + money);
                        Cursor cursor = db.query("statement", new String[]{"categoryMoney"},"categoryName = ? and type = ?",new String[]{categoryName,type}, null, null, null);
                        while (cursor.moveToNext()){
                            categoryMoney = cursor.getInt(cursor.getColumnIndex("categoryMoney"));
                            categoryMoney = categoryMoney - money;
                        }
                        if (categoryMoney <= 0) {
                            db.delete("statement","categoryName = ? and type = ?",new String[]{categoryName,type});
                        }else {
                            ContentValues values = new ContentValues();
                            values.put("categoryMoney",categoryMoney );
                            writableDatabase.update("statement", values,"categoryName = ? and type = ?",new String[]{categoryName,type});
                        }
                    adapter.notifyDataSetChanged();
                    isUpdate = true;
                    refreshListView();
                    Toast.makeText(myActivity, "删除成功", Toast.LENGTH_SHORT).show();

                    updateRest();   //更新余额

                }
            }).setNegativeButton("取消", null).show();
            return false;
            }
        });

        updateRest();   //更新余额
    }

    private void initHeaderView() {
        View headerView = View.inflate(myActivity, R.layout.header_list_detail,null);
        detail.addHeaderView(headerView);
    }

    private void findAllViews(View view) {
        addAccounts = (ImageButton) view.findViewById(R.id.btn_add);
        incomeItem = (TextView) view.findViewById(R.id.income);
        expenseItem = (TextView) view.findViewById(R.id.expense);
        income = (TextView) view.findViewById(R.id.tv_income);
        expense = (TextView) view.findViewById(R.id.tv_expense);
        budget = (ImageButton) view.findViewById(R.id.btn_budget);
        detail = (ListView) view.findViewById(R.id.lv_detail);
        budgetMoney = (TextView) view.findViewById(R.id.budget_money);
        hint = (TextView) view.findViewById(R.id.hint);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.btn_add:
                //添加账目
                intent.setClass(myActivity, AddAccountActivity.class);
                startActivityForResult(intent,0);
                break;
            case R.id.btn_budget:
                //设定预算
                intent.setClass(myActivity,BudgetActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private class AccountDetailAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return accountLists.size();
        }

        @Override
        public AccountData getItem(int position) {
            return accountLists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(myActivity,R.layout.item_list_detail,null);
                holder.incomeDetail = (TextView) convertView.findViewById(R.id.detail_account_income);
                holder.expenseDetail = (TextView) convertView.findViewById(R.id.detail_account_expense);
                holder.incomeRemark = (TextView) convertView.findViewById(R.id.detail_remark_income);
                holder.expenseRemark = (TextView) convertView.findViewById(R.id.detail_remark_expense);
                holder.itemType = (ImageButton) convertView.findViewById(R.id.item);

                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (getCount()>0){
                AccountData data = getItem(position);
                if (data!=null){
                    String type = data.getType();
                    if (type.equals(INCOME)) {
                        holder.incomeDetail.setText(data.getItemTypeName() + "  " +data.getMoney()+"元");
                        holder.incomeRemark.setText(data.getRemark());
                        holder.expenseDetail.setText(null);
                        holder.expenseRemark.setText(null);
                    }else if (type.equals(EXPENSE)){
                        holder.expenseDetail.setText(data.getItemTypeName() + "  " +data.getMoney()+"元");
                        holder.expenseRemark.setText(data.getRemark());
                        holder.incomeDetail.setText(null);
                        holder.incomeRemark.setText(null);
                    }
                    holder.itemType.setImageResource(data.getItemTypePic());
                }
            }


            return convertView;
        }

        private class ViewHolder{
            TextView incomeDetail;
            TextView expenseDetail;
            TextView incomeRemark;
            TextView expenseRemark;
            ImageButton itemType;
        }
    }

    //    该方法用来刷新账单列表
    private void refreshListView() {
        //对数据库进行查询
        accountLists = new ArrayList<>();
        accountLists.clear();
        totalExpense = 0;
        totalIncome = 0;
        Cursor cursor = db.query("accounts", null, null, null, null, null,"_id desc", null);
        if (cursor.getCount() <= 0) {
            hint.setVisibility(View.VISIBLE);
        } else {
            hint.setVisibility(View.GONE);
        }
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String type = cursor.getString(cursor.getColumnIndex("type"));
            String categoryName = cursor.getString(cursor.getColumnIndex("categoryName"));
            int categoryPic = cursor.getInt(cursor.getColumnIndex("categoryPic"));
            String remark = cursor.getString(cursor.getColumnIndex("remark"));
            int money = cursor.getInt(cursor.getColumnIndex("money"));
            AccountData data = new AccountData();
            data.set_id(id);
            data.setRemark(remark);
            data.setItemTypeName(categoryName);
            data.setItemTypePic(categoryPic);
            data.setMoney(money);
            data.setType(type);
            accountLists.add(data);
            if (type.equals(INCOME)) {
                totalIncome += money;
            } else if (type.equals(EXPENSE)) {
                totalExpense += money;
            }

        }
        if (isUpdate) {
            SharedPreferencesUtils.setInt(myActivity,"totalIncome",totalIncome);
            SharedPreferencesUtils.setInt(myActivity,"totalExpense",totalExpense);
            SharedPreferencesUtils.setInt(myActivity,"restMoney",money - totalExpense);

            updateRest();
        }

        totalIncome = SharedPreferencesUtils.getInt(myActivity, "totalIncome", 0);
        totalExpense = SharedPreferencesUtils.getInt(myActivity, "totalExpense", 0);
        mMoney = SharedPreferencesUtils.getInt(myActivity, "restMoney", 0);

        income.setText(totalIncome +"元");
        expense.setText(totalExpense+"元");
        budgetMoney.setText(mMoney +"");

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        money = SharedPreferencesUtils.getInt(myActivity, "money", 0);
        refreshListView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            isUpdate = data.getBooleanExtra("isUpdate", false);
        }
    }

    public void updateRest(){
        MainActivity activity = (MainActivity)myActivity;
        MeFragment meFragment = activity.findMeFragment();
        meFragment.getHandler().sendEmptyMessage(0);
    }
}
