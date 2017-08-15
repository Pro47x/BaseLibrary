package com.minicart.android.baselibrary.base

import android.widget.BaseExpandableListAdapter

import java.util.ArrayList

/**
 * @类名：MyBaseExpandableListAdapter
 * *
 * @描述：
 * *
 * @创建人：54506
 * *
 * @创建时间：2017/2/17 17:32
 * *
 * @版本：
 */

abstract class MyBaseExpandableListAdapter<D> : BaseExpandableListAdapter() {
    var data: ArrayList<D>? = null
        private set

    fun setData(data: List<D>?) {
        if (this.data == null) {
            this.data = ArrayList<D>()
        } else {
            this.data!!.clear()
        }
        if (data != null) {
            this.data!!.addAll(data)
        }
        notifyDataSetChanged()
    }

    override fun getGroupCount(): Int {
        return if (data == null) 0 else data!!.size
    }

    override fun getGroup(groupPosition: Int): D? {
        if (data == null || data!!.isEmpty()) {
            return null
        }
        return data!![groupPosition]
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return false
    }
}
