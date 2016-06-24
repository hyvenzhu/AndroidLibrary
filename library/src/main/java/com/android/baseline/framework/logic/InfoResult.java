package com.android.baseline.framework.logic;
/**
 * 网络请求返回数据
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2013-7-23]
 */
public class InfoResult<T>
{
    private boolean success;
    private String errorCode;
    private String desc;
    private T extraObj;

    public InfoResult(boolean success, String errorCode, String desc)
    {
        this.success = success;
        this.errorCode = errorCode;
        this.desc = desc;
    }

    public boolean isSuccess()
    {
        return success;
    }

    public void setSuccess(boolean success)
    {
        this.success = success;
    }

    public String getErrorCode()
    {
        return errorCode;
    }

    public void setErrorCode(String errorCode)
    {
        this.errorCode = errorCode;
    }

    public T getExtraObj()
    {
        return extraObj;
    }

    public void setExtraObj(T extraObj)
    {
        this.extraObj = extraObj;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }
}
