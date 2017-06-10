package com.example.dell.meituan;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by dell on 2017/6/6.
 */
//未完成订单详情
public class out_order_detail_adapter extends RecyclerView.Adapter<out_order_detail_adapter.ViewHorder> {
    private List<Out_order_detail> mdata;
    private Context mContext;

    public out_order_detail_adapter(List<Out_order_detail> l) {
        mdata = l;
    }

    @Override
    public ViewHorder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.out_order_detail_cardview, parent, false);
        mContext = parent.getContext();
        final ViewHorder horder = new ViewHorder(view);
        //删除订单
        horder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = horder.getAdapterPosition();
                Out_order_detail data = mdata.get(position);
                Operate.delete_out_order(mContext, data.getTime(), data.getDisname());
                mdata.remove(position);
                notifyItemRemoved(position);
            }
        });
        return horder;
    }

    @Override
    public void onBindViewHolder(ViewHorder holder, int position) {
        Out_order_detail data = mdata.get(position);
        holder.name.setText(data.getDisname());
        holder.price.setText(String.valueOf(data.getPrice()));
        holder.amount.setText(String.valueOf(data.getAmount()));
        holder.total_price.setText(String.valueOf(data.getTotal_prices()));
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class ViewHorder extends RecyclerView.ViewHolder {
        private TextView name, price, amount, total_price;
        private Button delete;

        public ViewHorder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.out_order_detail_disname);
            price = (TextView) itemView.findViewById(R.id.out_order_detail_price);
            amount = (TextView) itemView.findViewById(R.id.out_order_detail_amount);
            total_price = (TextView) itemView.findViewById(R.id.out_order_detail_total_prices);
            delete = (Button) itemView.findViewById(R.id.out_order_detail_delete);
        }
    }
}
