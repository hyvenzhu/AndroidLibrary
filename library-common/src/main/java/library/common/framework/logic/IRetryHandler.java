package library.common.framework.logic;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;

/**
 * 重新发射某个数据源：一般用来在令牌过期的情况下，重新申请令牌并继续上一个请求。
 * 适用于令牌通过拦截器统一添加在 head、url 等场景；不适用 post json body 携带令牌。
 *
 * @author zhuhf
 * @version 2019/5/14
 */
public interface IRetryHandler<O, N> {
    /**
     * 返回旧数据源O否要重试
     *
     * @param source
     * @return
     */
    boolean needRetry(@NonNull O source);

    /**
     * 返回新添加的数据源N是否成功
     *
     * @param result
     * @return
     */
    boolean isBeforeSourceSuccess(N result);

    /**
     * 返回在重试旧数据源O之前添加的新数据源N
     *
     * @return
     */
    @NonNull
    Observable<? extends N> beforeSource();
}
