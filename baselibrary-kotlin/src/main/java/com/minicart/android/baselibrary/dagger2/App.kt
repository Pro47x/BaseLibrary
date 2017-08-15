package com.minicart.android.baselibrary.dagger2


import com.minicart.android.baselibrary.dagger2.component.AppComponent

interface App {

    fun getAppComponent(): AppComponent
}
