package com.minicart.android.baselibrary.dagger2.module;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.minicart.android.baselibrary.net.GlobalHttpHandler;
import com.minicart.android.baselibrary.net.http.secret.ISecret;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;

/**
 * Created by jessyan on 2016/3/14.
 */
@Module
public class GlobalConfigModule {
    private HttpUrl mBaseUrl;
    private GlobalHttpHandler mHandler;
    private ISecret mISecret;

    private GlobalConfigModule(Builder builder) {
        this.mBaseUrl = builder.baseUrl;
        this.mHandler = builder.handler;
        this.mISecret = builder.ISecret;
    }

    @Singleton
    @Provides
    HttpUrl provideHttpUrl() {
        if (mBaseUrl == null) {
            throw new IllegalArgumentException("baseUrl can not Null!!!");
        }
        return mBaseUrl;
    }

    @Singleton
    @Provides
    @Nullable
    GlobalHttpHandler provideGlobalHttpHandler() {
        return mHandler;//处理Http请求和响应结果
    }

    @Singleton
    @Provides
    ISecret provideISecret() {
        return mISecret;//处理Http请求和响应结果
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private HttpUrl baseUrl;
        private GlobalHttpHandler handler;
        private ISecret ISecret;

        private Builder() {
        }

        public Builder setBaseUrl(String baseUrl) {
            if (TextUtils.isEmpty(baseUrl)) {
                throw new IllegalArgumentException("baseUrl can not null!!!");
            }
            this.baseUrl = HttpUrl.parse(baseUrl);
            return this;
        }

        public Builder globalHttpHandler(GlobalHttpHandler handler) {//用来处理http响应结果
            this.handler = handler;
            return this;
        }

        public Builder setSecret(ISecret secret) {//用来处理http响应结果
            this.ISecret = secret;
            return this;
        }

        public GlobalConfigModule build() {
            return new GlobalConfigModule(this);
        }
    }


}
