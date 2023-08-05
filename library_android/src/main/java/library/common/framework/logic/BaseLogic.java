package library.common.framework.logic;

import androidx.lifecycle.ViewModel;

import library.common.framework.logic.net.RetrofitManager;
import retrofit2.Retrofit;

/**
 * @author hiphonezhu@gmail.com
 * @version [AndroidLibrary, 2018-3-6]
 */
public abstract class BaseLogic extends ViewModel {
    protected Retrofit retrofit;

    public BaseLogic() {
        retrofit = RetrofitManager.getInstance().getRetrofit(getBaseUrl());
    }

    /**
     * 重新读取配置
     */
    public void rebuildConfig() {
        retrofit = RetrofitManager.getInstance().getRetrofit(getBaseUrl());
    }

    /**
     * create api service
     *
     * @param service
     * @param <T>
     * @return
     */
    protected <T> T create(final Class<T> service) {
        return retrofit.create(service);
    }

    /**
     * API根地址
     *
     * @return
     */
    protected abstract String getBaseUrl();
}
