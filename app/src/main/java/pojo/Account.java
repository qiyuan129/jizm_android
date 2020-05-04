package pojo;

import java.util.Date;

public class Account {
    private int account_id;
    private int user_id;
    private String account_name;
    private double money;
    private int state;
    private long anchor;

    public Account(int account_id, int user_id, String account_name, double money, int state, long anchor) {
        this.account_id = account_id;
        this.user_id = user_id;
        this.account_name = account_name;
        this.money = money;
        this.state = state;
        this.anchor = anchor;
    }


    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getAnchor() {
        return anchor;
    }

    public void setAnchor(long anchor) {
        this.anchor = anchor;
    }
}
