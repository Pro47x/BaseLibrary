package com.minicart.android.baselibrary.base;

import android.widget.BaseExpandableListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @类名：MyBaseExpandableListAdapter
 * @描述：
 * @创建人：54506
 * @创建时间：2017/2/17 17:32
 * @版本：
 */

public abstract class MyBaseExpandableListAdapter<D> extends BaseExpandableListAdapter {
    private ArrayList<D> data;

    public void setData(List<D> data) {
        if (this.data == null) {
            this.data = new ArrayList<>();
        } else {
            this.data.clear();
        }
        if (data != null) {
            this.data.addAll(data);
        }
        notifyDataSetChanged();
    }

    public ArrayList<D> getData() {
        return data;
    }

    @Override
    public int getGroupCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public D getGroup(int groupPosition) {
        if (data == null || data.isEmpty()) {
            return null;
        }
        return data.get(groupPosition);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
