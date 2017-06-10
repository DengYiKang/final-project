package com.example.dell.meituan;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by dell on 2017/6/7.
 */
//已完成订单详情
public class fin_order_detail_adapter extends RecyclerView.Adapter<fin_order_detail_adapter.ViewHolder> {
    private List<Fin_order_detail> mdata;
    private Context mContext;

    public fin_order_detail_adapter(List<Fin_order_detail> l) {
        mdata = l;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fin_order_detail_cardview, parent, false);
        mContext = parent.getContext();
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Fin_order_detail data = mdata.get(position);
        holder.name.setText(data.getDisname());
        holder.price.setText(String.valueOf(data.getPrice()));
        holder.amount.setText(String.valueOf(data.getAmount()));
        holder.total_prices.setText(String.valueOf(data.getTotal_prices()));
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name, price, amount, total_prices;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.fin_order_detail_disname);
            price = (TextView) itemView.findViewById(R.id.fin_order_detail_price);
            amount = (TextView) itemView.findViewById(R.id.fin_order_detail_amount);
            total_prices = (TextView) itemView.findViewById(R.id.fin_order_detail_total_prices);
        }
    }
}
