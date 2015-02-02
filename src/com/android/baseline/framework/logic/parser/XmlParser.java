package com.android.baseline.framework.logic.parser;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.SAXException;

import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Xml;

import com.android.baseline.framework.logic.InfoResult;
import com.android.baseline.framework.volley.InfoResultRequest.ResponseParserListener;
/**
 * A abstract class that parse logic status with type xml
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2014-9-28]
 */
public abstract class XmlParser implements ResponseParserListener
{
    /** 附加数据*/
    private Map<String, Object> extras = new HashMap<String, Object>();
    /**
     * 解析服务器结果的状态信息(业务成功与失败, 对应错误码和描述信息等)
     * @param response
     * @return InfoResult
     * @throws SAXException 
     */
    public InfoResult doParse(final String response) throws SAXException
    {
        RootElement documentEl = new RootElement("document");
        final InfoResult.Builder infoBilder = new InfoResult.Builder();
        documentEl.getChild("success").setEndTextElementListener(new EndTextElementListener()
        {
            @Override
            public void end(String body)
            {
                infoBilder.success(Boolean.parseBoolean(body));
            }
        });
        documentEl.getChild("errorCode").setEndTextElementListener(new EndTextElementListener()
        {
            @Override
            public void end(String body)
            {
                infoBilder.errorCode(body);
            }
        });
        documentEl.getChild("desc").setEndTextElementListener(new EndTextElementListener()
        {
            @Override
            public void end(String body)
            {
                infoBilder.desc(body);
            }
        });
        Xml.parse(response, documentEl.getContentHandler());
        InfoResult infoResult = infoBilder.build();
        parseResponse(infoResult, documentEl);
        return infoResult;
    }
    
    public XmlParser putExtra(String key, Object value)
    {
        extras.put(key, value);
        return this;
    }
    
    protected Object getExtra(String key)
    {
        return extras.get(key);
    }
    
    /**
     * 解析业务数据
     * @param infoResult
     * @param element
     */
    public abstract void parseResponse(final InfoResult infoResult, final RootElement element);
}
