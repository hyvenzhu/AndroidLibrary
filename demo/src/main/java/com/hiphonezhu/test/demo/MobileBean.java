package com.hiphonezhu.test.demo;

/**
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2016/03/17 10:25]
 * @copyright Copyright 2010 RD information technology Co.,ltd.. All Rights Reserved.
 */
public class MobileBean{
    private String phone;
    private String prefix;
    private String supplier;
    private String province;
    private String city;
    private String suit;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSuit() {
        return suit;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    @Override
    public String toString() {
        return "MobileBean{" +
                "phone='" + phone + '\'' +
                ", prefix='" + prefix + '\'' +
                ", supplier='" + supplier + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", suit='" + suit + '\'' +
                '}';
    }
}
