package com.hiphonezhu.test.demo;

import java.io.Serializable;

/**
 * 电话号码归属地
 *
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 16/9/3 13:05]
 */
public class MobileBean implements Serializable {
    private String phone;
    private String prefix;
    private String supplier;
    private String province;
    private String city;
    private String suit;

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    public String getPhone() {
        return phone;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSupplier() {
        return supplier;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getSuit() {
        return suit;
    }

    @Override
    public String toString() {
        return "RetDataEntity{" +
                "phone='" + phone + '\'' +
                ", prefix='" + prefix + '\'' +
                ", supplier='" + supplier + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", suit='" + suit + '\'' +
                '}';
    }
}
