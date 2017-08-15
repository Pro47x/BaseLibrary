package com.minicart.android.baselibrary.dagger2.module

import android.text.TextUtils
import com.minicart.android.baselibrary.net.GlobalHttpHandler
import com.minicart.android.baselibrary.net.http.secret.ISecret
import dagger.Module
import dagger.Provides
import okhttp3.HttpUrl
import javax.inject.Singleton

/**
 * Created by jessyan on 2016/3/14.
 */
@Module
class GlobalConfigModule private constructor(builder: Builder) {
    private val mBaseUrl: HttpUrl?
    private val mHandler: GlobalHttpHandler?
    private val mISecret: ISecret?

    init {
        this.mBaseUrl = builder.baseUrl
        this.mHandler = builder.handler
        this.mISecret = builder.ISecret
    }

    @Singleton
    @Provides
    internal fun provideHttpUrl(): HttpUrl {
        if (mBaseUrl == null) {
            throw IllegalArgumentException("baseUrl can not Null!!!")
        }
        return mBaseUrl
    }

    @Singleton
    @Provides
    internal fun provideGlobalHttpHandler(): GlobalHttpHandler? {
        return mHandler//处理Http请求和响应结果
    }

    @Singleton
    @Provides
    internal fun provideISecret(): ISecret? {
        return mISecret
    }

    class Builder internal constructor() {
        internal var baseUrl: HttpUrl? = null
        internal var handler: GlobalHttpHandler? = null
        internal var ISecret: ISecret? = null

        fun setBaseUrl(baseUrl: String): Builder {
            if (TextUtils.isEmpty(baseUrl)) {
                throw IllegalArgumentException("baseUrl can not null!!!")
            }
            this.baseUrl = HttpUrl.parse(baseUrl)
            return this
        }

        fun globalHttpHandler(handler: GlobalHttpHandler): Builder {//用来处理http响应结果
            this.handler = handler
            return this
        }

        fun setSecret(secret: ISecret): Builder {//用来处理http响应结果
            this.ISecret = secret
            return this
        }

        fun build(): GlobalConfigModule {
            return GlobalConfigModule(this)
        }
    }

    companion object {

        fun builder(): Builder {
            return Builder()
        }
    }


}
