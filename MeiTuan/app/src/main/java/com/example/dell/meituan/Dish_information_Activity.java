package com.example.dell.meituan;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Dish_information_Activity extends AppCompatActivity {
    private EmptyRecyclerView dish_information_recyclerview;
    private Button dish_information_decide;
    private List<Dis> datalist = new ArrayList<>();
    private MyDatabaseHelper dbHelper;
    private Dis_adapter dish_adapter;
    private String resname;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //接受上个Activity传来的数据
        setContentView(R.layout.activity_dish_information);
        Intent intent = getIntent();
        resname = intent.getStringExtra("resname");
        //打开数据库
        dbHelper = new MyDatabaseHelper(this, "MeiTuan.db", null, 4);
        //toolbar
        toolbar = (Toolbar) findViewById(R.id.dish_toolbar);
        toolbar.setTitle(resname +" 菜品");
        setSupportActionBar(toolbar);
        //获取控件
        dish_information_decide = (Button) findViewById(R.id.dish_information_decide);
        dish_information_recyclerview = (EmptyRecyclerView) findViewById(R.id.dish_information_recyclerview);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        dish_information_recyclerview.setLayoutManager(layoutManager);
        //读取数据
        read_into_Dis(resname);
        //adapter
        dish_adapter = new Dis_adapter(datalist);
        dish_information_recyclerview.setAdapter(dish_adapter);
        //确认订单
        dish_information_decide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!dish_adapter.if_choose_anyone()) {
                    Toast.makeText(Dish_information_Activity.this, "请挑选您要的菜", Toast.LENGTH_SHORT).show();
                    return;
                }
                Operate.insert_out_order(Dish_information_Activity.this, resname);
                //点击’确认订单‘后，把显示的数量全部清零，包括数据库中对应的
                dish_adapter.set_list_zero();
                //显示对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(Dish_information_Activity.this);
                builder.setTitle("成功");
                builder.setMessage("您可以在未完成订单里查看");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Dish_information_Activity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                builder.show();
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    //menu的添加功能的点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //在对话框中实行添加
        AlertDialog.Builder builder;
        AlertDialog alertDialog;
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.dish_insert_dialog, null);
        builder = new AlertDialog.Builder(this);
        builder.setTitle("添加菜品");
        builder.setView(layout);
        //获取edittext
        final EditText editText1 = (EditText) layout.findViewById(R.id.dish_insert_disname);
        final EditText editText2 = (EditText) layout.findViewById(R.id.dish_insert_price);
        //对话框‘确定’按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String disname = editText1.getText().toString();
                String mprice = editText2.getText().toString();
                float price;
                //信息为空的错误处理
                if (TextUtils.isEmpty(disname.trim()) || TextUtils.isEmpty(mprice.trim())) {
                    Toast.makeText(Dish_information_Activity.this, "以上信息不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                //价格为非数值的错误处理
                try {
                    price = Float.parseFloat(mprice);
                } catch (Exception e) {
                    Toast.makeText(Dish_information_Activity.this, "价格输入错误", Toast.LENGTH_SHORT).show();
                    return;
                }
                //名字重复的错误处理
                try {
                    Operate.insert_dis(Dish_information_Activity.this, disname, resname, price, 0);
                } catch (Exception e) {
                    Toast.makeText(Dish_information_Activity.this, "名字重复", Toast.LENGTH_LONG).show();
                }
                //更新datalist， adapter
                Dis data = new Dis(disname, resname, price);
                datalist.add(data);
                dish_adapter.notifyDataSetChanged();
            }
        });
        //对话框中的取消按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
        return true;
    }

    //从数据库中读取数据存入datalist，更新adapter
    private void read_into_Dis(String resname) {
        datalist.clear();
        String selection = "m_resname = ?";
        String[] selectionArgs = new String[]{resname};
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Dis", null, selection, selectionArgs, null, null, "price");
        if (cursor.moveToFirst()) {
            do {
                String disname = cursor.getString(cursor.getColumnIndex("disname"));
                float price = cursor.getFloat(cursor.getColumnIndex("price"));
                int amount = cursor.getInt(cursor.getColumnIndex("amount"));
                Dis data = new Dis(disname, resname, price, amount);
                datalist.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
