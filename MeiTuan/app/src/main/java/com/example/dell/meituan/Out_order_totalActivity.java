package com.example.dell.meituan;

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
//未完成订单页面
public class Out_order_totalActivity extends AppCompatActivity {
    private MyDatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private Button decide;
    private List<Out_order> dataList = new ArrayList<>();
    private out_order_adapter adapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_order_information);
        //打开数据库
        dbHelper = new MyDatabaseHelper(this, "MeiTuan.db", null, 4);
        //获取控件
        recyclerView = (RecyclerView) findViewById(R.id.out_order_recyclerview);
        decide = (Button) findViewById(R.id.out_order_all_decide);
        //toolbar
        toolbar = (Toolbar) findViewById(R.id.activity_out_order_information_toolbar);
        toolbar.setTitle("未完成订单");
        setSupportActionBar(toolbar);
        //’确认支付‘
        decide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Operate.insert_fin_order(Out_order_totalActivity.this, null);
                Operate.delete_out_order(Out_order_totalActivity.this, null, null);
                dataList.clear();
                adapter.notifyDataSetChanged();
            }
        });
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        //读取数据
        read_from_out_order();
        adapter = new out_order_adapter(dataList);
        recyclerView.setAdapter(adapter);
    }

    //确保每次更新
    @Override
    protected void onResume() {
        super.onResume();
        read_from_out_order();
        adapter = new out_order_adapter(dataList);
        recyclerView.setAdapter(adapter);
    }

    //从数据库中读取数据至datalist
    private void read_from_out_order() {
        dataList.clear();
        String groupBy = "timestamp";
        String orderBy = "timestamp";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Out_order", null, null, null, groupBy, null, orderBy, null);
        if (cursor.moveToFirst()) {
            do {
                String resname = cursor.getString(cursor.getColumnIndex("m_resname"));
                String time = cursor.getString(cursor.getColumnIndex("timestamp"));
                float prices = Operate.getPrices(this, "Out_order", null, time);
                Out_order data = new Out_order(resname, time, prices);
                dataList.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
