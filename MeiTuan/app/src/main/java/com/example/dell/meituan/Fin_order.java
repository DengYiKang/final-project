package com.example.dell.meituan;

/**
 * Created by dell on 2017/6/6.
 */
//已完成订单总概况
public class Fin_order {
    private String m_resname;
    private String time;
    private float prices;

    public Fin_order(String _m_resname, String time, float prices) {
        m_resname = _m_resname;
        this.time = time;
        this.prices = prices;
    }


    public String getM_resname() {
        return m_resname;
    }

    public float getPrices() {
        return prices;
    }

    public String getTime() {
        return time;
    }

}
