package com.minicart.android.baselibrary.dagger2.component

/**
 * @author 54506
 * *
 * @version 1
 * *
 * @createTime：2017/7/12 12:09
 */

interface MVPComponent<Target> {
    fun inject(target: Target)
}
