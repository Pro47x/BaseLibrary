package com.minicart.android.baselibrary.base

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View

import butterknife.ButterKnife

/**
 * Created by Pro47x on 2017/1/13.
 */

class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    init {
        ButterKnife.bind(this, itemView)
    }

    val context: Context
        get() = itemView.context
}
