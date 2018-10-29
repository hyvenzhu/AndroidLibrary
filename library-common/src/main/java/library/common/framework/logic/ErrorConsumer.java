package library.common.framework.logic;

/**
 * @author zhuhf
 * @version [AndroidLibrary, 2018-04-20]
 */
public interface ErrorConsumer<T> {
    T onError(Throwable throwable);
}
