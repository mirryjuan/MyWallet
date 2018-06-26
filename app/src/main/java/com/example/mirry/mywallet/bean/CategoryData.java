package com.example.mirry.mywallet.bean;

/**
 * Created by mirry on 2016/9/13 16:39.
 * 添加账目时，类别选项的数据
 */
public class CategoryData {
    private int categoryPic;
    private String categoryName;
    private int color;

    public int getCategoryPic() {
        return categoryPic;
    }

    public void setCategoryPic(int categoryPic) {
        this.categoryPic = categoryPic;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
