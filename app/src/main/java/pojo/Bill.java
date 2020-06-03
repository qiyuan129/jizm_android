package pojo;

import java.util.Date;

public class Bill {
    private int bill_id;
    private int account_id;
    private int category_id;
    private int user_id;
    private int type;
    private String bill_name;
    private Date bill_date;
    private double bill_money;
    private int state;
    private Date anchor;

    public Bill(int bill_id, int account_id, int category_id, int user_id, int type, String bill_name, Date bill_date, double bill_money, int state, Date anchor) {
        this.bill_id = bill_id;
        this.account_id = account_id;
        this.category_id = category_id;
        this.user_id = user_id;
        this.type = type;
        this.bill_name = bill_name;
        this.bill_date = bill_date;
        this.bill_money = bill_money;
        this.state = state;
        this.anchor = anchor;
    }

    public int getBill_id() {
        return bill_id;
    }

    public void setBill_id(int bill_id) {
        this.bill_id = bill_id;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBill_name() {
        return bill_name;
    }

    public void setBill_name(String bill_name) {
        this.bill_name = bill_name;
    }

    public Date getBill_date() {
        return bill_date;
    }

    public void setBill_date(Date bill_date) {
        this.bill_date = bill_date;
    }

    public double getBill_money() {
        return bill_money;
    }

    public void setBill_money(double bill_money) {
        this.bill_money = bill_money;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Date getAnchor() {
        return anchor;
    }

    public void setAnchor(Date anchor) {
        this.anchor = anchor;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "bill_id=" + bill_id +
                ", account_id=" + account_id +
                ", category_id=" + category_id +
                ", user_id=" + user_id +
                ", type=" + type +
                ", bill_name='" + bill_name + '\'' +
                ", bill_date=" + bill_date +
                ", bill_money=" + bill_money +
                ", state=" + state +
                ", anchor=" + anchor +
                '}';
    }
}
