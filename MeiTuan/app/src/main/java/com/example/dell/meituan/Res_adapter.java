package com.example.dell.meituan;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

//饭店adapter
public class Res_adapter extends RecyclerView.Adapter<Res_adapter.ViewHolder> {
    private List<Res> mData;
    private Context mContext;

    public Res_adapter(List<Res> l) {
        mData = l;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_cardview, parent, false);
        mContext = parent.getContext();
        final ViewHolder viewHolder = new ViewHolder(view);
        //详情
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                Res data = mData.get(position);
                Intent intent = new Intent(mContext, Dish_information_Activity.class);
                intent.putExtra("resname", data.getResname());
                mContext.startActivity(intent);
            }
        });
        //delete 的点击事件
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int position = viewHolder.getAdapterPosition();
                final Res data = mData.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("警告");
                builder.setMessage("确定删除吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Operate.delete_res(mContext, data.getResname());
                        mData.remove(position);
                        notifyItemRemoved(position);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.show();
            }
        });
        //modify 的点击事件
        viewHolder.modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                showDialog(position);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Res data = mData.get(position);
        holder.phone.setText(data.getPhone());
        holder.address.setText(data.getAddress());
        holder.name.setText(data.getResname());
        Glide.with(mContext).load(R.drawable.canting).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name, address, phone;
        private Button delete, modify;
        private ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.main_name);
            address = (TextView) itemView.findViewById(R.id.main_address);
            phone = (TextView) itemView.findViewById(R.id.main_phone);
            delete = (Button) itemView.findViewById(R.id.main_delete);
            modify = (Button) itemView.findViewById(R.id.main_modify);
            image = (ImageView) itemView.findViewById(R.id.res_image);
        }
    }

    //修改信息对话框
    public void showDialog(final int position) {
        AlertDialog.Builder builder;
        AlertDialog alertDialog;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.main_modify_showdialog, null);
        builder = new AlertDialog.Builder(mContext);
        builder.setTitle("修改信息");
        //获取控件
        final EditText editText1 = (EditText) layout.findViewById(R.id.main_modify_resname);
        final EditText editText2 = (EditText) layout.findViewById(R.id.main_modify_address);
        final EditText editText3 = (EditText) layout.findViewById(R.id.main_modify_phone);
        //获取信息
        final String dir = mData.get(position).getResname();
        String edit_address = mData.get(position).getAddress();
        final String edit_phone = mData.get(position).getPhone();
        //展示原信息
        editText1.setText(dir);
        editText2.setText(edit_address);
        editText3.setText(edit_phone);
        //光标移到最后
        editText1.setSelection(editText1.getText().length());
        editText2.setSelection(editText2.getText().length());
        editText3.setSelection(editText3.getText().length());
        builder.setView(layout);
        //对话框 确定 与 取消
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //获取输入框里的信息
                String resname = editText1.getText().toString();
                String address = editText2.getText().toString();
                String phone = editText3.getText().toString();
                if(TextUtils.isEmpty(resname.trim()) || TextUtils.isEmpty(address.trim()) || TextUtils.isEmpty(phone.trim())){
                    Toast.makeText(mContext, "以上信息不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    //修改数据库
                    Operate.update_res(mContext, dir, "phone", phone);
                    Operate.update_res(mContext, dir, "address", address);
                    Operate.update_res(mContext, dir, "resname", resname);
                    //修改list中的值
                    mData.get(position).setPhone(phone);
                    mData.get(position).setAddress(address);
                    mData.get(position).setResname(resname);
                    //通知更新
                    notifyItemChanged(position);
                } catch (Exception e) {
                    Toast.makeText(mContext, "名字重复", Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }
}
