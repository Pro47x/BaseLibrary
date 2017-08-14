package com.minicart.android.baselibrary.net.http.engine;


public interface IHttpEngine {
    void injectRetrofitService(Class<?>... services);

    <T> T obtainRetrofitService(Class<T> service);
}