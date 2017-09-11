package com.minicart.android.baselibrary.support

import android.app.Activity
import android.app.Application
import java.util.*

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
class AppManager(private val mApplication: Application) {
    private val activityStack: Stack<Activity> = Stack()

    //当前在前台的activity
    var currentActivity: Activity? = null

    fun pullActivity(activity: Activity) {
        activityStack.add(activity)
    }

    fun removeActivity(activity: Activity): Boolean = activityStack.remove(activity)

    fun currentActivity(): Activity? = currentActivity

    fun isTopActivity(activity: Activity): Boolean = activity == currentActivity

    fun destroyActivity(clazz: Class<*>): Boolean {
        for (activity in activityStack) {
            if (activity.javaClass.name == clazz.name) {
                activity.finish()
                return true
            }
        }
        return false
    }

    fun destroyAllActivity(): Boolean {
        for (activity in activityStack) {
            activity.finish()
        }
        return true
    }

}