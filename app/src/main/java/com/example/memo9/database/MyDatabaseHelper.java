package com.example.memo9.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by zxy on 2019/3/3.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {
    //用户表,包含name和pwd两个字段，name为主键
    public static final String CREATE_eventsDB = "create table eventsDB(" +

            "id integer primary key ," +
            "start_minute integer," +
            "start_hour integer," +
            "start_day integer," +
            "start_month integer," +
            "start_year integer," +
            "end_minute integer," +
            "end_hour integer," +
            "end_day integer," +
            "end_month integer," +
            "end_year integer," +
            "name text," +
            "location text," +
            "color text)";


    private Context mContext;

    //构造方法：
    // 第一个参数Context上下文，
    // 第二个参数数据库名，
    // 第三个参数cursor允许我们在查询数据的时候返回一个自定义的光标位置，一般传入的都是null，
    // 第四个参数表示目前库的版本号（用于对库进行升级）
    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory , int version){
        super(context,name ,factory,version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //调用SQLiteDatabase中的execSQL（）执行建表语句。
        db.execSQL(CREATE_eventsDB);
        //创建成功
        Toast.makeText(mContext, "创建成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //更新表
        db.execSQL("drop table if exists eventsDB");
        onCreate(db);
    }
}