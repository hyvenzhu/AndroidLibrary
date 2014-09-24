package com.android.baseline.framework.log.util;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * 
 * [Collection of methods for operate memory]<BR>
 * 
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2013-1-28]
 */
public class MemoryStatus
{

    private static final int ERROR = -1;

    private static final long RESERVED_SIZE = 2097152;

    /**
     * byte size
     */
    private static final int BYTE_SIZE = 1024;

    /**
     * Offset length
     */
    private static final int OFFSET_LENGTH = 3;

    /**
     * 
     * [构造简要说明]
     */
    private MemoryStatus()
    {
    }

    /**
     * 
     * [一句话功能简述]<BR>
     * [功能详细描述]
     * 
     * @return is external memory available
     */
    public static boolean externalMemoryAvailable()
    {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 
     * [获取外部存储器可用的内存大小]<BR>
     * [功能详细描述]
     * 
     * @return available internal memory size
     * @author hiphonezhu@gmail.com
     */
    public static long getAvailableInternalMemorySize()
    {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    /**
     * 
     * [一句话功能简述]<BR>
     * [功能详细描述]
     * 
     * @return total internal memory size
     */
    public static long getTotalInternalMemorySize()
    {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    /**
     * 
     * [一句话功能简述]<BR>
     * [功能详细描述]
     * 
     * @return available external memory size
     */
    public static long getAvailableExternalMemorySize()
    {
        long memorySize = ERROR;
        if (externalMemoryAvailable())
        {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            memorySize = availableBlocks * blockSize;
        }
        return memorySize;
    }

    /**
     * 
     * [用于获取外部存储器总的内存大小]<BR>
     * 
     * @return 外部存储器总的内存大小
     * @author hiphonezhu@gmail.com
     */
    public static long getTotalExternalMemorySize()
    {
        long totalMeorySize = ERROR;
        if (externalMemoryAvailable())
        {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            totalMeorySize = totalBlocks * blockSize;
        }
        return totalMeorySize;
    }

    /**
     * 
     * [一句话功能简述]<BR>
     * [功能详细描述]
     * 
     * @param size size
     * @return formated string
     */
    public static String formatSize(long size)
    {
        String suffix = "B";

        if (size >= BYTE_SIZE)
        {
            suffix = "KiB";
            size /= BYTE_SIZE;
            if (size >= BYTE_SIZE)
            {
                suffix = "MiB";
                size /= BYTE_SIZE;
            }
        }

        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

        int commaOffset = resultBuffer.length() - OFFSET_LENGTH;
        while (commaOffset > 0)
        {
            resultBuffer.insert(commaOffset,
                    ',');
            commaOffset -= OFFSET_LENGTH;
        }

        if (suffix != null)
        {
            resultBuffer.append(suffix);
        }
        return resultBuffer.toString();
    }

    /**
     * 
     * [通过指定的文件大小去判断存储内存是否够用]<BR>
     * [功能详细描述]
     * 
     * @param size 指定文件大小
     * @return 外部存储器是否可用
     * @author hiphonezhu@gmail.com
     */
    public static boolean isExternalMemoryAvailable(long size)
    {
        long availableMemory = getAvailableExternalMemorySize();
        return !(size > availableMemory);
    }

    /**
     * 
     * [一句话功能简述]<BR>
     * [功能详细描述]
     * 
     * @param size size
     * @return is internal memory available
     */
    public static boolean isInternalMemoryAvailable(long size)
    {
        long availableMemory = getAvailableInternalMemorySize();
        return !(size > availableMemory);
    }

    /**
     * 
     * [一句话功能简述]<BR>
     * [功能详细描述]
     * 
     * @param size size
     * @return is memory available
     */
    public static boolean isMemoryAvailable(long size)
    {
        size += RESERVED_SIZE;
        boolean isAvailable = false;
        if (externalMemoryAvailable())
        {
            isAvailable = isExternalMemoryAvailable(size);
        }
        else
        {
            isAvailable = isInternalMemoryAvailable(size);
        }
        return isAvailable;
    }

    /**
     * 
     * [一句话功能简述]<BR>
     * [功能详细描述]
     * 
     * @param path path
     * @return avaliable specific memory size
     */
    public static long getSpecificMemoryAvaliable(String path)
    {
        StatFs stat = new StatFs(path);
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    /**
     * 
     * [一句话功能简述]<BR>
     * [功能详细描述]
     * 
     * @param size size
     * @param path path
     * @return is specific memory available
     */
    public static boolean isSpecificMemoryAvailable(long size, String path)
    {
        long availableMemory = getSpecificMemoryAvaliable(path);
        return !(size > availableMemory);
    }

}
