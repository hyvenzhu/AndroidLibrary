package com.android.baseline.util.crash2email;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import android.content.Context;
import android.os.Build;
import android.sax.Element;
import android.sax.ElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Xml;

import com.android.baseline.util.APKUtil;
import com.android.baseline.util.crash2email.MailUtil.MailInfo;

/**
 * 全局异常处理
 * 
 * @author hiphonezhu@gmail.com
 * @version [2014-6-25]
 */
public final class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler
{
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private Thread.UncaughtExceptionHandler defaultHandler;
    private boolean caughtException = false;
    private String clientInfo;

    private Context appContext;

    /**
     * 全局错误处理构造函数
     */
    public GlobalExceptionHandler(Context appContext)
    {
        this.appContext = appContext;
        this.defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 收集客户端信息
        this.clientInfo = collectClientInfo();
        loadEmails();
    }

    private List<MailInfo> fromEmailList = new ArrayList<MailInfo>();
    private List<String> toEmailList = new ArrayList<String>();
    private MailInfo mailInfo = null;

    /**
     * 读取邮件列表信息
     */
    private void loadEmails()
    {
        try
        {
            InputStream is = appContext.getAssets().open("emails.xml");
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
     * 捕获到异常
     * 
     * @param thread 异常线程
     * @param throwable 异常信息
     */
    @Override
    public void uncaughtException(final Thread thread, final Throwable throwable)
    {
        if (caughtException)
        {
            defaultHandler.uncaughtException(thread,
                    throwable);
            return;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        throwable.printStackTrace(ps);
        final String errorMsg = new String(baos.toByteArray());
        final String mailContent = errorMsg + LINE_SEPARATOR + LINE_SEPARATOR + clientInfo;

        // 将报错信息发送到指定的邮箱
        Thread sendMailThread = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    String subject = APKUtil.getPackageName() + "_v" + APKUtil.getVerName()
                            + " Crash Report";
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
                    mailInfo.setContent(mailContent);
                    try
                    {
                        MailUtil.sendMail(mailInfo);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    if (!handleException() && defaultHandler != null)
                    {
                        defaultHandler.uncaughtException(thread,
                                throwable);
                    }
                }
            }
        };
        sendMailThread.start();
    }

    /**
     * 自定义错误处理、收集错误信息 、发送错误报告等操作均在此完成.
     * 
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException()
    {
        try
        {
            // android.os.Process.killProcess(android.os.Process.myPid());
            // System.exit(1);
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

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
        systemInfo.delete(0,
                systemInfo.length());
        return clientInfomation;
    }
}
