package com.jcgroup.common.framework.logic;

/**
 * @author zhuhf
 * @version [JCLauncher, 2018-04-20]
 */
public interface ErrorConsumer<T> {
    T onError(Throwable throwable);
}
