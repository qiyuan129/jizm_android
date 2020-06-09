package dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
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
                Date start=new Date(cursor.getLong(cursor.getColumnIndex("start")));
                Date end=new Date(cursor.getLong(cursor.getColumnIndex("end")));
                double periodic_money=cursor.getDouble(cursor.getColumnIndex("periodic_money"));
                int state=cursor.getInt(cursor.getColumnIndex("state"));
                Date anchor=new Date(cursor.getLong(cursor.getColumnIndex("anchor")));

                periodic=new Periodic(periodic_id,account_id,category_id,user_id,type,periodic_name,cycle,start,end,periodic_money,state,anchor);
                list.add(periodic);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    @Override
    public void insertPeriodic(Periodic periodic) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("account_id",periodic.getAccount_id());
        values.put("category_id",periodic.getCategory_id());
        values.put("user_id",periodic.getUser_id());
        values.put("type",periodic.getType());
        values.put("periodic_name",periodic.getPeriodic_name());
        values.put("cycle",periodic.getCycle());
        values.put("start",periodic.getStart().getTime());
        values.put("end",periodic.getEnd().getTime());
        values.put("periodic_money",periodic.getPeriodic_money());
        values.put("state",periodic.getState());
        values.put("anchor",periodic.getAnchor().getTime());

        db.insert("periodic",null,values);
    }

    @Override
    public void insertPeriodicById(Periodic periodic) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("periodic_id",periodic.getPeriodic_id());
        values.put("account_id",periodic.getAccount_id());
        values.put("category_id",periodic.getCategory_id());
        values.put("user_id",periodic.getUser_id());
        values.put("type",periodic.getType());
        values.put("periodic_name",periodic.getPeriodic_name());
        values.put("cycle",periodic.getCycle());
        values.put("start",periodic.getStart().getTime());
        values.put("end",periodic.getEnd().getTime());
        values.put("periodic_money",periodic.getPeriodic_money());
        values.put("state",periodic.getState());
        values.put("anchor",periodic.getAnchor().getTime());

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
        values.put("start",periodic.getStart().getTime());
        values.put("end",periodic.getEnd().getTime());
        values.put("periodic_money",periodic.getPeriodic_money());
        values.put("state",periodic.getState());
        values.put("anchor",periodic.getAnchor().getTime());

        db.update("periodic",values,"periodic_id = ?",new String[]{""+periodic.getPeriodic_id()});

    }

    @Override
    public void deletePeriodic(int id) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.delete("periodic","periodic_id = ?",new String[]{""+id});

    }

    @Override
    public void deleteAll() {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.execSQL("delete from periodic");
    }

    @Override
    public Periodic getPeriodicById(int id) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Periodic periodic=null;
        Cursor cursor = db.query("periodic",null,"periodic_id = ?",new String[]{""+id},null,null,null);
        if (cursor.moveToFirst())
        {
            int periodic_id=cursor.getInt(cursor.getColumnIndex("periodic_id"));
            int account_id=cursor.getInt(cursor.getColumnIndex("account_id"));
            int category_id=cursor.getInt(cursor.getColumnIndex("category_id"));
            int user_id=cursor.getInt(cursor.getColumnIndex("user_id"));
            int type=cursor.getInt(cursor.getColumnIndex("type"));
            String periodic_name=cursor.getString(cursor.getColumnIndex("periodic_name"));
            int cycle=cursor.getInt(cursor.getColumnIndex("cycle"));
            Date start=new Date(cursor.getLong(cursor.getColumnIndex("start")));
            Date end=new Date(cursor.getLong(cursor.getColumnIndex("end")));
            double periodic_money=cursor.getDouble(cursor.getColumnIndex("periodic_money"));
            int state=cursor.getInt(cursor.getColumnIndex("state"));
            Date anchor=new Date(cursor.getLong(cursor.getColumnIndex("anchor")));

            periodic=new Periodic(periodic_id,account_id,category_id,user_id,type,periodic_name,cycle,start,end,periodic_money,state,anchor);

        }
        cursor.close();
        return periodic;
    }

    @Override
    public List<Periodic> getSyncPeriodic() {
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
                Date start=new Date(cursor.getLong(cursor.getColumnIndex("start")));
                Date end=new Date(cursor.getLong(cursor.getColumnIndex("end")));
                double periodic_money=cursor.getDouble(cursor.getColumnIndex("periodic_money"));
                int state=cursor.getInt(cursor.getColumnIndex("state"));
                Date anchor=new Date(cursor.getLong(cursor.getColumnIndex("anchor")));

                periodic=new Periodic(periodic_id,account_id,category_id,user_id,type,periodic_name,cycle,start,end,periodic_money,state,anchor);
                list.add(periodic);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    @Override
    public Date getMaxAnchor() {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        long anchor=0;
        Cursor cursor = db.query("periodic",null,null,null,null,null,"anchor desc");

        if (cursor.moveToFirst())
        {
            anchor=cursor.getLong(cursor.getColumnIndex("anchor"));
        }
        cursor.close();

        return new Date(anchor);
    }

    @Override
    public void setStateAndAnchor(int id, int state, Date anchor) {
        Periodic periodic=getPeriodicById(id);
        if (periodic!=null){
            periodic.setState(state);
            periodic.setAnchor(anchor);
            updatePeriodic(periodic);
        }
    }

    @Override
    public void setState(int id, int state) {
        Periodic periodic=getPeriodicById(id);
        if (periodic!=null){
            periodic.setState(state);
            updatePeriodic(periodic);
        }
    }
}
