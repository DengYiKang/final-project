package com.example.dell.meituan;

/**
 * Created by dell on 2017/6/1.
 */
//饭店信息
public class Res {
    private String resname;
    private String address;
    private String phone;
    public Res(String m_name, String m_address, String m_phone){
        resname = m_name;
        address = m_address;
        phone = m_phone;
    }
    public String getResname(){
        return resname;
    }
    public String getAddress(){
        return address;
    }
    public String getPhone(){
        return phone;
    }
    public void setResname(String m_name){
        resname = m_name;
    }
    public void setAddress(String m_address){
        address = m_address;
    }
    public void setPhone(String m_phone){
        phone = m_phone;
    }
}
