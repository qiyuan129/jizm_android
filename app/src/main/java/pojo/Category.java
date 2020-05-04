package pojo;

import java.util.Date;

public class Category {
    private int category_id;
    private int user_id;
    private String category_name;
    private int type;
    private int state;
    private long anchor;


    public Category(int category_id, int user_id, String category_name, int type, int state, long anchor) {
        this.category_id = category_id;
        this.user_id = user_id;
        this.category_name = category_name;
        this.type = type;
        this.state = state;
        this.anchor = anchor;
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

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
