package com.minicart.android.baselibrary.support;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.util.Stack;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * _____________#########_______________________
 * ____________############_____________________
 * ____________#############____________________
 * ___________##__###########___________________
 * __________###__######_#####__________________
 * __________###_#######___####_________________
 * _________###__##########_####________________
 * ________####__###########_####_______________
 * ______#####___###########__#####_____________
 * _____######___###_########___#####___________
 * _____#####___###___########___######_________
 * ____######___###__###########___######_______
 * ___######___####_##############__######______
 * __#######__#####################_#######_____
 * __#######__##############################____
 * _#######__######_#################_#######___
 * _#######__######_######_#########___######___
 * _#######____##__######___######_____######___
 * _#######________######____#####_____#####____
 * __######________#####_____#####_____####_____
 * ___#####________####______#####_____###______
 * ____#####______;###________###______#________
 * ______##_______####________####______________
 *
 * @类名：ActivityUtil
 * @描述：
 * @创建人：54506
 * @创建时间：2016/8/3
 * @版本：
 */
@Singleton
public class AppManager {
    private Application mApplication;
    private Stack<Activity> activityStack = new Stack<>();

    //当前在前台的activity
    private Activity mCurrentActivity;

    @Inject
    public AppManager(Application application) {
        this.mApplication = application;
    }

    public void pullActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    public boolean removeActivity(Activity activity) {
        return activityStack != null && activityStack.remove(activity);
    }

    public Activity currentActivity() {
        if (activityStack != null && !activityStack.isEmpty()) {
            return activityStack.lastElement();
        }
        return null;
    }

    public boolean isTopActivity(Class clazz) {
        if (activityStack != null) {
            return activityStack.lastElement().getClass().getName().equals(clazz.getName());
        }
        return false;
    }

    public boolean destroyActivity(Class clazz) {
        if (activityStack != null) {
            for (Activity activity : activityStack) {
                if (activity.getClass().getName().equals(clazz.getName())) {
                    activity.finish();
                    return true;
                }
            }
        }
        return false;
    }

    public boolean destroyAllActivity() {
        if (activityStack != null) {
            for (Activity activity : activityStack) {
                activity.finish();
            }
            return true;
        }
        return false;
    }

    public void addFragmentToActivity(FragmentActivity activity, Fragment fragment, int containerViewId) {
        activity.getSupportFragmentManager().beginTransaction().add(containerViewId, fragment).commit();
    }

    public Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    public void setCurrentActivity(Activity currentActivity) {
        mCurrentActivity = currentActivity;
    }
}