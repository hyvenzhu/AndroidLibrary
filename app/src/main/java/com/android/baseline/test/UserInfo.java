package com.android.baseline.test;

public class UserInfo
{
    private String mobile;
    private String name;
    public String getMobile()
    {
        return mobile;
    }
    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    
    @Override
    public String toString()
    {
        return "mobile:" + mobile + "\nname:" + name;
    }
}
