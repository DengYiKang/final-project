package com.example.dell.meituan;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
//未完成订单详情界面
public class Out_order_detail_Activity extends AppCompatActivity {
    private MyDatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private List<Out_order_detail> datalist = new ArrayList<>();
    private out_order_detail_adapter adapter;
    private String time;
    private Button decide;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_order_detail_);
        //接受数据
        Intent intent = getIntent();
        time = intent.getStringExtra("timestamp");
        //打开数据库
        dbHelper = new MyDatabaseHelper(this, "MeiTuan.db", null, 4);
        //toolbar
        toolbar = (Toolbar) findViewById(R.id.out_order_detail_toolbar);
        toolbar.setTitle("未完成订单详情");
        setSupportActionBar(toolbar);
        //’确认支付‘的点击事件
        decide = (Button) findViewById(R.id.out_order_detail_decide);
        decide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Operate.insert_fin_order(Out_order_detail_Activity.this, time);
                Operate.delete_out_order(Out_order_detail_Activity.this, time, null);
                datalist.clear();
                adapter.notifyDataSetChanged();
            }
        });
        //获取控件
        recyclerView = (RecyclerView) findViewById(R.id.out_order_detail_recyclerview);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        //读取数据
        read_from_out_order_detail();
        adapter = new out_order_detail_adapter(datalist);
        recyclerView.setAdapter(adapter);
    }

    //从数据库读取数据至datalist
    private void read_from_out_order_detail() {
        datalist.clear();
        String selection = "timestamp = ?";
        String[] selectionArgs = new String[]{time};
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Out_order", null, selection, selectionArgs, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String disname = cursor.getString(cursor.getColumnIndex("disname"));
                String resname = cursor.getString(cursor.getColumnIndex("m_resname"));
                float price = cursor.getFloat(cursor.getColumnIndex("price"));
                int amount = cursor.getInt(cursor.getColumnIndex("amount"));
                String time = cursor.getString(cursor.getColumnIndex("timestamp"));
                Out_order_detail data = new Out_order_detail(disname, resname, price, amount, time);
                datalist.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
