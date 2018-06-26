package com.example.mirry.mywallet.bean;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mirry.mywallet.R;

/**
 * Created by mirry on 2016/9/10 16:53.
 * 添加愿望的数据，需要保存
 */
public class WishData {

    private int _id;
    private String wish;
    private int wishMoney;
    private int wishPic;
    private String remark;
    private int progress;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getWish() {
        return wish;
    }

    public void setWish(String wish) {
        this.wish = wish;
    }

    public int getWishPic() {
        return wishPic;
    }

    public void setWishPic(int wishPic) {
        this.wishPic = wishPic;
    }

    public int getWishMoney() {
        return wishMoney;
    }

    public void setWishMoney(int wishMoney) {
        this.wishMoney = wishMoney;
    }

}
