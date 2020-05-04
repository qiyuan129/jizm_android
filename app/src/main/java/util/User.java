package util;

import android.content.SharedPreferences;

public class User {
    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;

    public User(SharedPreferences.Editor editor, SharedPreferences preferences) {
        this.editor = editor;
        this.preferences = preferences;
    }

    public void setUserId(int id) {
        editor.putInt("user_id",id);
    }

    public int getUserId() {
        int userId=preferences.getInt("user_id",0);
        return userId;
    }

    public void setUserName(String name) {
        editor.putString("user_name",name);
    }

    public String getUserName() {
        String name=preferences.getString("user_name","user");
        return name;
    }

    public void setPhone(String phone) {
        editor.putString("phone",phone);
    }

    public String getPhone() {
        String phone=preferences.getString("phone","");
        return phone;
    }

    public void setEmail(String email) {
        editor.putString("email",email);
    }

    public String getEmail() {
        String email=preferences.getString("email","");
        return email;
    }

    public void setPassword(String password) {
        editor.putString("password",password);
    }

    public String getPassword() {
        String password=preferences.getString("password","");
        return password;
    }

    public void setRemember(boolean remember) {
        editor.putBoolean("remember",remember);
    }

    public boolean getRemember() {
        boolean remember=preferences.getBoolean("remember",false);
        return remember;
    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }

    public void setEditor(SharedPreferences.Editor editor) {
        this.editor = editor;
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public void setPreferences(SharedPreferences preferences) {
        this.preferences = preferences;
    }
}
