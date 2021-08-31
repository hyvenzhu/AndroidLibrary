package library.common.framework.logic

/**
 * 一般用来在令牌过期的情况下，重新申请令牌并继续上一个请求。
 * 适用于令牌通过拦截器统一添加在 head、url 等场景；不适用 post json body 携带令牌。
 *
 * @author hyvenzhu
 * @version 2021-02-01
 */
interface IRetryHandlerKt<S, D> {
    /**
     * 返回本次结果是否要重试
     *
     * @param result
     * @return
     */
    fun needRetry(result: S): Boolean

    /**
     * 返回依赖的请求是否成功
     *
     * @param dependsOn
     * @return
     */
    fun dependsOnSuccess(dependsOn: D): Boolean

    /**
     * 返回所依赖的请求
     *
     * @return
     */
    suspend fun dependsOn(): D
}