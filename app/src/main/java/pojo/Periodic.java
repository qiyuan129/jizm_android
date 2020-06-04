package pojo;

import java.util.Date;

public class Periodic {
    private int periodic_id;
    private int account_id;
    private int category_id;
    private int user_id;
    private int type;
    private String periodic_name;
    private int cycle;
    private Date start;
    private Date end;
    private double periodic_money;
    private int state;
    private Date anchor;

    public Periodic(int periodic_id, int account_id, int category_id, int user_id, int type, String periodic_name, int cycle, Date start, Date end, double periodic_money, int state, Date anchor) {
        this.periodic_id = periodic_id;
        this.account_id = account_id;
        this.category_id = category_id;
        this.user_id = user_id;
        this.type = type;
        this.periodic_name = periodic_name;
        this.cycle = cycle;
        this.start = start;
        this.end = end;
        this.periodic_money = periodic_money;
        this.state = state;
        this.anchor = anchor;
    }

    public int getPeriodic_id() {
        return periodic_id;
    }

    public void setPeriodic_id(int periodic_id) {
        this.periodic_id = periodic_id;
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

    public String getPeriodic_name() {
        return periodic_name;
    }

    public void setPeriodic_name(String periodic_name) {
        this.periodic_name = periodic_name;
    }

    public int getCycle() {
        return cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public double getPeriodic_money() {
        return periodic_money;
    }

    public void setPeriodic_money(double periodic_money) {
        this.periodic_money = periodic_money;
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
        return "Periodic{" +
                "periodic_id=" + periodic_id +
                ", account_id=" + account_id +
                ", category_id=" + category_id +
                ", user_id=" + user_id +
                ", type=" + type +
                ", periodic_name='" + periodic_name + '\'' +
                ", cycle=" + cycle +
                ", start=" + start +
                ", end=" + end +
                ", periodic_money=" + periodic_money +
                ", state=" + state +
                ", anchor=" + anchor +
                '}';
    }
}
