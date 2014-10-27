package com.android.baseline.test;

import android.sax.EndTextElementListener;
import android.sax.RootElement;

import com.android.baseline.framework.logic.InfoResult;
import com.android.baseline.framework.logic.parser.XmlParser;

public class UserLoginXmlParser extends XmlParser
{
    @Override
    public void parseResponse(final InfoResult infoResult, final RootElement element)
    {
        element.getChild("result").setEndTextElementListener(new EndTextElementListener()
        {
            
            @Override
            public void end(String body)
            {
                infoResult.setExtraObj(body);                
            }
        });
    }
}
