package com.example.mirry.mywallet.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.mirry.mywallet.R;
import com.example.mirry.mywallet.bean.StatementData;
import com.example.mirry.mywallet.database.MyOpenHelper;
import com.example.mirry.mywallet.views.IconFontTextView;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by mirry on 2016/9/9 13:16.
 */
public class StatementFragment extends Fragment {
    private Activity myActivity;

    private static String INCOME = "收入";
    private static String EXPENSE = "支出";

    private String type = EXPENSE;

    private RadioGroup rgType;
    private Button date;
    private PieChart pieChart;
    private ListView categoryDetail;

    private ArrayList<StatementData> categoryLists = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity = getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(myActivity, R.layout.fragment_statement,null);
        findAllViews(view);         //找到需要的控件

        rgType.check(R.id.expense);    //默认选中支出
        initData();

        final PieData pieData = getPieData(categoryLists.size(), 100);
        setPieChartData(pieChart,pieData);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2016/9/11   显示日期选择器   选择日期
            }
        });

        rgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.income:
                        if (type.equals(EXPENSE)) {
                            type = INCOME;
                            initData();
                            final PieData pieData = getPieData(categoryLists.size(), 100);
                            setPieChartData(pieChart,pieData);
                        }
                        break;
                    case R.id.expense:
                        if (type.equals(INCOME)) {
                            type = EXPENSE;
                            initData();
                            final PieData pieData = getPieData(categoryLists.size(), 100);
                            setPieChartData(pieChart,pieData);
                        }
                        break;
                    default:
                        break;
                }
            }
        });


        categoryDetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: 2016/9/11    点击查看详情
            }
        });

        return view;
    }

    private void setPieChartData(PieChart pieChart, PieData pieData) {
        pieChart.setHoleRadius(60f);               //半径    
        pieChart.setTransparentCircleRadius(64f);  //半透明圈 

        pieChart.setDrawHoleEnabled(true);
        pieChart.setDrawEntryLabels(false);

        pieChart.setDescription("");
        pieChart.setCenterTextSize(20);

        pieChart.setNoDataText("什么都没有...");

        pieChart.setDrawCenterText(true);         //饼状图中间可以添加文字 
        if (type.equals(INCOME)) {
            pieChart.setCenterText("总收入");        //饼状图中间的文字

        }else {
            pieChart.setCenterText("总支出");        //饼状图中间的文字
        }

        pieChart.setRotationAngle(90);           // 初始旋转角度
        pieChart.setRotationEnabled(true);      // 可以手动旋转
        pieChart.setUsePercentValues(false);     //显示成百分比

        pieChart.animateXY(1000, 1000);         //设置动画

        pieChart.setData(pieData);

        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);

    }

    private PieData getPieData(int count, float range) {
        int sum = 0;

        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();  //yVals用来表示封装每个饼块的实际数据
        ArrayList<Integer> colors = new ArrayList<Integer>();     //每一部分的颜色数据

        for (int i = 0 ; i< count;i++){
            StatementData statementData = categoryLists.get(i);
            int money = statementData.getCategoryMoney();
            sum += money;
        }

        if(sum <= 0){
            return null;
        }else {
            // 饼图数据(百分比)
            for (int i = 0;i<count;i++) {
                StatementData statementData = categoryLists.get(i);
                int money = statementData.getCategoryMoney();

                BigDecimal decimal = new BigDecimal(money * 100).divide(new BigDecimal(sum),2, BigDecimal.ROUND_HALF_UP);
                float quarterly = decimal.floatValue();

                yValues.add(new PieEntry(quarterly,i));
                statementData.setCategoryPercentage(quarterly +"%");
            }

            //y轴的集合
            PieDataSet pieDataSet = new PieDataSet(yValues, "");
            pieDataSet.setDrawValues(false);
            pieDataSet.setSliceSpace(0f); //设置个饼状图之间的距离


            // 饼图颜色
            colors.add(Color.rgb(205, 205, 205));
            colors.add(Color.rgb(114, 188, 223));
            colors.add(Color.rgb(255, 123, 124));
            colors.add(Color.rgb(57, 135, 200));

            pieDataSet.setColors(colors);

            DisplayMetrics metrics = getResources().getDisplayMetrics();
            float px = 2 * (metrics.densityDpi / 160f);
            pieDataSet.setSelectionShift(px);         // 选中态多出的长度

            PieData pieData = new PieData(pieDataSet);
            pieData.setValueTextSize(14f);          //设置颜色

            return pieData;
        }

    }

    private void initData() {
        categoryLists = new ArrayList<>();

        MyOpenHelper helper = new MyOpenHelper(myActivity);
        SQLiteDatabase db = helper.getReadableDatabase();
        if (type.equals(INCOME)) {
            //如果是收入
            Cursor cursor = db.query("statement", null, "type = ?", new String[]{INCOME}, null, null, null);
            while (cursor.moveToNext()){
                int categoryPic = cursor.getInt(cursor.getColumnIndex("categoryPic"));
                String categoryName = cursor.getString(cursor.getColumnIndex("categoryName"));
                int categoryMoney = cursor.getInt(cursor.getColumnIndex("categoryMoney"));
                int color = cursor.getInt(cursor.getColumnIndex("color"));

                StatementData statementData = new StatementData();
                statementData.setCategoryPic(categoryPic);
                statementData.setCategoryName(categoryName);
                statementData.setCategoryMoney(categoryMoney);
                statementData.setColor(color);
                categoryLists.add(statementData);
            }
        }else {
            //如果为支出
            Cursor cursor = db.query("statement", null, "type = ?", new String[]{EXPENSE}, null, null, null);
                while (cursor.moveToNext()){
                    int categoryPic = cursor.getInt(cursor.getColumnIndex("categoryPic"));
                    String categoryName = cursor.getString(cursor.getColumnIndex("categoryName"));
                    int categoryMoney = cursor.getInt(cursor.getColumnIndex("categoryMoney"));
                    int color = cursor.getInt(cursor.getColumnIndex("color"));

                    StatementData statementData = new StatementData();
                    statementData.setCategoryPic(categoryPic);
                    statementData.setCategoryName(categoryName);
                    statementData.setCategoryMoney(categoryMoney);
                    statementData.setColor(color);
                    categoryLists.add(statementData);
                }
        }
        categoryDetail.setAdapter(new CategoryAdapter());
    }

    private void findAllViews(View view) {
        rgType = (RadioGroup) view.findViewById(R.id.rg_type);
        date = (Button) view.findViewById(R.id.date_chart);
        pieChart = (PieChart) view.findViewById(R.id.chart_pie);
        categoryDetail = (ListView) view.findViewById(R.id.category_detail);
    }

    private class CategoryAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return categoryLists.size();
        }

        @Override
        public StatementData getItem(int position) {
            return categoryLists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(myActivity,R.layout.item_list_category,null);

                holder.categoryPic = (IconFontTextView) convertView.findViewById(R.id.category_pic);
                holder.categoryName = (TextView) convertView.findViewById(R.id.category_name);
                holder.categoryPercentage = (TextView) convertView.findViewById(R.id.category_percentage);
                holder.categoryMoney = (TextView) convertView.findViewById(R.id.category_money);

                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            StatementData statementData = categoryLists.get(position);

            if (statementData != null) {
                holder.categoryPic.setText(getString(statementData.getCategoryPic()));
                holder.categoryPic.setTextColor(getResources().getColor(statementData.getColor()));
                holder.categoryName.setText(statementData.getCategoryName());
                holder.categoryPercentage.setText(statementData.getCategoryPercentage());
                holder.categoryMoney.setText(statementData.getCategoryMoney()+"");
            }

            return convertView;
        }

        private class ViewHolder
        {
            IconFontTextView categoryPic;
            TextView categoryName;
            TextView categoryPercentage;
            TextView categoryMoney;
        }

    }

}
