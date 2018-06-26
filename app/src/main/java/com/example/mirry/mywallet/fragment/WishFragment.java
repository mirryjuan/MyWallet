package com.example.mirry.mywallet.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mirry.mywallet.R;
import com.example.mirry.mywallet.activity.AddWishActivity;
import com.example.mirry.mywallet.bean.WishData;
import com.example.mirry.mywallet.database.MyOpenHelper;
import com.example.mirry.mywallet.views.IconFontTextView;

import java.util.ArrayList;

/**
 * Created by mirry on 2016/9/9 11:54.
 */
public class WishFragment extends Fragment implements View.OnClickListener {

    private Activity myActivity;

    //愿望页的控件
    private IconFontTextView addWishes;
    private TextView money;
    private ListView wishes;

    private int totalWishMoney = 0;   //总共的愿望资金

    private ArrayList<WishData> wishesLists;
    private TextView hint;

    private WishAdapter adapter;
    private SQLiteDatabase db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(myActivity, R.layout.fragment_wish,null);
        findAllViews(view);         //找到需要的控件

        initData();

        addWishes.setOnClickListener(this);       //添加愿望

        return view;
    }

    private void initData() {
        wishesLists  = new ArrayList<>();
        MyOpenHelper helper = new MyOpenHelper(myActivity);
        db = helper.getReadableDatabase();
        adapter = new WishAdapter(myActivity, R.layout.item_list_wish, null,
                new String[]{"wish", "wishMoney", "wishPic", "remark"},
                new int[]{R.id.wish, R.id.money_wish, R.id.pic_wish, R.id.remark_wish}, 0);

        wishes.setAdapter(adapter);
        refreshListView();
    }

    //    该方法用于刷新愿望列表
    private void refreshListView() {
        //对数据库进行查询
        Cursor cursor = db.query("wishes", null, null, null, null, null, "_id desc", null);
        if (cursor.getCount() <= 0) {
            hint.setVisibility(View.VISIBLE);
        } else {
            hint.setVisibility(View.GONE);
        }
        wishesLists.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String wish = cursor.getString(cursor.getColumnIndex("wish"));
            int wishMoney = cursor.getInt(cursor.getColumnIndex("wishMoney"));
            int wishPic = cursor.getInt(cursor.getColumnIndex("wishPic"));
            String remark = cursor.getString(cursor.getColumnIndex("remark"));
            int wishProgress = cursor.getInt(cursor.getColumnIndex("wishProgress"));
            WishData wishData = new WishData();
            wishData.set_id(id);
            wishData.setWish(wish);
            wishData.setWishMoney(wishMoney);
            wishData.setWishPic(wishPic);
            wishData.setRemark(remark);
            wishData.setProgress(wishProgress);
            wishesLists.add(wishData);
        }
        adapter.changeCursor(cursor);
    }


    private void findAllViews(View view) {
        addWishes = (IconFontTextView) view.findViewById(R.id.btn_add);
        money = (TextView) view.findViewById(R.id.money);
        wishes = (ListView) view.findViewById(R.id.lv_wish);
        hint = (TextView) view.findViewById(R.id.hint);
    }

    //添加愿望单击事件
    @Override
    public void onClick(View v) {
        startActivity(new Intent(myActivity, AddWishActivity.class));
    }

    private class WishAdapter extends SimpleCursorAdapter {
        private final Context context;

        public WishAdapter(Context context, int layout, Cursor cursor, String[] from, int[] to, int flags) {
            super(context, layout, cursor, from, to, flags);
            this.context = context;
        }


        @Override
        public WishData getItem(int position) {
            return wishesLists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getCount() {
            return wishesLists.size();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(context, R.layout.item_list_wish, null);

                holder.wish = (TextView) convertView.findViewById(R.id.wish);
                holder.wishMoney = (TextView) convertView.findViewById(R.id.money_wish);
                holder.wishPic = (ImageView) convertView.findViewById(R.id.pic_wish);
                holder.wishProgress = (TextView) convertView.findViewById(R.id.progress_wish);
                holder.wishRemark = (TextView) convertView.findViewById(R.id.remark_wish);
                holder.progress = (ProgressBar) convertView.findViewById(R.id.progressBar);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            WishData wishData = wishesLists.get(position);
            holder.wish.setText(wishData.getWish());
            holder.wishRemark.setText(wishData.getRemark());
            holder.wishMoney.setText(wishData.getWishMoney()+"");
            holder.wishProgress.setText(""+wishData.getProgress());
//          holder.wishPic.setBackgroundResource(wishData.getWishPic());

            convertView.setClickable(true);
            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new AlertDialog.Builder(myActivity).setMessage("删除该愿望?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            WishData wishData = (WishData) wishes.getItemAtPosition(position);
                            int id = wishData.get_id();
                            db.delete("wishes", "_id=?", new String[]{"" + id});
                            adapter.notifyDataSetChanged();
                            refreshListView();
                            Toast.makeText(myActivity, "删除成功", Toast.LENGTH_SHORT).show();
                        }
                    }).setNegativeButton("取消",null).show();
                    return false;
                }
            });

            return convertView;
        }

        private class ViewHolder
        {
            TextView wish;
            TextView wishMoney;
            TextView wishProgress;
            TextView wishRemark;
            ImageView wishPic;
            ProgressBar progress;
            ImageButton delete;
            Button accomplish;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshListView();
    }
}
