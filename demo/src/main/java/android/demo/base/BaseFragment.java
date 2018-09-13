package android.demo.base;

import android.os.Message;

import library.common.framework.ui.activity.presenter.FragmentPresenter;
import library.common.framework.ui.activity.view.IDelegate;


/**
 * @author zhuhf
 * @version [AndroidLibrary, 2018-03-07]
 */
public abstract class BaseFragment<T extends IDelegate> extends FragmentPresenter<T> {
    @Override
    protected void onResponse(Message msg) {
        super.onResponse(msg);
        if (msg.obj instanceof InfoResult) {
            InfoResult infoResult = (InfoResult) msg.obj;
            if (infoResult.isSuccess()) {
                onSuccess(msg.what, infoResult.getData(), infoResult.getCode());
            } else {
                onFailure(msg.what, infoResult.getData(), infoResult.getCode(), infoResult.getErrmsg());
            }
        } else {
            onFailure(msg.what, msg.obj, null, NetworkError.errorMsg(getActivity(), msg.obj));
        }
    }
    
    /**
     * 成功响应
     *
     * @param requestId    请求Id
     * @param response     响应结果
     * @param responseCode 响应码
     */
    protected void onSuccess(int requestId, Object response, String responseCode) {
    }
    
    /**
     * 失败响应
     *
     * @param requestId    请求Id
     * @param response     响应结果
     * @param responseCode 响应码
     * @param errmsg       错误信息
     */
    protected void onFailure(int requestId, Object response, String responseCode, String errmsg) {
    }
}
