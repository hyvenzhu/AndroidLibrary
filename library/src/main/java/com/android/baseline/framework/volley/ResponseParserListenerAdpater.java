package com.android.baseline.framework.volley;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.android.baseline.framework.logic.InfoResult;

/**
 * 解析适配器
 * @author hiphonezhu@gmail.com
 * @version [ELibrary, 2015/05/07 16:59]
 * @copyright Copyright 2010 RD information technology Co.,ltd.. All Rights Reserved.
 */
public class ResponseParserListenerAdpater implements InfoResultRequest.ResponseParserListener
{
    /** 附加数据*/
    private Map<String, Object> extras = new HashMap<String, Object>();

    public ResponseParserListenerAdpater putExtra(String key, Object value)
    {
        extras.put(key, value);
        return this;
    }

    protected Object getExtra(String key)
    {
        return extras.get(key);
    }

    public InfoResult doParse(final String response) throws Exception
    {
        return null;
    }
    public InfoResult doParse(final InputStream response) throws Exception
    {
        return null;
    }
}
