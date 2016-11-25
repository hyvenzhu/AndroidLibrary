package com.android.baseline.framework.router;

import android.content.Context;
import android.support.v4.app.Fragment;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Router for Android
 *
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 16/10/21 11:37]
 */
public final class LiteRouter {
    private Interceptor interceptor;
    private final Map<Method, IntentWrapper> serviceMethodCache = new LinkedHashMap<>();

    LiteRouter(Interceptor interceptor) {
        this.interceptor = interceptor;
    }

    /**
     * create router class service for Activity
     *
     * @param service router class
     * @param context from context
     * @param <T>
     * @return
     */
    public <T> T create(final Class<T> service, final Context context) {
        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[]{service},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object... args)
                            throws Throwable {
                        // cache method
                        IntentWrapper intentWrapper;
                        synchronized (serviceMethodCache) {
                            intentWrapper = serviceMethodCache.get(method);
                            if (intentWrapper == null) {
                                intentWrapper = new IntentWrapper.Builder(context, method).build();
                                serviceMethodCache.put(method, intentWrapper);
                            }
                        }
                        IntentCall intentCall = new IntentCall(intentWrapper, args);
                        return intentCall.call(interceptor);
                    }
                });
    }

    /**
     * create router class service for Fragment
     *
     * @param service router class
     * @param fragment from fragment
     * @param <T>
     * @return
     */
    public <T> T create(final Class<T> service, final Fragment fragment) {
        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[]{service},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object... args)
                            throws Throwable {
                        // cache method
                        IntentWrapper intentWrapper;
                        synchronized (serviceMethodCache) {
                            intentWrapper = serviceMethodCache.get(method);
                            if (intentWrapper == null) {
                                intentWrapper = new IntentWrapper.Builder(fragment, method).build();
                                serviceMethodCache.put(method, intentWrapper);
                            }
                        }
                        IntentCall intentCall = new IntentCall(intentWrapper, args);
                        return intentCall.call(interceptor);
                    }
                });
    }

    public static final class Builder {
        private Interceptor interceptor;

        public Builder interceptor(Interceptor interceptor) {
            this.interceptor = interceptor;
            return this;
        }

        public LiteRouter build() {
            return new LiteRouter(interceptor);
        }
    }
}
