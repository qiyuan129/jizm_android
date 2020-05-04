package dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pojo.Account;
import pojo.Bill;
import pojo.Category;

import static com.myapplication.MainActivity.dbHelper;

public class BillDAOImpl implements BillDAO {
    @Override
    public void insertBill(Bill bill) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("account_id",bill.getAccount_id());
        values.put("category_id",bill.getCategory_id());
        values.put("user_id",bill.getUser_id());
        values.put("type",bill.getType());
        values.put("bill_name",bill.getBill_name());
        values.put("bill_date",bill.getBill_date());
        values.put("bill_money",bill.getBill_money());
        values.put("state",bill.getState());
        values.put("anchor",bill.getAnchor());

        db.insert("bill",null,values);
    }

    @Override
    public List<Bill> listBill() {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Bill bill=null;
        List<Bill> list=new ArrayList<>();

        Cursor cursor = db.query("bill",null,null,null,null,null,null);
        if (cursor.moveToFirst())
        {
            do {
                int bill_id=cursor.getInt(cursor.getColumnIndex("bill_id"));
                int account_id=cursor.getInt(cursor.getColumnIndex("account_id"));
                int category_id=cursor.getInt(cursor.getColumnIndex("category_id"));
                int user_id=cursor.getInt(cursor.getColumnIndex("user_id"));
                int type=cursor.getInt(cursor.getColumnIndex("type"));
                String bill_name=cursor.getString(cursor.getColumnIndex("bill_name"));
                long bill_date=cursor.getLong(cursor.getColumnIndex("bill_date"));
                double bill_money=cursor.getDouble(cursor.getColumnIndex("bill_money"));
                int state=cursor.getInt(cursor.getColumnIndex("state"));
                long anchor=cursor.getLong(cursor.getColumnIndex("anchor"));

                bill=new Bill(bill_id,account_id,category_id,user_id,type,bill_name,bill_date,bill_money,state,anchor);
                list.add(bill);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    @Override
    public void updateBill(Bill bill) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("account_id",bill.getAccount_id());
        values.put("category_id",bill.getCategory_id());
        values.put("user_id",bill.getUser_id());
        values.put("type",bill.getType());
        values.put("bill_name",bill.getBill_name());
        values.put("bill_date",bill.getBill_date());
        values.put("bill_money",bill.getBill_money());
        values.put("state",bill.getState());
        values.put("anchor",bill.getAnchor());

        db.update("bill",values,"bill_id = ?",new String[]{""+bill.getBill_id()});
    }

    @Override
    public void deleteBill(int id) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.delete("bill","bill_id = ?",new String[]{""+id});

    }

    @Override
    public List<Double> monthlyIncome(int year) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        double m0=0,m1=0,m2=0,m3=0,m4=0,m5=0,m6=0,m7=0,m8=0,m9=0,m10=0,m11=0;
        List<Double> list=new ArrayList<>();
        Date date1= new Date(year-1900, 0, 1);
        Date date2= new Date(year-1900+1, 0, 1);
        Date date;

        Cursor cursor = db.query("bill",null,"bill_date >= ? and bill_date < ? and type = ?",new String[]{""+date1.getTime(),""+date2.getTime(),"1"},null,null,null);
        if (cursor.moveToFirst())
        {
            do {
                long bill_date=cursor.getLong(cursor.getColumnIndex("bill_date"));
                double bill_money=cursor.getDouble(cursor.getColumnIndex("bill_money"));

                date=new Date(bill_date);
                switch (date.getMonth()){
                    case 0:
                        m0+=bill_money;
                        break;
                    case 1:
                        m1+=bill_money;
                        break;
                    case 2:
                        m2+=bill_money;
                        break;
                    case 3:
                        m3+=bill_money;
                        break;
                    case 4:
                        m4+=bill_money;
                        break;
                    case 5:
                        m5+=bill_money;
                        break;
                    case 6:
                        m6+=bill_money;
                        break;
                    case 7:
                        m7+=bill_money;
                        break;
                    case 8:
                        m8+=bill_money;
                        break;
                    case 9:
                        m9+=bill_money;
                        break;
                    case 10:
                        m10+=bill_money;
                        break;
                    case 11:
                        m11+=bill_money;
                        break;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        list.add(m0);
        list.add(m1);
        list.add(m2);
        list.add(m3);
        list.add(m4);
        list.add(m5);
        list.add(m6);
        list.add(m7);
        list.add(m8);
        list.add(m9);
        list.add(m10);
        list.add(m11);

        return list;
    }

    @Override
    public List<Double> monthlyOutcome(int year) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        double m0=0,m1=0,m2=0,m3=0,m4=0,m5=0,m6=0,m7=0,m8=0,m9=0,m10=0,m11=0;
        List<Double> list=new ArrayList<>();
        Date date1= new Date(year-1900, 0, 1);
        Date date2= new Date(year-1900+1, 0, 1);
        Date date;

        Cursor cursor = db.query("bill",null,"bill_date >= ? and bill_date < ? and type = ?",new String[]{""+date1.getTime(),""+date2.getTime(),"0"},null,null,null);
        if (cursor.moveToFirst())
        {
            do {
                long bill_date=cursor.getLong(cursor.getColumnIndex("bill_date"));
                double bill_money=cursor.getDouble(cursor.getColumnIndex("bill_money"));

                date=new Date(bill_date);
                switch (date.getMonth()){
                    case 0:
                        m0+=bill_money;
                        break;
                    case 1:
                        m1+=bill_money;
                        break;
                    case 2:
                        m2+=bill_money;
                        break;
                    case 3:
                        m3+=bill_money;
                        break;
                    case 4:
                        m4+=bill_money;
                        break;
                    case 5:
                        m5+=bill_money;
                        break;
                    case 6:
                        m6+=bill_money;
                        break;
                    case 7:
                        m7+=bill_money;
                        break;
                    case 8:
                        m8+=bill_money;
                        break;
                    case 9:
                        m9+=bill_money;
                        break;
                    case 10:
                        m10+=bill_money;
                        break;
                    case 11:
                        m11+=bill_money;
                        break;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        list.add(m0);
        list.add(m1);
        list.add(m2);
        list.add(m3);
        list.add(m4);
        list.add(m5);
        list.add(m6);
        list.add(m7);
        list.add(m8);
        list.add(m9);
        list.add(m10);
        list.add(m11);

        return list;
    }

    @Override
    public List<Bill> top10(Date begin, Date end, int type1) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Bill bill=null;
        List<Bill> list=new ArrayList<>();
        Cursor cursor = db.query("bill",null,"bill_date >= ? and bill_date < ? and type = ?",new String[]{""+begin.getTime(),""+end.getTime(),""+type1},null,null,"bill_money desc");
        int i=0;

        if (cursor.moveToFirst())
        {
            do {
                int bill_id=cursor.getInt(cursor.getColumnIndex("bill_id"));
                int account_id=cursor.getInt(cursor.getColumnIndex("account_id"));
                int category_id=cursor.getInt(cursor.getColumnIndex("category_id"));
                int user_id=cursor.getInt(cursor.getColumnIndex("user_id"));
                int type=cursor.getInt(cursor.getColumnIndex("type"));
                String bill_name=cursor.getString(cursor.getColumnIndex("bill_name"));
                long bill_date=cursor.getLong(cursor.getColumnIndex("bill_date"));
                double bill_money=cursor.getDouble(cursor.getColumnIndex("bill_money"));
                int state=cursor.getInt(cursor.getColumnIndex("state"));
                long anchor=cursor.getLong(cursor.getColumnIndex("anchor"));

                bill=new Bill(bill_id,account_id,category_id,user_id,type,bill_name,bill_date,bill_money,state,anchor);
                list.add(bill);
                i++;
            } while (cursor.moveToNext() && i<10);
        }
        cursor.close();
        return list;
    }

    @Override
    public Map<Integer, Double> getCategoryChartData(Date begin, Date end, int type) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Bill bill=null;
        CategoryDAO categoryDAO=new CategoryDAOImpl();
        List<Category> categoryList=categoryDAO.listCategory();
        List<Integer> categoryIdList=new ArrayList<>();
        Map<Integer, Double> map = new HashMap<Integer,Double>();
        for (Category category : categoryList){
            categoryIdList.add(category.getCategory_id());
            map.put(category.getCategory_id(),0.0);
        }
        Cursor cursor = db.query("bill",null,"bill_date >= ? and bill_date < ? and type = ?",new String[]{""+begin.getTime(),""+end.getTime(),""+type},null,null,"bill_money desc");
        if (cursor.moveToFirst())
        {
            do {
                int category_id=cursor.getInt(cursor.getColumnIndex("category_id"));
                double bill_money=cursor.getDouble(cursor.getColumnIndex("bill_money"));
                double sum=map.get(category_id);
                sum+=bill_money;
                map.put(category_id,sum);

//                int user_id=cursor.getInt(cursor.getColumnIndex("user_id"));
//                int type=cursor.getInt(cursor.getColumnIndex("type"));
//                String bill_name=cursor.getString(cursor.getColumnIndex("bill_name"));
//                long bill_date=cursor.getLong(cursor.getColumnIndex("bill_date"));
//                int state=cursor.getInt(cursor.getColumnIndex("state"));
//                long anchor=cursor.getLong(cursor.getColumnIndex("anchor"));
//
//                bill=new Bill(bill_id,account_id,category_id,user_id,type,bill_name,bill_date,bill_money,state,anchor);
//                list.add(bill);
//                i++;
            } while (cursor.moveToNext());
        }
        cursor.close();



        return map;
    }
}
