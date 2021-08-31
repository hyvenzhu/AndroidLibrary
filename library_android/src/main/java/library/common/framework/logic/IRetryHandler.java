package library.common.framework.logic;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;

/**
 * 一般用来在令牌过期的情况下，重新申请令牌并继续上一个请求。
 * 适用于令牌通过拦截器统一添加在 head、url 等场景；不适用 post json body 携带令牌。
 *
 * @author zhuhf
 * @version 2019/5/14
 */
public interface IRetryHandler<S, D> {
    /**
     * 返回本次结果是否要重试
     *
     * @param source
     * @return
     */
    boolean needRetry(@NonNull S source);

    /**
     * 返回依赖的请求是否成功
     *
     * @param dependsOn
     * @return
     */
    boolean dependsOnSuccess(D dependsOn);

    /**
     * 返回所依赖的请求
     *
     * @return
     */
    @NonNull
    Observable<? extends D> dependsOn();
}
