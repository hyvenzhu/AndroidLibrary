package com.android.baseline.util.crash2email;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import android.os.Build;
import android.sax.Element;
import android.sax.ElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Xml;

import com.android.baseline.AppDroid;
import com.android.baseline.util.APKUtil;
import com.android.baseline.util.MailUtil;
import com.android.baseline.util.MailUtil.MailInfo;

/**
 * Provide crash email info
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2014-9-26]
 */
public class CrashMailProvider
{
    private final String LINE_SEPARATOR = System.getProperty("line.separator");
    /** Mail Senders*/
    private List<MailInfo> fromEmailList = new ArrayList<MailInfo>();
    /** Mail receivers*/
    private List<String> toEmailList = new ArrayList<String>();
    private MailInfo mailInfo = null;
    private static CrashMailProvider sInstance;
    
    private CrashMailProvider()
    {
        loadSenderAndReceivers();
    }
    
    /**
     * Single instance
     * @return
     */
    public synchronized static CrashMailProvider getInstance()
    {
        if (sInstance == null)
        {
            sInstance = new CrashMailProvider();
        }
        return sInstance;
    }
    
    /**
     * Build email info
     * @param crashMsg 错误信息
     * @return
     */
    public MailInfo getMailInfo(String crashMsg)
    {
        String subject = APKUtil.getPackageName(AppDroid.getInstance().getApplicationContext())
            + "_v" + APKUtil.getVerName(AppDroid.getInstance().getApplicationContext()) + " Crash Report";
        MailUtil.MailInfo mailInfo = new MailUtil.MailInfo();
        int index = (int) (Math.floor(Math.random() * fromEmailList.size()));
        MailInfo info = fromEmailList.get(index);
        mailInfo.setFrom(info.getFrom());
        mailInfo.setPassword(info.getPassword());
        mailInfo.setSmtpHost(info.getSmtpHost());
        mailInfo.setNeedAuth(true);
        String[] tos = new String[toEmailList.size()];
        toEmailList.toArray(tos);
        mailInfo.setToList(tos);
        mailInfo.setSubject(subject);
        String content = crashMsg + LINE_SEPARATOR + LINE_SEPARATOR + collectClientInfo();
        LogUtil.e("GlobalExceptionHandler", content);
        mailInfo.setContent(content);
        return mailInfo;
    }
    
    /**
     * 初始化信息邮件信息
     */
    private void loadSenderAndReceivers()
    {
        try
        {
            InputStream is = AppDroid.getInstance().getApplicationContext().getAssets().open("emails.xml");
            RootElement document = new RootElement("addrs");
            Element fromList = document.getChild("from-list");
            Element itemFrom = fromList.getChild("item");
            itemFrom.setElementListener(new ElementListener()
            {

                @Override
                public void end()
                {
                    fromEmailList.add(mailInfo);
                }

                @Override
                public void start(Attributes attributes)
                {
                    mailInfo = new MailInfo();
                }
            });
            itemFrom.getChild("account").setEndTextElementListener(new EndTextElementListener()
            {
                @Override
                public void end(String body)
                {
                    mailInfo.setFrom(body);
                }
            });
            itemFrom.getChild("password").setEndTextElementListener(new EndTextElementListener()
            {
                @Override
                public void end(String body)
                {
                    mailInfo.setPassword(body);
                }
            });
            itemFrom.getChild("smtp-host").setEndTextElementListener(new EndTextElementListener()
            {
                @Override
                public void end(String body)
                {
                    mailInfo.setSmtpHost(body);
                }
            });
            Element toList = document.getChild("to-list");
            Element itemTo = toList.getChild("item");
            itemTo.setEndTextElementListener(new EndTextElementListener()
            {
                @Override
                public void end(String body)
                {
                    toEmailList.add(body);
                }
            });
            Xml.parse(new InputStreamReader(is),
                    document.getContentHandler());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException("File of 'emails.xml' dose not exist in assets dir");
        }
    }
    
    /**
     * Collect device info
     * @return
     */
    private String collectClientInfo()
    {
        StringBuilder systemInfo = new StringBuilder();
        systemInfo.append("CLIENT-INFO");
        systemInfo.append(LINE_SEPARATOR);
        systemInfo.append("Id: ");
        systemInfo.append(Build.ID);
        systemInfo.append(LINE_SEPARATOR);
        systemInfo.append("Display: ");
        systemInfo.append(Build.DISPLAY);
        systemInfo.append(LINE_SEPARATOR);
        systemInfo.append("Product: ");
        systemInfo.append(Build.PRODUCT);
        systemInfo.append(LINE_SEPARATOR);
        systemInfo.append("Device: ");
        systemInfo.append(Build.DEVICE);
        systemInfo.append(LINE_SEPARATOR);
        systemInfo.append("Board: ");
        systemInfo.append(Build.BOARD);
        systemInfo.append(LINE_SEPARATOR);
        systemInfo.append("CpuAbility: ");
        systemInfo.append(Build.CPU_ABI);
        systemInfo.append(LINE_SEPARATOR);
        systemInfo.append("Manufacturer: ");
        systemInfo.append(Build.MANUFACTURER);
        systemInfo.append(LINE_SEPARATOR);
        systemInfo.append("Brand: ");
        systemInfo.append(Build.BRAND);
        systemInfo.append(LINE_SEPARATOR);
        systemInfo.append("Model: ");
        systemInfo.append(Build.MODEL);
        systemInfo.append(LINE_SEPARATOR);
        systemInfo.append("Type: ");
        systemInfo.append(Build.TYPE);
        systemInfo.append(LINE_SEPARATOR);
        systemInfo.append("Tags: ");
        systemInfo.append(Build.TAGS);
        systemInfo.append(LINE_SEPARATOR);
        systemInfo.append("FingerPrint: ");
        systemInfo.append(Build.FINGERPRINT);
        systemInfo.append(LINE_SEPARATOR);
        systemInfo.append("Version.Incremental: ");
        systemInfo.append(Build.VERSION.INCREMENTAL);
        systemInfo.append(LINE_SEPARATOR);
        systemInfo.append("Version.Release: ");
        systemInfo.append(Build.VERSION.RELEASE);
        systemInfo.append(LINE_SEPARATOR);
        systemInfo.append("SDKInt: ");
        systemInfo.append(Build.VERSION.SDK_INT);
        systemInfo.append(LINE_SEPARATOR);
        systemInfo.append("Version.CodeName: ");
        systemInfo.append(Build.VERSION.CODENAME);
        systemInfo.append(LINE_SEPARATOR);
        String clientInfomation = systemInfo.toString();
        systemInfo.delete(0, systemInfo.length());
        return clientInfomation;
    }
}
