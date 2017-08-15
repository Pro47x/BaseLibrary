package com.minicart.android.baselibrary.dagger2.module

import android.app.Application
import android.support.v4.util.ArrayMap
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val mApplication: Application) {

    @Singleton
    @Provides
    fun provideApplication(): Application {
        return mApplication
    }

    @Singleton
    @Provides
    fun provideGson(application: Application): Gson {
        return GsonBuilder().create()
    }

    @Singleton
    @Provides
    fun provideExtras(): Map<String, Any> {
        return ArrayMap()
    }
}
