package com.example.dell.meituan;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by dell on 2017/6/6.
 */
//已完成订单的adapter
public class fin_order_adapter extends RecyclerView.Adapter<fin_order_adapter.ViewHolder> {
    private List<Fin_order> mdata;
    private Context mContext;

    public fin_order_adapter(List<Fin_order> l) {
        mdata = l;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fin_order_cardview, parent, false);
        mContext = parent.getContext();
        final ViewHolder horder = new ViewHolder(view);
        //整个view的点击事件，跳转到详情
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = horder.getAdapterPosition();
                Fin_order data = mdata.get(position);
                Intent intent = new Intent(mContext, Fin_order_detail_Activity.class);
                intent.putExtra("timestamp", data.getTime());
                mContext.startActivity(intent);
            }
        });
        return horder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Fin_order data = mdata.get(position);
        holder.resname.setText(data.getM_resname());
        holder.time.setText(data.getTime());
        holder.prices.setText(String.valueOf(data.getPrices()));
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView resname, time, prices;
        public ViewHolder(View itemView) {
            super(itemView);
            resname = (TextView) itemView.findViewById(R.id.fin_order_resname);
            time = (TextView) itemView.findViewById(R.id.fin_order_time);
            prices = (TextView) itemView.findViewById(R.id.fin_order_prices);
        }
    }
}
