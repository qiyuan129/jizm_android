package dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pojo.Account;
import util.MyDatabaseHelper;
import static com.myapplication.MainActivity.dbHelper;

public class AccountDAOImpl implements AccountDAO {

    @Override
    public List<Account> listAccount() {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Account account=null;
        List<Account> list=new ArrayList<>();

        Cursor cursor = db.query("account",null,null,null,null,null,null);
        if (cursor.moveToFirst())
        {
            do {
                int account_id=cursor.getInt(cursor.getColumnIndex("account_id"));
                int user_id=cursor.getInt(cursor.getColumnIndex("user_id"));
                String account_name=cursor.getString(cursor.getColumnIndex("account_name"));
                double money=cursor.getDouble(cursor.getColumnIndex("money"));
                int state=cursor.getInt(cursor.getColumnIndex("state"));
                Date anchor=new Date(cursor.getLong(cursor.getColumnIndex("anchor")));
                account=new Account(account_id,user_id,account_name,money,state,anchor);
                list.add(account);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    @Override
    public void insertAccount(Account account) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("user_id",account.getUser_id());
        values.put("account_name",account.getAccount_name());
        values.put("money",account.getMoney());
        values.put("state",account.getState());
        values.put("anchor",account.getAnchor().getTime());
        db.insert("account",null,values);

    }

    @Override
    public void updateAccount(Account account) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id",account.getUser_id());
        values.put("account_name",account.getAccount_name());
        values.put("money",account.getMoney());
        values.put("state",account.getState());
        values.put("anchor",account.getAnchor().getTime());

        db.update("account",values,"account_id = ?",new String[]{""+account.getAccount_id()});

    }

    @Override
    public Account getAccountById(int id) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Account account=null;
        Cursor cursor = db.query("account",null,"account_id = ?",new String[]{""+id},null,null,null);
        if (cursor.moveToFirst())
        {
            int category_id=cursor.getInt(cursor.getColumnIndex("category_id"));
            int user_id=cursor.getInt(cursor.getColumnIndex("user_id"));
            String category_name=cursor.getString(cursor.getColumnIndex("category_name"));
            int type=cursor.getInt(cursor.getColumnIndex("type"));
            int state=cursor.getInt(cursor.getColumnIndex("state"));
            Date anchor=new Date(cursor.getLong(cursor.getColumnIndex("anchor")));

            account=new Account(category_id,user_id,category_name,type,state,anchor);
        }
        cursor.close();
        return account;
    }

    @Override
    public List<Account> getSyncAccount() {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Account account=null;
        List<Account> list=new ArrayList<>();

        Cursor cursor = db.query("account",null,"state != ?",new String[]{"9"},null,null,null);
        if (cursor.moveToFirst())
        {
            do {
                int account_id=cursor.getInt(cursor.getColumnIndex("account_id"));
                int user_id=cursor.getInt(cursor.getColumnIndex("user_id"));
                String account_name=cursor.getString(cursor.getColumnIndex("account_name"));
                double money=cursor.getDouble(cursor.getColumnIndex("money"));
                int state=cursor.getInt(cursor.getColumnIndex("state"));
                Date anchor=new Date(cursor.getLong(cursor.getColumnIndex("anchor")));
                account=new Account(account_id,user_id,account_name,money,state,anchor);
                list.add(account);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    @Override
    public Date getMaxAnchor() {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        long anchor=0;
        Cursor cursor = db.query("account",null,null,null,null,null,"anchor desc");

        if (cursor.moveToFirst())
        {
            anchor=cursor.getLong(cursor.getColumnIndex("anchor"));
        }
        cursor.close();

        return new Date(anchor);
    }

    @Override
    public void setStateAndAnchor(int id, int state, Date anchor) {
        Account account=getAccountById(id);
        if (account!=null){
            account.setState(state);
            account.setAnchor(anchor);
            updateAccount(account);
        }
    }
}
