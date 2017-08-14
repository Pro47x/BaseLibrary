package com.minicart.android.baselibrary.dagger2.config;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;


public final class MCManifestParser {

    private final Application context;

    public MCManifestParser(Application context) {
        this.context = context;
    }

    public Application getContext() {
        return context;
    }

    public <T> T parse(@NonNull String moduleValue) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                for (String key : appInfo.metaData.keySet()) {
                    if (moduleValue.equals(appInfo.metaData.get(key))) {
                        return parseModule(key);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Unable to find metadata to parse ConfigModule", e);
        }
        throw new NullPointerException("config can't null!!!");
    }

    private static <T> T parseModule(String className) throws ClassCastException {
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Unable to find implementation", e);
        }

        Object module;
        try {
            module = clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("Unable to instantiate implementation for " + clazz, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to instantiate implementation for " + clazz, e);
        }
        return (T) module;
    }

}