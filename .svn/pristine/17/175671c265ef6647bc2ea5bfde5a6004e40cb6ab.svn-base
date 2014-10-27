/*
 * 文件名: LogWriter.java 版 权： Copyright Huawei Tech. Co. Ltd. All Rights Reserved.
 * 描 述: [该类的简要描述] 创建人: 盛兴亚 创建时间:Feb 15, 2012 修改人： 修改时间: 修改内容：[修改内容]
 */
package com.android.baseline.framework.log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import android.util.Log;

import com.android.baseline.framework.log.util.FileUtil;

/**
 * 
 * [日志信息写入到指定的文件中]<BR>
 * 
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2013-1-28]
 */
public class LogWriter
{
    /**
     * Log tag
     */
    public static final String TAG = "LogWriter";

    /**
     * 时间格式
     */
    private static final DateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS.sss");

    /**
     * byte size
     */
    private static final int BYTESIZE = 1024;

    private final Comparator<File> c = new Comparator<File>()
    {
        public int compare(File f1, File f2)
        {
            return String.CASE_INSENSITIVE_ORDER.compare(f1.getName(),
                    f2.getName());
        }
    };

    /**
     * the file being logged into
     */
    private File current;

    /**
     * the amount of log files in loop
     */
    private int fileAmount = 0;

    /**
     * one log file's size limited
     */
    private long maxSize = 0;

    /**
     * history logs exist in the sdcard
     */
    private ArrayList<File> historyLogs = null;

    private DateFormat timestampOfName = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * logging writer
     */
    private PrintWriter writer = null;

    /**
     * [构造简要说明]
     * 
     * @param current is always the original file
     * @param fileAmount log file total number
     * @param maxSize one log file max size
     */
    public LogWriter(File current, int fileAmount, long maxSize)
    {
        this.current = current;
        this.fileAmount = fileAmount <= 0 ? LogCache.FILE_AMOUNT : fileAmount;
        this.maxSize = (maxSize <= 0) ? LogCache.MAXSIZE_PERFILE : maxSize;
        initialize();
    }

    /**
     * [一句话功能简述]<BR>
     * [功能详细描述]
     * 
     * @return result of initialize
     */
    public synchronized boolean initialize()
    {
        // Log.v(TAG, "initializing... ");
        try
        {
            if (!current.getParentFile().exists())
            {
                if (!current.getParentFile().mkdirs())
                {
                    return false;
                }
            }
            if (null == historyLogs)
            {
                File[] fs = current.getParentFile().listFiles(new FilenameFilter()
                {
                    public boolean accept(File dir, String filename)
                    {
                        return filename.endsWith(Logger.LOG_FILE_SUFFIX);
                    }
                });
                if (fs != null && fs.length != 0)
                {
                    historyLogs = new ArrayList<File>(Arrays.asList(fs));
                }
                else
                {
                    historyLogs = new ArrayList<File>();
                }
            }
            writer = new PrintWriter(new FileOutputStream(current,
                    current.exists() && isCurrentAvailable()),
                    true);
            // 打印开始日志
            printBegin();
            Log.v(TAG,
                    "initialized.");
            return true;
        }
        catch (Exception e)
        {
            Log.e("LogWriter",
                    "print log to file failed",
                    e);
            return false;
        }
    }

    private File getTheEarliest()
    {
        Collections.sort(historyLogs,
                c);
        return historyLogs.get(0);
    }

    /**
     * 日志超过最大容量后，处理日志文件,生成一个新的；超过文件数目需要删除
     * 
     * @return deleteResult
     */
    public boolean rotate()
    {
        File des = new File(current.getParentFile(), newName());
        try
        {
            close();
            boolean result = current.renameTo(des);
            if (!result || !initialize())
            {
                Log.v(TAG, "rename or initialize error!");
                return false;
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "", e);
            return false;
        }
        historyLogs.add(des);
        Log.i(TAG, "old historyLogs: " + historyLogs);
        while (historyLogs.size() > fileAmount)
        {
            Log.v(TAG, "begin to delete the redundant log file...");
            boolean deleteResult = FileUtil.forceDeleteFile(getTheEarliest());
            if (deleteResult)
            {
                Log.i(TAG, "delete " + historyLogs.get(0).getName() + "successfully.");
                historyLogs.remove(0);
            }
            else
            {
                Log.i(TAG, "delete " + historyLogs.get(0).getName() + "abortively.");
                return false;
            }
        }
        Log.i(TAG, "new historyLogs: " + historyLogs);
        return true;
    }

    /**
     * 判断正在打日志的问价是否存在
     * 
     * @return 是否存在
     */
    public boolean isCurrentExist()
    {
        return current.exists();
    }

    /**
     * msg放入之前预算是否会超过最大容量
     * 
     * @param msg msg
     * @return isCurrentAvailable isCurrentAvailable
     */
    public boolean isCurrentAvailable(String msg)
    {
        return msg.getBytes().length + current.length() < maxSize;
    }

    /**
     * 文件未达到最大容量
     * 
     * @return 文件是否未达到最大容量
     */
    public boolean isCurrentAvailable()
    {
        return current.length() < maxSize;
    }

    /**
     * 新生成的日志名,按照最新的规格：日志名称：YYYYMMDDHHMMSS.log
     * 
     * @return newName 新文件名 modify：shenyadong
     */
    public String newName()
    {
        String name = current.getAbsolutePath();
        int dox = name.lastIndexOf('.');
        String suffix = name.substring(dox);
        return timestampOfName.format(System.currentTimeMillis()) + suffix;
    }

    /**
     * delete the earliest log file
     * 
     * @return true if delete successfully, false otherwise
     */
    private boolean deleteTheEarliest()
    {
        return (historyLogs.size() != 0) && getTheEarliest().delete();
    }

    /**
     * delete all other logs
     * 
     * @return true if delete successfully, false otherwise
     */
    @SuppressWarnings("unused")
    private boolean deleteAllOthers()
    {
        for (File file : historyLogs)
        {
            if (!file.delete())
            {
                return false;
            }
        }
        return true;
    }

    /**
     * flush the msg into the log file
     * 
     * @param msg msg
     */
    public void println(String msg)
    {
        if (null == writer)
        {
            initialize();
        }
        else
        {
            writer.println(msg);
        }
    }

    /**
     * 打开始日志
     */
    private void printBegin()
    {
        StringBuilder sbr = new StringBuilder();

        // 输入起始内容
        sbr.append("Begin Time:");
        sbr = addCurrentTime(sbr);

        println(sbr.toString());
    }

    /**
     * 添加时间
     * 
     * @param sbr StringBuilder待添加
     * @return 添加过后的StringBuilder
     */
    public StringBuilder addCurrentTime(StringBuilder sbr)
    {
        if (null == sbr)
        {
            return null;
        }

        // 添加时间,格式:YYYY-MM-DD HH-MM-SS.mmm
        sbr.append(TIME_FORMAT.format(System.currentTimeMillis()));

        return sbr;
    }

    /**
     * [一句话功能简述]<BR>
     * [功能详细描述]
     * 
     * @param des des
     * @throws java.io.IOException IOException
     */
    public void copyTo(File des) throws IOException
    {
        FileChannel fi = new FileInputStream(current).getChannel();
        FileChannel fo = new FileOutputStream(des,
                false).getChannel();
        ByteBuffer bf = ByteBuffer.allocateDirect(BYTESIZE);
        while (fi.read(bf) != -1)
        {
            bf.flip();
            fo.write(bf);
            bf.clear();
        }
        fi.close();
        fo.close();
    }

    /**
     * retrieve the log text<BR>
     * [功能详细描述]
     * 
     * @param logFile logFile
     * @return log text
     * @throws java.io.IOException IOException
     */
    public String getTextInfo(File logFile) throws IOException
    {
        BufferedReader bReader = null;
        StringBuilder sbr = new StringBuilder();
        String line;
        bReader = new BufferedReader(new InputStreamReader(new FileInputStream(logFile)));
        line = bReader.readLine();
        while (null != line)
        {
            sbr.append(line).append("\n");
            line = bReader.readLine();
        }
        bReader.close();
        return sbr.toString();
    }

    /**
     * [一句话功能简述]<BR>
     * [功能详细描述]
     * 
     * @return true if delete successfully, false otherwise
     */
    public boolean clearSpace()
    {
        return deleteTheEarliest();
    }

    /**
     * [一句话功能简述]<BR>
     * [功能详细描述]
     */
    public synchronized void close()
    {
        if (null != writer)
        {
            writer.close();
        }
    }
}
