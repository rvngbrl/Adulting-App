package com.rvngbrl.adulting.TaskTracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.Date;

public class MyDatabaseHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "ToDoDBHelper.db";
    public static final String TABLE_NAME = "task_mngr";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

        db.execSQL(
                "CREATE TABLE " + TABLE_NAME + "(id INTEGER PRIMARY KEY, task TEXT, task_at DATETIME,task_time DATETIME, status INTEGER)"
        );
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public boolean insertTask(String task, String task_at, String task_time) {
        Date date;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("task", task);
        contentValues.put("task_at", task_at);
        contentValues.put("task_time", task_time);
        //contentValues.put("dateStr", getDate(dateStr));
        contentValues.put("status", 0);
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean updateTask(String id, String task, String task_at,  String task_time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("task", task);
        contentValues.put("task_at", task_at);
        contentValues.put("task_time", task_time);

        db.update(TABLE_NAME, contentValues, "id = ? ", new String[]{id});
        return true;
    }

    public boolean deleteTask(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "id = ? ", new String[]{id});
        return true;
    }

    public boolean updateTaskStatus(String id, Integer status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("status", status);

        db.update(TABLE_NAME, contentValues, "id = ? ", new String[]{id});
        return true;
    }


    public Cursor getSingleTask(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " WHERE id = '" + id + "' order by id desc", null);
        return res;

    }

    public Cursor getTodayTask() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " WHERE date(task_at) = date('now', 'localtime') order by id desc", null);
        return res;

    }


    public Cursor getTomorrowTask() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " WHERE date(task_at) = date('now', '+1 day', 'localtime')  order by id desc", null);
        return res;

    }


    public Cursor getUpcomingTask() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " WHERE date(task_at) > date('now', '+1 day', 'localtime') order by id desc", null);
        return res;

    }

}
