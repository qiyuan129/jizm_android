package dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
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
                long anchor=cursor.getLong(cursor.getColumnIndex("anchor"));
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
        values.put("anchor",account.getAnchor());
        db.insert("account",null,values);


    }



}
