package com.example.dell.meituan;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by dell on 2017/6/3.
 */
//菜品adapter
public class Dis_adapter extends RecyclerView.Adapter<Dis_adapter.ViewHold> {
    private List<Dis> mdata;
    private Context mContext;

    public Dis_adapter(List<Dis> l) {
        mdata = l;
    }

    @Override
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dish_cardview, parent, false);
        mContext = parent.getContext();
        final ViewHold hold = new ViewHold(view);
        //减号的点击事件
        hold.reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = hold.getAdapterPosition();
                //如果数量为零就不再减
                if(mdata.get(position).getAmount() == 0){
                    return;
                }else{
                    Operate.update_dis(mContext, mdata.get(position).getDisname(), mdata.get(position).getM_resname(),
                            mdata.get(position).getAmount()-1);
                    mdata.get(position).setAmount(mdata.get(position).getAmount()-1);
                }
                notifyItemChanged(position);
            }
        });
        //加号的点击事件
        hold.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = hold.getAdapterPosition();
                Operate.update_dis(mContext, mdata.get(position).getDisname(), mdata.get(position).getM_resname(),
                        mdata.get(position).getAmount() + 1);
                mdata.get(position).setAmount(mdata.get(position).getAmount() + 1);
                notifyItemChanged(position);
            }
        });
        return hold;
    }

    @Override
    public void onBindViewHolder(ViewHold holder, int position) {
        Dis data = mdata.get(position);
        String price = String.valueOf(data.getPrice());
        String amount = String.valueOf(data.getAmount());
        holder.disname.setText(data.getDisname());
        holder.price.setText(price + "元");
        holder.amount.setText(amount);
        Glide.with(mContext).load(R.drawable.caipus).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class ViewHold extends RecyclerView.ViewHolder {
        private TextView disname, price, amount;
        private Button reduce, plus;
        private ImageView image;

        public ViewHold(View itemView) {
            super(itemView);
            disname = (TextView) itemView.findViewById(R.id.dish_name);
            price = (TextView) itemView.findViewById(R.id.dish_price);
            amount = (TextView) itemView.findViewById(R.id.dish_amount);
            reduce = (Button) itemView.findViewById(R.id.reduce);
            plus = (Button) itemView.findViewById(R.id.plus);
            image = (ImageView) itemView.findViewById(R.id.dis_image);
        }
    }

    //把’定量‘清零
    public void set_list_zero() {
        String address = mdata.get(0).getM_resname();
        for (int i = 0; i < mdata.size(); i++) {
            mdata.get(i).setAmount(0);
        }
        notifyDataSetChanged();
        Operate.update_dis(mContext, null, address, 0);
    }

    //如果没有点菜却点击‘确认订单’，则会跳出提示框，判断是否点了菜
    public boolean if_choose_anyone() {
        boolean if_choose = false;
        for (int i = 0; i < mdata.size(); i++) {
            if (mdata.get(i).getAmount() > 0) {
                if_choose = true;
                break;
            }
        }
        return if_choose;
    }

}
