package dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import pojo.Bill;
import pojo.Category;

import static com.myapplication.MainActivity.dbHelper;

public class CategoryDAOImpl implements CategoryDAO {
    @Override
    public List<Category> listCategory() {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Category category=null;
        List<Category> list=new ArrayList<>();

        Cursor cursor = db.query("category",null,null,null,null,null,null);
        if (cursor.moveToFirst())
        {
            do {
                int category_id=cursor.getInt(cursor.getColumnIndex("category_id"));
                int user_id=cursor.getInt(cursor.getColumnIndex("user_id"));
                String category_name=cursor.getString(cursor.getColumnIndex("category_name"));
                int type=cursor.getInt(cursor.getColumnIndex("type"));
                int state=cursor.getInt(cursor.getColumnIndex("state"));
                long anchor=cursor.getLong(cursor.getColumnIndex("anchor"));

                category=new Category(category_id,user_id,category_name,type,state,anchor);
                list.add(category);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    @Override
    public void insertCategory(Category category) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("user_id",category.getUser_id());
        values.put("category_name",category.getCategory_name());
        values.put("type",category.getType());
        values.put("state",category.getState());
        values.put("anchor",category.getAnchor());

        db.insert("category",null,values);
    }

    @Override
    public void updateCategory(Category category) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id",category.getUser_id());
        values.put("category_name",category.getCategory_name());
        values.put("type",category.getType());
        values.put("state",category.getState());
        values.put("anchor",category.getAnchor());

        db.update("category",values,"category_id = ?",new String[]{""+category.getCategory_id()});

    }

    @Override
    public void deleteCategory(int id) {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.delete("category","category_id = ?",new String[]{""+id});
    }
}
