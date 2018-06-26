package com.example.mirry.mywallet.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.mirry.mywallet.R;
import com.example.mirry.mywallet.fragment.DetailFragment;
import com.example.mirry.mywallet.fragment.MeFragment;
import com.example.mirry.mywallet.fragment.StatementFragment;
import com.example.mirry.mywallet.fragment.WishFragment;
import com.example.mirry.mywallet.utils.DrawableUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {
    private FrameLayout flContent;
    private RadioGroup radioGroup;

    private void assignViews() {
        flContent = (FrameLayout) findViewById(R.id.fl_content);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        RadioButton detail = (RadioButton) findViewById(R.id.detail);
        RadioButton wish = (RadioButton) findViewById(R.id.wish);
        RadioButton statement = (RadioButton) findViewById(R.id.statement);

        DrawableUtil.setDrawableSize(this,detail,R.drawable.detail,80);
        DrawableUtil.setDrawableSize(this,wish,R.drawable.wish,80);
        DrawableUtil.setDrawableSize(this,statement,R.drawable.chart,80);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);     //去掉标题栏
        setContentView(R.layout.activity_main);

        setBehindContentView(R.layout.menu_left_me);  //设置侧边栏布局

        SlidingMenu slidingMenu = getSlidingMenu();   //获取侧边栏对象
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);   //设置全屏触摸
        slidingMenu.setFadeDegree(0.5f);    //设置渐入渐出效果

        slidingMenu.setBehindOffset(150);    //设置预留屏幕的宽度

        FragmentManager fragmentManager = getFragmentManager();
        //开启事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_left,new MeFragment(),"Me");
        transaction.commit();

        assignViews();   //找到控件

        initData();
    }

    private void initData() {
        radioGroup.check(R.id.detail);
        setDefaultFragment();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (checkedId){
                    case R.id.detail:
                        transaction.replace(R.id.fl_content,new DetailFragment(),"Detail");
                        break;
                    case R.id.wish:
                        transaction.replace(R.id.fl_content,new WishFragment(),"Wish");
                        break;
                    case R.id.statement:
                        transaction.replace(R.id.fl_content,new StatementFragment(),"Statement");
                        break;
                    default:
                        break;
                }
                transaction.commit();
            }
        });
    }

    private void setDefaultFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        //开启事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_content,new DetailFragment());
        transaction.commit();
    }

    public MeFragment findMeFragment(){
        FragmentManager manager = getFragmentManager();
        MeFragment me = (MeFragment) manager.findFragmentByTag("Me");
        return me;
    }

}
