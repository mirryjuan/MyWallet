package com.example.mirry.mywallet.bean;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by mirry on 2016/9/11 19:07.
 * 分类图表的数据，需要及时更新
 */
public class StatementData {
    private String type;
    private int categoryPic;
    private String categoryName;
    private String categoryPercentage;
    private int categoryMoney;
    private int color;

    public int getCategoryColor() {
        return categoryColor;
    }

    public void setCategoryColor(int categoryColor) {
        this.categoryColor = categoryColor;
    }

    private int categoryColor;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryPic() {
        return categoryPic;
    }

    public void setCategoryPic(int categoryPic) {
        this.categoryPic = categoryPic;
    }

    public String getCategoryPercentage() {
        return categoryPercentage;
    }

    public void setCategoryPercentage(String categoryPercentage) {
        this.categoryPercentage = categoryPercentage;
    }

    public int getCategoryMoney() {
        return categoryMoney;
    }

    public void setCategoryMoney(int categoryMoney) {
        this.categoryMoney = categoryMoney;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
