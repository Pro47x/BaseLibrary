package com.minicart.android.baselibrary.dagger2.delegate

import android.os.Bundle
import android.os.Parcelable

/**
 * Created by jess on 26/04/2017 20:23
 * Contact with jess.yan.effort@gmail.com
 */

interface ActivityDelegate : Parcelable {

    fun onCreate(savedInstanceState: Bundle)

    fun onStart()

    fun onResume()

    fun onPause()

    fun onStop()

    fun onSaveInstanceState(outState: Bundle)

    fun onDestroy()

    companion object {
        val ACTIVITY_DELEGATE = "activity_delegate"
    }
}
