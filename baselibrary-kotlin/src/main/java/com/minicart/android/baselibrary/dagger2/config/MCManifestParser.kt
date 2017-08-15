package com.minicart.android.baselibrary.dagger2.config

import android.app.Application
import android.content.pm.PackageManager


class MCManifestParser(val context: Application) {

    fun <T> parse(moduleValue: String): T {
        try {
            val appInfo = context.packageManager.getApplicationInfo(
                    context.packageName, PackageManager.GET_META_DATA)
            if (appInfo.metaData != null) {
                appInfo.metaData.keySet()
                        .filter { moduleValue == appInfo.metaData.get(it) }
                        .forEach { return parseModule(it) }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            throw RuntimeException("Unable to find metadata to parse ConfigModule", e)
        }

        throw NullPointerException("config can't null!!!")
    }

    @Throws(ClassCastException::class)
    private fun <T> parseModule(className: String): T {
        val clazz: Class<*>
        try {
            clazz = Class.forName(className)
        } catch (e: ClassNotFoundException) {
            throw IllegalArgumentException("Unable to find implementation", e)
        }

        val module: Any
        try {
            module = clazz.newInstance()
        } catch (e: InstantiationException) {
            throw RuntimeException("Unable to instantiate implementation for " + clazz, e)
        } catch (e: IllegalAccessException) {
            throw RuntimeException("Unable to instantiate implementation for " + clazz, e)
        }

        return module as T
    }

}