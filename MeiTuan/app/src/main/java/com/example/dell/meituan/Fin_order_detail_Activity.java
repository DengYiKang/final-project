package com.example.dell.meituan;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;
//已完成订单详情
public class Fin_order_detail_Activity extends AppCompatActivity {
    private List<Fin_order_detail> datalist = new ArrayList<>();
    private RecyclerView recyclerView;
    private fin_order_detail_adapter adapter;
    private String time;
    private MyDatabaseHelper dbHelper;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fin_order_detail_);
        //接受数据
        Intent intent = getIntent();
        time = intent.getStringExtra("timestamp");
        //打开数据库
        dbHelper = new MyDatabaseHelper(this, "MeiTuan.db", null, 4);
        //toolbar
        toolbar = (Toolbar) findViewById(R.id.fin_order_toolbar);
        toolbar.setTitle("已完成订单详情");
        setSupportActionBar(toolbar);
        //获取控件
        recyclerView = (RecyclerView) findViewById(R.id.activity_fin_order_detail_recyclerview);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        //读取信息
        read_from_fin_order_detail();
        //adapter
        adapter = new fin_order_detail_adapter(datalist);
        recyclerView.setAdapter(adapter);
    }

    //从数据库读取信息至datalist
    private void read_from_fin_order_detail() {
        datalist.clear();
        String selection = "timestamp = ?";
        String[] selectionArgs = new String[]{time};
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Fin_order", null, selection, selectionArgs, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String disname = cursor.getString(cursor.getColumnIndex("disname"));
                String resname = cursor.getString(cursor.getColumnIndex("m_resname"));
                float price = cursor.getFloat(cursor.getColumnIndex("price"));
                int amount = cursor.getInt(cursor.getColumnIndex("amount"));
                String time = cursor.getString(cursor.getColumnIndex("timestamp"));
                Fin_order_detail data = new Fin_order_detail(disname, resname, price, amount, time);
                datalist.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
