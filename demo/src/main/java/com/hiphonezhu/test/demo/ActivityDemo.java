package com.hiphonezhu.test.demo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.baseline.framework.logic.InfoResult;
import com.android.baseline.framework.logic.net.IProgress;
import com.android.baseline.framework.task.TaskExecutor;

import common.MyBaseActivity;

/**
 * 网络请求测试
 *
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2016/03/09 15:01]
 */
public class ActivityDemo extends MyBaseActivity {
    private XLogic moduleLogic;

    private int n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        moduleLogic = registLogic(new XLogic(this));

        // 网络请求
        findViewById(R.id.net_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress("handling...");
                moduleLogic.getResult("18068440586");
            }
        });

        // 本地任务
        findViewById(R.id.task_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress("handling...");
                TaskExecutor.getInstance().execute(registTask(new ModuleTask(R.id.testTask, ActivityDemo.this)));
            }
        });

        findViewById(R.id.download_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress("handling...");

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
            }
        });

        findViewById(R.id.upload_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress("handling...");
                moduleLogic.upload("admin", Environment.getExternalStorageDirectory() + "/file1.jpeg");
            }
        });

        findViewById(R.id.batchUpload_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress("handling...");
                moduleLogic.batchUpload("admin", Environment.getExternalStorageDirectory() + "/file1.jpeg", Environment.getExternalStorageDirectory() + "/file2.jpeg");
            }
        });

        findViewById(R.id.uploadProgress_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress("handling...");
                moduleLogic.uploadWithProgress("admin", Environment.getExternalStorageDirectory() + "/file1.jpeg", new IProgress() {
                    @Override
                    public void onProgress(long current, long total) {
                        Log.e("onProgress", "current: " + current + ", total: " + total);
                    }
                });
            }
        });
    }

    @Override
    protected void onCreateCustomToolBar(Toolbar toolbar) {
        // use custom Title Layout
        // super.onCreateCustomToolBar(toolbar);

        // use ToolBar
        toolbar.setTitle("标题");
        toolbar.setLogo(R.drawable.ic_launcher);
    }

    @Override
    protected boolean defaultTitleBarVisible() {
        return true;
    }

    @Override
    public void onResponse(Message msg) {
        super.onResponse(msg);
        switch (msg.what) {
            case R.id.mobilenumber:
                hideProgress();
                if (checkResponse(msg)) {
                    InfoResult phoneResult = (InfoResult) msg.obj;
                    showToast(phoneResult.toString());
                }
                break;
            case R.id.testTask:
                hideProgress();
                if (checkResponse(msg)) {
                    InfoResult infoResult = (InfoResult) msg.obj;
                    showToast(infoResult.toString());
                }
                break;
            case R.id.download:
                hideProgress();
                if (checkResponse(msg)) {
                    InfoResult infoResult = (InfoResult) msg.obj;
                    showToast(infoResult.toString());
                }
                break;
            case R.id.upload:
                hideProgress();
                UploadResult result = (UploadResult) msg.obj;
                showToast(result.toString());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
