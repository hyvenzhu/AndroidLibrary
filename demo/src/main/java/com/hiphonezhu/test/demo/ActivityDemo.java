package com.hiphonezhu.test.demo;

import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.android.baseline.framework.logic.InfoResult;
import com.android.baseline.framework.logic.net.IProgress;
import com.android.baseline.framework.task.TaskExecutor;
import com.android.baseline.framework.ui.activity.presenter.ActivityPresenter;

/**
 * 网络请求测试
 *
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2016/03/09 15:01]
 */
public class ActivityDemo extends ActivityPresenter<ActivityDemoDelegate> {
    private XLogic moduleLogic;
    
    private int n;
    
    @Override
    protected void onCreate() {
        moduleLogic = findLogic(new XLogic(this));
        
        viewDelegate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.net_btn:
                        viewDelegate.showProgress("handling...", true);
                        moduleLogic.getResult("18068440586");
                        break;
                    case R.id.task_btn:
                        viewDelegate.showProgress("handling...", true);
                        TaskExecutor.getInstance().execute(findTask(new ModuleTask(R.id.testTask, ActivityDemo.this)));
                        break;
                    case R.id.download_btn:
                        viewDelegate.showProgress("handling...", true);
                        
                        String filePath;
                        n++;
                        int r = n % 2;
                        if (r == 0) {
                            filePath = Environment.getExternalStorageDirectory() + "/file1.jpeg";
                        } else {
                            filePath = Environment.getExternalStorageDirectory() + "/file2.jpeg";
                        }
                        
                        moduleLogic.download("http://photocdn.sohu.com/20160923/Img468996929.jpeg"
                                , filePath
                                , new IProgress() {
                                    @Override
                                    public void onProgress(long current, long total) {
                                        Log.e("onProgress", "current: " + current + ", total: " + total);
                                    }
                                }, null);
                        break;
                    case R.id.upload_btn:
                        viewDelegate.showProgress("handling...", true);
                        moduleLogic.upload("admin", Environment.getExternalStorageDirectory() + "/file1.jpeg");
                        break;
                    case R.id.batchUpload_btn:
                        viewDelegate.showProgress("handling...", true);
                        moduleLogic.batchUpload("admin", Environment.getExternalStorageDirectory() + "/file1.jpeg", Environment.getExternalStorageDirectory() + "/file2.jpeg");
                        break;
                    case R.id.uploadProgress_btn:
                        viewDelegate.showProgress("handling...", true);
                        moduleLogic.uploadWithProgress("admin", Environment.getExternalStorageDirectory() + "/file1.jpeg", new IProgress() {
                            @Override
                            public void onProgress(long current, long total) {
                                Log.e("onProgress", "current: " + current + ", total: " + total);
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        }, R.id.net_btn, R.id.task_btn, R.id.download_btn, R.id.upload_btn, R.id.batchUpload_btn, R.id.uploadProgress_btn);
    
        viewDelegate.showLoadView();
        viewDelegate.getRootView().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (viewDelegate != null) {
                    viewDelegate.showLoadError("错误提示", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewDelegate.hideLoadView();
                        }
                    });
                }
            }
        }, 3000);
    }
    
    @Override
    public void onResponse(Message msg) {
        super.onResponse(msg);
        switch (msg.what) {
            case R.id.mobilenumber:
                viewDelegate.hideProgress();
                InfoResult infoResult = (InfoResult) msg.obj;
                viewDelegate.showToast(infoResult.toString());
                break;
            case R.id.testTask:
                viewDelegate.hideProgress();
                InfoResult infoResult2 = (InfoResult) msg.obj;
                viewDelegate.showToast(infoResult2.toString());
                break;
            case R.id.download:
                viewDelegate.hideProgress();
                InfoResult infoResult3 = (InfoResult) msg.obj;
                viewDelegate.showToast(infoResult3.toString());
                break;
            case R.id.upload:
                viewDelegate.hideProgress();
                if (msg.obj instanceof UploadResult) {
                    UploadResult result = (UploadResult) msg.obj;
                    viewDelegate.showToast(result.toString());
                } else {
                    InfoResult infoResult4 = (InfoResult) msg.obj;
                    viewDelegate.showToast(infoResult4.toString());
                }
                break;
            default:
                break;
        }
    }
    
    @Override
    protected Class<ActivityDemoDelegate> getDelegateClass() {
        return ActivityDemoDelegate.class;
    }
}
