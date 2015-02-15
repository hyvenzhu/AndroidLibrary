package com.android.baseline.framework.logic.parser;

import android.sax.RootElement;

import com.android.baseline.framework.logic.InfoResult;
/**
 * 通用操作解析器
 * @author hiphonezhu@gmail.com
 * @version [OApp, 2014-11-10]
 */
public class SimpleXmlParser extends XmlParser
{
    /** 需要回传的值*/
    private Object extraObj;
    public SimpleXmlParser(Object extraObj)
    {
        this.extraObj = extraObj;
    }
    
    public SimpleXmlParser()
    {
    }
    
    @Override
    public void parseResponse(InfoResult infoResult, RootElement element)
    {
        infoResult.setExtraObj(extraObj);
    }
}
