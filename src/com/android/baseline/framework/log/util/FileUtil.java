package com.android.baseline.framework.log.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.Context;

import com.android.baseline.framework.log.Logger;

/**
 * 
 * [文件处理工具]<BR>
 * 
 * @author zhuhf
 * @version [BaseLine_Android, 2013-1-28]
 */
public class FileUtil
{
    private static final String TAG = "FileUtil";

    /**
     * 可保存的最大的文件数量
     */
    private static final int MAX_FILE_COUNT = 10;

    private static final int WAIT_TIME = 200;

    private FileUtil()
    {
    }

    /**
     * fore delete a file,thread safe.
     * 
     * @param file file
     * @return del result
     */
    public static boolean forceDeleteFile(File file)
    {
        boolean result = false;
        int tryCount = 0;
        while (!result && tryCount < MAX_FILE_COUNT)
        {
            tryCount++;
            result = file.delete();
            if (!result)
            {
                try
                {
                    synchronized (file)
                    {
                        file.wait(WAIT_TIME);
                    }
                }
                catch (InterruptedException e)
                {
                    Logger.e("FileUtil.forceDeleteFile",
                            "",
                            e);
                }
            }
        }
        Logger.v("FileUtil.forceDeleteFile",
                "tryCount = " + tryCount);
        return result;
    }
    
    /**
     * read strings for a file in /data/data/package/filename
     * 
     * @param context context
     * @param file file
     * @return strings for a file in /data/data/package/filename
     */
    public static String read(Context context, String file)
    {
        String data = "";
        try
        {
            FileInputStream stream = context.openFileInput(file);
            StringBuffer sb = new StringBuffer();
            int c = stream.read();
            while (c != -1)
            {
                sb.append((char) c);
                c = stream.read();
            }
            stream.close();
            data = sb.toString();

        }
        catch (FileNotFoundException e)
        {
            Logger.e(TAG,
                    e.getMessage());
        }
        catch (IOException e)
        {
            Logger.e(TAG,
                    e.getMessage());
        }
        return data;
    }

    /**
     * write strings to a file in /data/data/package/filename
     * 
     * @param context context
     * @param file file
     * @param msg msg
     */
    @SuppressLint("WorldWriteableFiles")
    public static void write(Context context, String file, String msg)
    {
        try
        {
            FileOutputStream stream = context.openFileOutput(file,
                    Context.MODE_WORLD_WRITEABLE);
            stream.write(msg.getBytes());
            stream.flush();
            stream.close();
        }
        catch (FileNotFoundException e)
        {
            Logger.e(TAG,
                    e.getMessage());
        }
        catch (IOException e)
        {
            Logger.e(TAG,
                    e.getMessage());
        }
    }

}
