package dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import pojo.Bill;
import pojo.Periodic;

import static com.myapplication.MainActivity.dbHelper;

public class PeriodicDAOImpl implements PeriodicDAO {

    @Override
    public List<Periodic> listPeriodic() {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Periodic periodic=null;
        List<Periodic> list=new ArrayList<>();

        Cursor cursor = db.query("periodic",null,null,null,null,null,null);
        if (cursor.moveToFirst())
        {
            do {
                int periodic_id=cursor.getInt(cursor.getColumnIndex("periodic_id"));
                int account_id=cursor.getInt(cursor.getColumnIndex("account_id"));
                int category_id=cursor.getInt(cursor.getColumnIndex("category_id"));
                int user_id=cursor.getInt(cursor.getColumnIndex("user_id"));
                int type=cursor.getInt(cursor.getColumnIndex("type"));
                String periodic_name=cursor.getString(cursor.getColumnIndex("periodic_name"));
                int cycle=cursor.getInt(cursor.getColumnIndex("cycle"));
                long start=cursor.getLong(cursor.getColumnIndex("start"));
                long end=cursor.getLong(cursor.getColumnIndex("end"));
                double periodic_money=cursor.getDouble(cursor.getColumnIndex("periodic_money"));
                int state=cursor.getInt(cursor.getColumnIndex("state"));
                long anchor=cursor.getLong(cursor.getColumnIndex("anchor"));

                periodic=new Periodic(periodic_id,account_id,category_id,user_id,type,periodic_name,cycle,start,end,periodic_money,state,anchor);
                list.add(periodic);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    @Override
    public void addPeriodic(Periodic periodic) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("account_id",periodic.getAccount_id());
        values.put("category_id",periodic.getCategory_id());
        values.put("user_id",periodic.getUser_id());
        values.put("type",periodic.getType());
        values.put("periodic_name",periodic.getPeriodic_name());
        values.put("cycle",periodic.getCycle());
        values.put("start",periodic.getStart());
        values.put("end",periodic.getEnd());
        values.put("periodic_money",periodic.getPeriodic_money());
        values.put("state",periodic.getState());
        values.put("anchor",periodic.getAnchor());

        db.insert("periodic",null,values);
    }

    @Override
    public void updatePeriodic(Periodic periodic) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("account_id",periodic.getAccount_id());
        values.put("category_id",periodic.getCategory_id());
        values.put("user_id",periodic.getUser_id());
        values.put("type",periodic.getType());
        values.put("periodic_name",periodic.getPeriodic_name());
        values.put("cycle",periodic.getCycle());
        values.put("start",periodic.getStart());
        values.put("end",periodic.getEnd());
        values.put("periodic_money",periodic.getPeriodic_money());
        values.put("state",periodic.getState());
        values.put("anchor",periodic.getAnchor());

        db.update("periodic",values,"periodic_id = ?",new String[]{""+periodic.getPeriodic_id()});

    }

    @Override
    public void deletePeriodic(int id) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.delete("periodic","periodic_id = ?",new String[]{""+id});

    }

    @Override
    public List<Periodic> getAyncPeriodic() {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Periodic periodic=null;
        List<Periodic> list=new ArrayList<>();

        Cursor cursor = db.query("periodic",null,"state != ?",new String[]{"9"},null,null,null);
        if (cursor.moveToFirst())
        {
            do {
                int periodic_id=cursor.getInt(cursor.getColumnIndex("periodic_id"));
                int account_id=cursor.getInt(cursor.getColumnIndex("account_id"));
                int category_id=cursor.getInt(cursor.getColumnIndex("category_id"));
                int user_id=cursor.getInt(cursor.getColumnIndex("user_id"));
                int type=cursor.getInt(cursor.getColumnIndex("type"));
                String periodic_name=cursor.getString(cursor.getColumnIndex("periodic_name"));
                int cycle=cursor.getInt(cursor.getColumnIndex("cycle"));
                long start=cursor.getLong(cursor.getColumnIndex("start"));
                long end=cursor.getLong(cursor.getColumnIndex("end"));
                double periodic_money=cursor.getDouble(cursor.getColumnIndex("periodic_money"));
                int state=cursor.getInt(cursor.getColumnIndex("state"));
                long anchor=cursor.getLong(cursor.getColumnIndex("anchor"));

                periodic=new Periodic(periodic_id,account_id,category_id,user_id,type,periodic_name,cycle,start,end,periodic_money,state,anchor);
                list.add(periodic);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    @Override
    public Long getMaxAnchor() {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        long anchor=0;
        Cursor cursor = db.query("periodic",null,null,null,null,null,"anchor desc");

        if (cursor.moveToFirst())
        {
            anchor=cursor.getLong(cursor.getColumnIndex("anchor"));
        }
        cursor.close();

        return anchor;
    }
}
