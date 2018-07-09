package com.jcgroup.demo.base;

import java.io.Serializable;


/**
 * @author zhuhf
 * @version [DX-AndroidLibrary, 2018-03-07]
 */
public class InfoResult<T> implements Serializable {
    static final String SUCCESS_CODE = "0";
    private String code;
    private String errmsg;
    private T data;
    
    public boolean isSuccess() {
        return SUCCESS_CODE.equals(code);
    }
    
    public String getCode() {
        return code;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    public String getErrmsg() {
        return errmsg;
    }
    
    @Override
    public String toString() {
        return "InfoResult{" +
                "code='" + code + '\'' +
                ", errmsg='" + errmsg + '\'' +
                ", data=" + data +
                '}';
    }
}
