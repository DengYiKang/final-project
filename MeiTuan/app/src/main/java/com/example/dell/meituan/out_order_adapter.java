package com.example.dell.meituan;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by dell on 2017/6/5.
 */
//未完成订单
public class out_order_adapter extends RecyclerView.Adapter<out_order_adapter.ViewHorder> {
    private List<Out_order> mdata;
    private Context mContext;

    public out_order_adapter(List<Out_order> l) {
        mdata = l;
    }

    @Override
    public ViewHorder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.out_order_cardview, parent, false);
        mContext = parent.getContext();
        final ViewHorder horder = new ViewHorder(view);
        //删除订单
        horder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = horder.getAdapterPosition();
                Out_order data = mdata.get(position);
                Operate.delete_out_order(mContext, data.getTime(), null);
                mdata.remove(position);
                notifyItemRemoved(position);
            }
        });
        //点击事件，跳转至未完成订单详情
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = horder.getAdapterPosition();
                Out_order data = mdata.get(position);
                Intent intent = new Intent(mContext, Out_order_detail_Activity.class);
                intent.putExtra("timestamp", data.getTime());
                mContext.startActivity(intent);
            }
        });
        return horder;
    }

    @Override
    public void onBindViewHolder(ViewHorder holder, int position) {
        Out_order data = mdata.get(position);
        holder.resname.setText(data.getM_resname());
        holder.time.setText(data.getTime());
        holder.prices.setText(String.valueOf(data.getPrices()));
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class ViewHorder extends RecyclerView.ViewHolder {
        private TextView resname, time, prices;
        private Button delete;

        public ViewHorder(View itemView) {
            super(itemView);
            resname = (TextView) itemView.findViewById(R.id.out_order_resname);
            time = (TextView) itemView.findViewById(R.id.out_order_time);
            prices = (TextView) itemView.findViewById(R.id.out_order_prices);
            delete = (Button) itemView.findViewById(R.id.out_order_delete);
        }
    }
}
