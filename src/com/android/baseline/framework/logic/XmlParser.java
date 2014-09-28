package com.android.baseline.framework.logic;

import org.xml.sax.SAXException;

import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Xml;

import com.android.baseline.framework.volley.InfoResultRequest.ResponseParserListener;
/**
 * A abstract class that parse logic status with type xml
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2014-9-28]
 */
public abstract class XmlParser implements ResponseParserListener
{
    /**
     * 解析服务器结果的状态信息(业务成功与失败, 对应错误码和描述信息等)
     * @param logicStatus
     * @return InfoResult
     * @throws SAXException 
     */
    protected InfoResult parseLogicSatus(String logicStatus) throws SAXException
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
        Xml.parse(logicStatus, documentEl.getContentHandler());
        return infoBilder.build();
    }
}
