package com.example.dell.meituan;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class Fin_order_totalActivity extends AppCompatActivity {
    private MyDatabaseHelper dbHelper;
    private EmptyRecyclerView recyclerView;
    private List<Fin_order> dataList = new ArrayList<>();
    private fin_order_adapter adapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fin_order_total);
        //打开数据库
        dbHelper = new MyDatabaseHelper(this, "MeiTuan.db", null, 4);
        toolbar = (Toolbar) findViewById(R.id.fin_order_total_toolbar);
        //toolbar
        toolbar.setTitle("已完成订单");
        setSupportActionBar(toolbar);
        //获取控件
        recyclerView = (EmptyRecyclerView) findViewById(R.id.fin_order_recyclerview);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        //读取数据
        read_from_fin_order();
        //adapter
        adapter = new fin_order_adapter(dataList);
        recyclerView.setAdapter(adapter);
    }

    //从数据库里读取数据到datalist
    private void read_from_fin_order() {
        dataList.clear();
        String groupBy = "timestamp";
        String orderBy = "timestamp";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Fin_order", null, null, null, groupBy, null, orderBy, null);
        if (cursor.moveToFirst()) {
            do {
                String resname = cursor.getString(cursor.getColumnIndex("m_resname"));
                String time = cursor.getString(cursor.getColumnIndex("timestamp"));
                float prices = Operate.getPrices(this, "Fin_order", null, time);
                Fin_order data = new Fin_order(resname, time, prices);
                dataList.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
