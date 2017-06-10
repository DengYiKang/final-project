package com.example.dell.meituan;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
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

public class MainActivity extends AppCompatActivity {
    private MyDatabaseHelper dbHelper;
    private EmptyRecyclerView recyclerView;
    private Res_adapter res_adapter;
    private List<Res> datalist = new ArrayList<>();
    private DrawerLayout mDrawerLayout;
    private NavigationView navView;
    private Toolbar toolbar;
    private View mEmptyView;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //打开数据库
        dbHelper = new MyDatabaseHelper(this, "MeiTuan.db", null, 4);
        preferences = getSharedPreferences("phone", this.MODE_PRIVATE);
        //判断是不是首次登录
        if (preferences.getBoolean("firststart", true)) {
            editor = preferences.edit();
            //将登录标志位设置为false，下次登录时不在显示首次登录界面
            editor.putBoolean("firststart", false);
            editor.commit();
            initData();
        }
        //获取控件
        mEmptyView = findViewById(R.id.res_emptyView);
        //toolbar
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        //滑动菜单
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //NavigationView
        navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    //跳转至未完成订单
                    case R.id.nav_out_order:
                        Intent intent = new Intent(MainActivity.this, Out_order_totalActivity.class);
                        startActivity(intent);
                        break;
                    //跳转至已完成订单
                    case R.id.nav_fin_order:
                        Intent intent2 = new Intent(MainActivity.this, Fin_order_totalActivity.class);
                        startActivity(intent2);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        //toolbar设置导航按钮
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_toc_black_48dp);
        }
        recyclerView = (EmptyRecyclerView) findViewById(R.id.main_recyclerview);
        res_adapter = new Res_adapter(datalist);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        //读取数据
        read_from_Res();
        recyclerView.setAdapter(res_adapter);
        recyclerView.setEmptyView(mEmptyView);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }
    //menu的点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            //添加饭店的功能
            case R.id.add_res:
                add_res();
                break;
            //打开滑动菜单
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return true;
    }

    //初始化数据
    public void initData() {
        dbHelper = new MyDatabaseHelper(this, "MeiTuan.db", null, 4);
        try {
            Operate.insert_res(this, "wojia", "12-34", "1234");
            Operate.insert_res(this, "tajia", "12-35", "1235");
            Operate.insert_res(this, "nijia", "12-36", "1236");
            Operate.insert_res(this, "KFC", "12-37", "1237");
            Operate.insert_res(this, "MCF", "12-38", "1238");
            Operate.insert_res(this, "MDL", "12-39", "1239");
            Operate.insert_dis(this, "宫保鸡丁", "wojia", 12, 0);
            Operate.insert_dis(this, "鱼香肉丝", "wojia", 12, 0);
            Operate.insert_dis(this, "麻婆豆腐", "wojia", 10, 0);
            Operate.insert_dis(this, "铁板牛肉", "wojia", 15, 0);
            Operate.insert_dis(this, "剁椒鱼头", "wojia", 15, 0);
            Operate.insert_dis(this, "西红柿炒蛋", "wojia", 10, 0);
            Operate.insert_dis(this, "辣鸡", "wojia", 12, 0);
            Operate.insert_dis(this, "宫保鸡丁", "tajia", 12, 0);
            Operate.insert_dis(this, "鱼香肉丝", "tajia", 12, 0);
            Operate.insert_dis(this, "麻婆豆腐", "tajia", 10, 0);
            Operate.insert_dis(this, "铁板牛肉", "tajia", 15, 0);
            Operate.insert_dis(this, "剁椒鱼头", "tajia", 15, 0);
            Operate.insert_dis(this, "西红柿炒蛋", "tajia", 10, 0);
            Operate.insert_dis(this, "辣鸡", "tajia", 12, 0);
            Operate.insert_dis(this, "宫保鸡丁", "nijia", 12, 0);
            Operate.insert_dis(this, "鱼香肉丝", "nijia", 12, 0);
            Operate.insert_dis(this, "麻婆豆腐", "nijia", 10, 0);
            Operate.insert_dis(this, "铁板牛肉", "nijia", 15, 0);
            Operate.insert_dis(this, "剁椒鱼头", "nijia", 15, 0);
            Operate.insert_dis(this, "西红柿炒蛋", "nijia", 10, 0);
            Operate.insert_dis(this, "辣鸡", "nijia", 12, 0);
            Operate.insert_dis(this, "薯条", "KFC", 5, 0);
            Operate.insert_dis(this, "烤鸡", "KFC", 5, 0);
            Operate.insert_dis(this, "烤鸭", "KFC", 5, 0);
            Operate.insert_dis(this, "圣代", "KFC", 5, 0);
            Operate.insert_dis(this, "意大利面", "KFC", 5, 0);
            Operate.insert_dis(this, "可乐", "KFC", 5, 0);
            Operate.insert_dis(this, "薯条", "MCF", 5, 0);
            Operate.insert_dis(this, "烤鸡", "MCF", 5, 0);
            Operate.insert_dis(this, "烤鸭", "MCF", 5, 0);
            Operate.insert_dis(this, "圣代", "MCF", 5, 0);
            Operate.insert_dis(this, "意大利面", "MCF", 5, 0);
            Operate.insert_dis(this, "可乐", "MCF", 5, 0);
            Operate.insert_dis(this, "薯条", "MDL", 5, 0);
            Operate.insert_dis(this, "烤鸡", "MDL", 5, 0);
            Operate.insert_dis(this, "烤鸭", "MDL", 5, 0);
            Operate.insert_dis(this, "圣代", "MDL", 5, 0);
            Operate.insert_dis(this, "意大利面", "MDL", 5, 0);
            Operate.insert_dis(this, "可乐", "MDL", 5, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //从数据库中读取数据至datalist中
    public void read_from_Res() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Res", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String resname = cursor.getString(cursor.getColumnIndex("resname"));
                String address = cursor.getString(cursor.getColumnIndex("address"));
                String phone = cursor.getString(cursor.getColumnIndex("phone"));
                Res data = new Res(resname, address, phone);
                datalist.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();
        res_adapter.notifyDataSetChanged();
    }
    //修改饭店信息对话框
    public void add_res(){
        AlertDialog.Builder builder;
        AlertDialog alertDialog;
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        //引入布局
        final View layout = inflater.inflate(R.layout.main_modify_showdialog, null);
        builder = new AlertDialog.Builder(this);
        //设置
        builder.setTitle("添加饭店");
        builder.setView(layout);
        //获取控件
        final EditText editText1 = (EditText) layout.findViewById(R.id.main_modify_resname);
        final EditText editText2 = (EditText) layout.findViewById(R.id.main_modify_address);
        final EditText editText3 = (EditText) layout.findViewById(R.id.main_modify_phone);
        //对话框的’确定按钮‘
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String resname = editText1.getText().toString();
                String address = editText2.getText().toString();
                String phone = editText3.getText().toString();
                //信息输入为空的错误处理
                if(TextUtils.isEmpty(resname.trim()) || TextUtils.isEmpty(address.trim()) || TextUtils.isEmpty(phone.trim())){
                    Toast.makeText(MainActivity.this, "以上信息不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                //名字重复的错误处理
                try{
                    Operate.insert_res(MainActivity.this, resname, address, phone);
                }catch (Exception e){
                    Toast.makeText(MainActivity.this, "名字重复", Toast.LENGTH_LONG).show();
                }
                //通知datalist，adapter更新
                Res data = new Res(resname, address, phone);
                datalist.add(data);
                res_adapter.notifyDataSetChanged();
            }
        });
        //对话框上的‘取消’按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }

}
