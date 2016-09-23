package com.hiphonezhu.test.demo;

import com.android.baseline.framework.logic.BaseLogic;
import com.android.baseline.framework.logic.InfoResult;
import com.android.baseline.framework.logic.net.IProgress;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * 业务模块
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 16/9/3 12:12]
 */
public class XLogic extends BaseLogic {
    XAPI phoneService;
    public XLogic(Object subscriber) {
        super(subscriber);
        phoneService = create(XAPI.class);
    }

    public void getResult(String phone)
    {
        sendRequest(phoneService.getResult(phone, "8e13586b86e4b7f3758ba3bd6c9c9135").doOnNext(new Action1<InfoResult<MobileBean>>() {
            @Override
            public void call(InfoResult<MobileBean> mobileBeanInfoResult) {
                // do anything you want before call onNext
            }
        }), R.id.mobilenumber);
    }

    /**
     * 下载文件
     * @param downloadUrl 下载路径
     * @param destFilePath 本地存储路径
     * @param iProgress 进度
     * @param extraInfo 附加信息, 一般用于结束之后判断数据源
     */
    public void download(String downloadUrl, final String destFilePath, final IProgress iProgress, final Object extraInfo)
    {
        // ResponseBody下载成功之后, 使用map操作, 最后输出InfoResult
        sendRequest(phoneService.download(downloadUrl).map(new Func1<ResponseBody, InfoResult>() {
            @Override
            public InfoResult call(ResponseBody responseBody) {
                InfoResult infoResult = new InfoResult();
                infoResult.setExtraObj(extraInfo);

                InputStream is = responseBody.byteStream();
                FileOutputStream fos = null;
                long total = responseBody.contentLength();
                long current = 0;
                try {
                    fos = new FileOutputStream(destFilePath);
                    byte[] buffer = new byte[10240];
                    int len = -1;
                    while((len = is.read(buffer)) != -1)
                    {
                        fos.write(buffer, 0, len);
                        current += len;
                        iProgress.onProgress(current, total);
                    }
                    fos.flush();

                    // download success
                    infoResult.setErrorCode(InfoResult.SUCCESS_CODE);
                    infoResult.setDesc("下载成功");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    infoResult.setDesc("FileNotFoundException: " + destFilePath);
                } catch (IOException e) {
                    e.printStackTrace();
                    infoResult.setDesc("IOException");
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return infoResult;
            }
        }), R.id.download);
    }

    @Override
    public String getBaseUrl() {
        return "http://apis.baidu.com/";
    }
}
