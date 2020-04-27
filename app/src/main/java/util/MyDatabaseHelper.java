package util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_CATEGORY="create table category("
            +"category_id integer primary key autoincrement,"
            +"user_id integer,"
            +"category_name text,"
            +"type integer,"
            +"state integer,"
            +"anchor timestamp)";

    public static final String CREATE_ACCOUNT="create table account("
            +"account_id integer primary key autoincrement,"
            +"user_id integer,"
            +"account_name text,"
            +"money double,"
            +"state integer,"
            +"anchor timestamp)";

    public static final String CREATE_BILL="create table bill("
            +"bill_id integer primary key autoincrement,"
            +"account_id integer,"
            +"category_id integer,"
            +"user_id integer,"
            +"type integer,"
            +"bill_name text,"
            +"bill_date timestamp,"
            +"bill_money double,"
            +"state integer,"
            +"anchor timestamp)";

    public static final String CREATE_PERIODIC="create table periodic("
            +"periodic_id integer primary key autoincrement,"
            +"account_id integer,"
            +"category_id integer,"
            +"user_id integer,"
            +"type integer,"
            +"periodic_name text,"
            +"cycle integer,"
            +"start date,"
            +"end date,"
            +"periodic_money double,"
            +"state integer,"
            +"anchor timestamp)";

    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context,name,factory,version);
        mContext=context;
    }

    @Override
    public  void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_CATEGORY);
        db.execSQL(CREATE_ACCOUNT);
        db.execSQL(CREATE_BILL);
        db.execSQL(CREATE_PERIODIC);
        Toast.makeText(mContext,"创建成功",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void  onUpgrade(SQLiteDatabase db,int oldversion,int newversion)
    {
        db.execSQL("drop table if exists category");
        db.execSQL("drop table if exists account");
        db.execSQL("drop table if exists bill");
        db.execSQL("drop table if exists periodic");
        onCreate(db);
    }

}
