package com.example.memo9.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.library.WeekViewEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by zxy 2019/3/14
 */

public class EditEvents {
    private MyDatabaseHelper dbHelper;  //数据库
    private int id,start_minute,start_hour,start_day,start_month,start_year,end_minute,end_hour,end_day,end_month,end_year;
    private String name,location,color;
    private int getId,getStart_minute,getStart_hour,getStart_day,getStart_month,getStart_year,
                 getEnd_minute,getEnd_hour,getEnd_day,getEnd_month,getEnd_year;
    private String getName,getLocation,getColor;
    private Context context;

    public EditEvents (Context context){
        this.context = context;
    }

    public void addinfo( WeekViewEvent event){
        //第二个参数是数据库名
        dbHelper = new MyDatabaseHelper(context,"eventsDataBase",null,1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id",event.getId());
        values.put("start_minute",event.getStartTime().get(Calendar.MINUTE));
        values.put("start_hour",event.getStartTime().get(Calendar.HOUR_OF_DAY));
        values.put("start_day",event.getStartTime().get(Calendar.DAY_OF_MONTH));
        values.put("start_month",event.getStartTime().get(Calendar.MONTH));
        values.put("start_year",event.getStartTime().get(Calendar.YEAR));
        values.put("end_minute",event.getEndTime().get(Calendar.MINUTE));
        values.put("end_hour",event.getEndTime().get(Calendar.HOUR_OF_DAY));
        values.put("end_day",event.getEndTime().get(Calendar.DAY_OF_MONTH));
        values.put("end_month",event.getEndTime().get(Calendar.MONTH));
        values.put("end_year",event.getEndTime().get(Calendar.YEAR));
        values.put("name", event.getName());
        values.put("location", event.getLocation());
        values.put("color",event.getColor());
        //insert（）方法中第一个参数是表名，第二个参数是表示给表中未指定数据的自动赋值为NULL。第三个参数是一个ContentValues对象
        db.insert("eventsDB",null,values);;
    }

    //数据库查询函数：
    public List<WeekViewEvent> queryinfo1(){
        final List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();/*在数组中存放数据*/
        //第二个参数是数据库名
        dbHelper = new MyDatabaseHelper(context,"eventsDataBase",null,1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from eventsDB", null);
        if (cursor != null && cursor.getCount() > 0) {
            while(cursor.moveToNext()) {
                getId = cursor.getInt(0);
                getStart_minute = cursor.getInt(1);
                getStart_hour = cursor.getInt(2);
                getStart_day = cursor.getInt(3);
                getStart_month = cursor.getInt(4);
                getStart_year = cursor.getInt(5);
                getEnd_minute = cursor.getInt(6);
                getEnd_hour = cursor.getInt(7);
                getEnd_day = cursor.getInt(8);
                getEnd_month = cursor.getInt(9);
                getEnd_year = cursor.getInt(10);
                getName = cursor.getString(11);
                getLocation = cursor.getString(12);
                getColor = cursor.getString(13);

                Calendar startTime = Calendar.getInstance();
                startTime.set(Calendar.MINUTE, getStart_minute);
                startTime.set(Calendar.HOUR_OF_DAY, getStart_hour);
                startTime.set(Calendar.DAY_OF_MONTH,getStart_day);
                startTime.set(Calendar.MONTH, getStart_month);
                startTime.set(Calendar.YEAR, getStart_year);
                Calendar endTime = (Calendar) startTime.clone();
                endTime.set(Calendar.MINUTE,getEnd_minute);
                endTime.set(Calendar.HOUR_OF_DAY, getEnd_hour);
                endTime.set(Calendar.DAY_OF_MONTH, getEnd_day);
                endTime.set(Calendar.MONTH,  getEnd_month);
                endTime.set(Calendar.YEAR, getEnd_year);
                WeekViewEvent event = new WeekViewEvent(getId, getName , getLocation , startTime, endTime);
                events.add(event);
            }
            cursor.close();
            db.close();
            return events;
        }
        cursor.close();
        db.close();
        return new ArrayList<>();
    }

    public List<WeekViewEvent> queryEventByMonth(int year, int month) {
        final List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();/*在数组中存放数据*/
        //第二个参数是数据库名
        dbHelper = new MyDatabaseHelper(context,"eventsDataBase",null,1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from eventsDB where start_year=? and start_month=?", new String[]{String.valueOf(year),String.valueOf(month)});
        if (cursor != null && cursor.getCount() > 0) {
            while(cursor.moveToNext()) {
                getId = cursor.getInt(0);
                getStart_minute = cursor.getInt(1);
                getStart_hour = cursor.getInt(2);
                getStart_day = cursor.getInt(3);
                getStart_month = cursor.getInt(4);
                getStart_year = cursor.getInt(5);
                getEnd_minute = cursor.getInt(6);
                getEnd_hour = cursor.getInt(7);
                getEnd_day = cursor.getInt(8);
                getEnd_month = cursor.getInt(9);
                getEnd_year = cursor.getInt(10);
                getName = cursor.getString(11);
                getLocation = cursor.getString(12);
                getColor = cursor.getString(13);

                Calendar startTime = Calendar.getInstance();
                startTime.set(Calendar.MINUTE, getStart_minute);
                startTime.set(Calendar.HOUR_OF_DAY, getStart_hour);
                startTime.set(Calendar.DAY_OF_MONTH,getStart_day);
                startTime.set(Calendar.MONTH, getStart_month-1);
                startTime.set(Calendar.YEAR, getStart_year);
                Calendar endTime = (Calendar) startTime.clone();
                endTime.set(Calendar.MINUTE,getEnd_minute);
                endTime.set(Calendar.HOUR_OF_DAY, getEnd_hour);
                endTime.set(Calendar.DAY_OF_MONTH, getEnd_day);
                endTime.set(Calendar.MONTH,  getEnd_month-1);
                endTime.set(Calendar.YEAR, getEnd_year);
                WeekViewEvent event = new WeekViewEvent(getId, getName , getLocation , startTime, endTime);
                events.add(event);
            }
        }
        cursor.close();
        db.close();
        return events;
    }

    public void delete(String id){
        //删除数据库
        dbHelper = new MyDatabaseHelper(context,"eventsDataBase",null,1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("eventsDB","id=?",new String[] {id});
        db.close();
    }

    public WeekViewEvent queryById(String id){
        WeekViewEvent event;
        dbHelper = new MyDatabaseHelper(context,"eventsDataBase",null,1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from eventsDB where id=? ", new String[]{String.valueOf(id)});
        if (cursor != null && cursor.getCount() > 0) {
            while(cursor.moveToNext()) {
                getId = cursor.getInt(0);
                getStart_minute = cursor.getInt(1);
                getStart_hour = cursor.getInt(2);
                getStart_day = cursor.getInt(3);
                getStart_month = cursor.getInt(4);
                getStart_year = cursor.getInt(5);
                getEnd_minute = cursor.getInt(6);
                getEnd_hour = cursor.getInt(7);
                getEnd_day = cursor.getInt(8);
                getEnd_month = cursor.getInt(9);
                getEnd_year = cursor.getInt(10);
                getName = cursor.getString(11);
                getLocation = cursor.getString(12);
                getColor = cursor.getString(13);

                Calendar startTime = Calendar.getInstance();
                startTime.set(Calendar.MINUTE, getStart_minute);
                startTime.set(Calendar.HOUR_OF_DAY, getStart_hour);
                startTime.set(Calendar.DAY_OF_MONTH,getStart_day);
                startTime.set(Calendar.MONTH, getStart_month-1);
                startTime.set(Calendar.YEAR, getStart_year);
                Calendar endTime = (Calendar) startTime.clone();
                endTime.set(Calendar.MINUTE,getEnd_minute);
                endTime.set(Calendar.HOUR_OF_DAY, getEnd_hour);
                endTime.set(Calendar.DAY_OF_MONTH, getEnd_day);
                endTime.set(Calendar.MONTH,  getEnd_month-1);
                endTime.set(Calendar.YEAR, getEnd_year);
                event = new WeekViewEvent(getId, getName , getLocation , startTime, endTime);
                return event;
            }
        }
        cursor.close();
        db.close();
        return null;
    }

    public void update(WeekViewEvent event){
        dbHelper = new MyDatabaseHelper(context,"eventsDataBase",null,1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("start_minute",event.getStartTime().get(Calendar.MINUTE));
        values.put("start_hour",event.getStartTime().get(Calendar.HOUR_OF_DAY));
        values.put("start_day",event.getStartTime().get(Calendar.DAY_OF_MONTH));
        values.put("start_month",event.getStartTime().get(Calendar.MONTH));
        values.put("start_year",event.getStartTime().get(Calendar.YEAR));
        values.put("end_minute",event.getEndTime().get(Calendar.MINUTE));
        values.put("end_hour",event.getEndTime().get(Calendar.HOUR_OF_DAY));
        values.put("end_day",event.getEndTime().get(Calendar.DAY_OF_MONTH));
        values.put("end_month",event.getEndTime().get(Calendar.MONTH));
        values.put("end_year",event.getEndTime().get(Calendar.YEAR));
        values.put("name", event.getName());
        values.put("location", event.getLocation());
        values.put("color",event.getColor());
        db.update("eventsDB", values, "id=?", new String[]{Long.toString(event.getId())});
        db.close();
    }
}
