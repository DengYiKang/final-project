package com.example.dell.meituan;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context mContext;
    //饭店
    public static final String CREATE_RES = "create table Res("
            + "id int identity(1,1),"
            + "resname text primary key,"
            + "address text,"
            + "phone text)";

    //菜品
    public static final String CREATE_DIS = "create table Dis("
            + "id integer primary key,"
            + "disname text,"
            + "m_resname text,"
            + "price real,"
            + "amount int default 0 ,"
            + "unique(disname, m_resname),"
            + "foreign key(m_resname) references Res(resname) on delete cascade on update cascade)";

    //未完成订单
    public static final String CREATE_OUT_ORDER = "create table Out_order("
            +"id integer primary key,"
            +"disname text,"
            +"m_resname text,"
            +"price real,"
            +"amount int,"
            +"timestamp not null default (datetime('now', 'localtime')),"
            +"foreign key(m_resname) references Res(resname) on delete cascade on update cascade)";

    //已完成订单
    public static final String CREATE_FIN_ORDER = "create table Fin_order("
            +"id integer primary key,"
            +"disname text,"
            +"m_resname text,"
            +"price real,"
            +"amount int,"
            +"timestamp not null default (datetime('now', 'localtime')),"
            +"foreign key(m_resname) references Res(resname) on delete cascade on update cascade)";

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_RES);
        db.execSQL(CREATE_DIS);
        db.execSQL(CREATE_OUT_ORDER);
        db.execSQL(CREATE_FIN_ORDER);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists Res");
        db.execSQL("drop table if exists Dis");
        db.execSQL("drop table if exists Out_order");
        db.execSQL("drop table if exists Fin_order");
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("pragma foreign_keys = ON;");
    }
}
