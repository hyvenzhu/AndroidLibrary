package library.common.util;

/**
 * @author zhuhf
 * @version [AndroidLibrary, 2018-03-14]
 */
public interface Callback<T> {
    /**
     * call the observer
     *
     * @param data
     */
    void call(T data);
}
