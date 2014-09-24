package com.android.baseline.util.crash2email;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.xml.sax.Attributes;

import android.os.Build;
import android.sax.Element;
import android.sax.ElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Xml;

import com.android.baseline.AppDroid;
import com.android.baseline.util.APKUtil;

/**
 * 邮件操作封装
 * @author hiphonezhu@gmail.com
 * @version [2014-6-25]
 */
public class MailUtil
{
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static List<MailInfo> fromEmailList = new ArrayList<MailInfo>();
    private static List<String> toEmailList = new ArrayList<String>();
    private static MailInfo mailInfo = null;
    private static String clientInfo;
    
    /**
     * 初始化信息邮件信息
     */
    private static void init()
    {
        if (mailInfo != null)
        {
            return;
        }
        try
        {
            clientInfo = collectClientInfo();
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
     * 发送邮件
     * @param content 内容
     * @throws MessagingException
     */
    public static void sendMail(String content) throws MessagingException
    {
        init();
        String subject = APKUtil.getPackageName() + "_v" + APKUtil.getVerName() + " Crash Report";
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
        mailInfo.setContent(content + LINE_SEPARATOR + LINE_SEPARATOR + clientInfo);
        sendMail(mailInfo);
    }

    /**
     * 发送邮件
     * @param info 邮件信息
     * @throws MessagingException MessagingException
     */
    private static void sendMail(final MailInfo info) throws MessagingException
    {
        Properties props = new Properties();
        props.put("mail.smtp.host",
                info.getSmtpHost());
        props.put("mail.smtp.auth",
                String.valueOf(info.isNeedAuth()));
        Session session = Session.getDefaultInstance(props,
                new Authenticator()
                {
                    @Override
                    public PasswordAuthentication getPasswordAuthentication()
                    {
                        return new PasswordAuthentication(info.getFrom(),
                                info.getPassword());
                    }
                });
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(info.getFrom()));
        InternetAddress[] address = new InternetAddress[info.getToList().length];
        for (int i = 0; i < info.getToList().length; i++)
        {
            address[i] = new InternetAddress(info.getToList()[i]);
        }
        msg.setRecipients(Message.RecipientType.TO,
                address);
        Date current = new Date();
        msg.setSubject(info.getSubject());
        Multipart mp = new MimeMultipart();
        MimeBodyPart mbpContent = new MimeBodyPart();
        mbpContent.setText(info.getContent());
        mp.addBodyPart(mbpContent);
        msg.setContent(mp);
        msg.setSentDate(current);
        Transport.send(msg,
                address);
    }
    
    /**
     * Collect device info
     * @return
     */
    private static String collectClientInfo()
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

    /**
     * 邮件信息
     */
    public static class MailInfo
    {
        private String from;
        private String password;
        private String[] toList;
        private String subject;
        private String content;
        private boolean needAuth;
        private String smtpHost;

        public String getFrom()
        {
            return from;
        }

        public void setFrom(String from)
        {
            this.from = from;
        }

        public String getPassword()
        {
            return password;
        }

        public void setPassword(String password)
        {
            this.password = password;
        }

        public String[] getToList()
        {
            return toList;
        }

        public void setToList(String[] toList)
        {
            this.toList = toList;
        }

        public String getSubject()
        {
            return subject;
        }

        public void setSubject(String subject)
        {
            this.subject = subject;
        }

        public String getContent()
        {
            return content;
        }

        public void setContent(String content)
        {
            this.content = content;
        }

        public boolean isNeedAuth()
        {
            return needAuth;
        }

        public void setNeedAuth(boolean needAuth)
        {
            this.needAuth = needAuth;
        }

        public String getSmtpHost()
        {
            return smtpHost;
        }

        public void setSmtpHost(String smtpHost)
        {
            this.smtpHost = smtpHost;
        }
    }
}
