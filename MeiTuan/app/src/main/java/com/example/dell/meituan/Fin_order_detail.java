package com.example.dell.meituan;

/**
 * Created by dell on 2017/6/7.
 */
//已完成订单详情
public class Fin_order_detail {
    private String disname;
    private String m_resname;
    private float price;
    private String time;
    private int amount;
    private float total_prices;

    public Fin_order_detail(String _disname, String _m_resname, float _price, int _amount, String time) {
        disname = _disname;
        m_resname = _m_resname;
        price = _price;
        amount = _amount;
        total_prices = price * amount;
        this.time = time;
    }

    public String getDisname() {
        return disname;
    }

    public String getM_resname() {
        return m_resname;
    }

    public float getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }

    public float getTotal_prices() {
        return total_prices;
    }

    public String getTime() {
        return time;
    }
}
