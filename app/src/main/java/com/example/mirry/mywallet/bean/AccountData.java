package com.example.mirry.mywallet.bean;

/**
 * Created by mirry on 2016/9/10 16:24.
 * 添加的账目数据，需要保存
 */
public class AccountData {

    private int _id;
    private String type;     //收入  支出
    private String itemTypeName;        //账目类型名称
    private int ItemTypePic;           //账目类型图片
    private int money;                 //账目金额
    private String remark;             //备注
    private String date;               //日期

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getItemTypeName() {
        return itemTypeName;
    }

    public void setItemTypeName(String itemTypeName) {
        this.itemTypeName = itemTypeName;
    }

    public int getItemTypePic() {
        return ItemTypePic;
    }

    public void setItemTypePic(int itemTypePic) {
        ItemTypePic = itemTypePic;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
