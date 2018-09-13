package library.common.framework.logic.net;

/**
 * 文件下载进度
 *
 * @author hiphonezhu@gmail.com
 * @version [AndroidLibrary, 2018-3-6]
 */
public interface IProgress {
    /**
     * 进度
     *
     * @param current 已下载
     * @param total   总共大小
     */
    void onProgress(long current, long total);
}
