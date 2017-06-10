package com.example.dell.meituan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.StringBuilderPrinter;
import android.widget.Toast;

//增添查改的方法汇总
public class Operate {
    private static MyDatabaseHelper dbHelper;

    //插入Dis表
    public static void update_dis(Context context, String name, String address, int amount) {
        dbHelper = new MyDatabaseHelper(context, "MeiTuan.db", null, 4);
        String where = null;
        String[] selectionArgs;
        if (name != null) {
            where = "disname = ? and m_resname = ?";
            selectionArgs = new String[]{name, address};
        } else {
            where = "m_resname = ?";
            selectionArgs = new String[]{address};
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("amount", amount);
        db.update("Dis", values, where, selectionArgs);
    }

    //更新Res表
    public static void update_res(Context context, String name, String object, String object_values) {
        dbHelper = new MyDatabaseHelper(context, "MeiTuan.db", null, 4);
        String where = "resname = ?";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(object, object_values);
        db.update("Res", values, where, new String[]{name});
    }

    //删除Res数据
    public static void delete_res(Context context, String name) {
        dbHelper = new MyDatabaseHelper(context, "MeiTuan.db", null, 4);
        String where = "resname = ?";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("Res", where, new String[]{name});
    }

    //删除Out_order数据
    public static void delete_out_order(Context context, String time, String disname) {
        dbHelper = new MyDatabaseHelper(context, "MeiTuan.db", null, 4);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String where = null;
        String[] selectionArgs = null;
        if (time == null && disname == null) {
            db.delete("Out_order", null, null);
        } else {
            if (disname == null) {
                where = "timestamp = ?";
                selectionArgs = new String[]{time};
            } else {
                where = "timestamp = ? and disname = ?";
                selectionArgs = new String[]{time, disname};
            }
            db = dbHelper.getWritableDatabase();
            db.delete("Out_order", where, selectionArgs);
        }
    }

    //插入数据至Res中
    public static void insert_res(Context context, String name, String address, String phone) {
        dbHelper = new MyDatabaseHelper(context, "MeiTuan.db", null, 4);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("insert into Res(resname, address, phone) values(?, ?, ?)", new String[]{name, address, phone});
    }

    //插入数据至Dis
    public static void insert_dis(Context context, String disname, String m_resname, float price, int amount) {
        dbHelper = new MyDatabaseHelper(context, "MeiTuan.db", null, 4);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("insert into Dis(disname, m_resname, price, amount) values(?, ?, ?, ?)",
                new String[]{disname, m_resname, String.valueOf(price), String.valueOf(amount)});
    }

    //插入数据至Out_order
    public static void insert_out_order(Context context, String m_resname) {
        dbHelper = new MyDatabaseHelper(context, "MeiTuan.db", null, 4);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("insert into Out_order(disname, m_resname, price, amount)"
                + " select disname, m_resname, price, amount from Dis "
                + "where m_resname = ? and amount > 0", new String[]{m_resname});
    }

    //插入数据至Fin_order
    public static void insert_fin_order(Context context, String time) {
        dbHelper = new MyDatabaseHelper(context, "MeiTuan.db", null, 4);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (time == null) {
            db.execSQL("insert into Fin_order(disname, m_resname, price, amount, timestamp) "
                    + "select disname, m_resname, price, amount, timestamp from Out_order ");
        } else {
            db.execSQL("insert into Fin_order(disname, m_resname, price, amount, timestamp) "
                    + "select disname, m_resname, price, amount, timestamp from Out_order "
                    + "where timestamp = ? ", new String[]{time});
        }
    }

    //计算总费用
    public static float getPrices(Context context, String table, String m_resname, String time) {
        float prices = 0;
        float price;
        int amount;
        dbHelper = new MyDatabaseHelper(context, "MeiTuan.db", null, 4);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection;
        String[] selectionArgs;
        if (m_resname == null) {
            selection = "timestamp = ?";
            selectionArgs = new String[]{time};
        } else {
            selection = "m_resname = ? and timestamp = ?";
            selectionArgs = new String[]{m_resname, time};
        }
        Cursor cursor = db.query(table, null, selection, selectionArgs, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                amount = cursor.getInt(cursor.getColumnIndex("amount"));
                price = cursor.getFloat(cursor.getColumnIndex("price"));
                prices += amount * price;
            } while (cursor.moveToNext());
        }
        return prices;
    }

}
