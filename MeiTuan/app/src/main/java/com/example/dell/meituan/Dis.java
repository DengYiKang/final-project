package com.example.dell.meituan;

/**
 * Created by dell on 2017/6/3.
 */
//菜品数据

public class Dis {

    private String disname;
    private String m_resname;
    private float price;
    private int amount;

    public Dis(String _disname, String _m_resname, float _price) {
        disname = _disname;
        m_resname = _m_resname;
        price = _price;
        amount = 0;
    }
    public Dis(String _disname, String _m_resname, float _price, int _amount){
        disname = _disname;
        m_resname = _m_resname;
        price = _price;
        amount = _amount;
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

    public void setAmount(int a) {
        amount = a;
    }
}
