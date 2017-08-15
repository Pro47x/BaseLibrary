package com.minicart.android.baselibrary.dagger2.delegate

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.View

/**
 * Created by jess on 29/04/2017 14:30
 * Contact with jess.yan.effort@gmail.com
 */

interface FragmentDelegate : Parcelable {

    fun onAttach(context: Context)

    fun onCreate(savedInstanceState: Bundle)

    fun onCreateView(view: View, savedInstanceState: Bundle)

    fun onActivityCreate(savedInstanceState: Bundle)

    fun onStart()

    fun onResume()

    fun onPause()

    fun onStop()

    fun onSaveInstanceState(outState: Bundle)

    fun onDestroyView()

    fun onDestroy()

    fun onDetach()

    /**
     * Return true if the fragment is currently added to its activity.
     */
    val isAdded: Boolean

    companion object {

        val FRAGMENT_DELEGATE = "fragment_delegate"
    }
}
