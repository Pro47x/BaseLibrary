/*
 *    Copyright (C) 2015 Haruki Hasegawa
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.minicart.android.baselibrary.ui.recyclerview.expandable;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.minicart.android.baselibrary.ui.recyclerview.adapter.ItemIdComposer;
import com.minicart.android.baselibrary.ui.recyclerview.adapter.ItemViewTypeComposer;
import com.minicart.android.baselibrary.ui.recyclerview.adapter.SimpleWrapperAdapter;
import com.minicart.android.baselibrary.ui.recyclerview.utils.WrapperAdapterUtils;

import java.util.List;

class ExpandableRecyclerViewWrapperAdapter
        extends SimpleWrapperAdapter<RecyclerView.ViewHolder> {

    private static final String TAG = "ARVExpandableWrapper";

    //
    // NOTE: Make accessible with short name
    private interface Constants extends ExpandableItemConstants {
    }

    private static final int VIEW_TYPE_FLAG_IS_GROUP = ItemViewTypeComposer.BIT_MASK_EXPANDABLE_FLAG;

    private static final int STATE_FLAG_INITIAL_VALUE = -1;

    private ExpandableItemAdapter mExpandableItemAdapter;
    private RecyclerViewExpandableItemManager mExpandableListManager;
    private ExpandablePositionTranslator mPositionTranslator;

    private RecyclerViewExpandableItemManager.OnGroupExpandListener mOnGroupExpandListener;
    private RecyclerViewExpandableItemManager.OnGroupCollapseListener mOnGroupCollapseListener;

    public ExpandableRecyclerViewWrapperAdapter(RecyclerViewExpandableItemManager manager, RecyclerView.Adapter<RecyclerView.ViewHolder> adapter, long[] expandedItemsSavedState) {
        super(adapter);

        mExpandableItemAdapter = getExpandableItemAdapter(adapter);
        if (mExpandableItemAdapter == null) {
            throw new IllegalArgumentException("adapter does not implement ExpandableItemAdapter");
        }

        if (manager == null) {
            throw new IllegalArgumentException("manager cannot be null");
        }

        mExpandableListManager = manager;

        mPositionTranslator = new ExpandablePositionTranslator();
        mPositionTranslator.build(
                mExpandableItemAdapter,
                ExpandablePositionTranslator.BUILD_OPTION_DEFAULT,
                mExpandableListManager.getDefaultGroupsExpandedState());

        if (expandedItemsSavedState != null) {
            // NOTE: do not call hook routines and listener methods
            mPositionTranslator.restoreExpandedGroupItems(expandedItemsSavedState, null, null, null);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager manager = (GridLayoutManager) layoutManager;
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return isGroup(position) ? manager.getSpanCount() : 1;
                }
            });
        }
    }

    @Override
    protected void onRelease() {
        super.onRelease();

        mExpandableItemAdapter = null;
        mExpandableListManager = null;
        mOnGroupExpandListener = null;
        mOnGroupCollapseListener = null;
    }

    @Override
    public int getItemCount() {
        return mPositionTranslator.getItemCount();
    }

    @Override
    public long getItemId(int position) {
        if (mExpandableItemAdapter == null) {
            return RecyclerView.NO_ID;
        }

        final long expandablePosition = mPositionTranslator.getExpandablePosition(position);
        final int groupPosition = ExpandableAdapterHelper.getPackedPositionGroup(expandablePosition);
        final int childPosition = ExpandableAdapterHelper.getPackedPositionChild(expandablePosition);

        if (childPosition == RecyclerView.NO_POSITION) {
            final long groupId = mExpandableItemAdapter.getGroupId(groupPosition);
            return ItemIdComposer.composeExpandableGroupId(groupId);
        } else {
            final long groupId = mExpandableItemAdapter.getGroupId(groupPosition);
            final long childId = mExpandableItemAdapter.getChildId(groupPosition, childPosition);
            return ItemIdComposer.composeExpandableChildId(groupId, childId);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mExpandableItemAdapter == null) {
            return 0;
        }

        final long expandablePosition = mPositionTranslator.getExpandablePosition(position);
        final int groupPosition = ExpandableAdapterHelper.getPackedPositionGroup(expandablePosition);
        final int childPosition = ExpandableAdapterHelper.getPackedPositionChild(expandablePosition);

        final int type;

        if (childPosition == RecyclerView.NO_POSITION) {
            type = mExpandableItemAdapter.getGroupItemViewType(groupPosition);
        } else {
            type = mExpandableItemAdapter.getChildItemViewType(groupPosition, childPosition);
        }

        if ((type & VIEW_TYPE_FLAG_IS_GROUP) != 0) {
            throw new IllegalStateException("Illegal view type (type = " + Integer.toHexString(type) + ")");
        }

        return (childPosition == RecyclerView.NO_POSITION) ? (type | VIEW_TYPE_FLAG_IS_GROUP) : (type);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mExpandableItemAdapter == null) {
            return null;
        }

        final int maskedViewType = (viewType & (~VIEW_TYPE_FLAG_IS_GROUP));

        final RecyclerView.ViewHolder holder;

        if ((viewType & VIEW_TYPE_FLAG_IS_GROUP) != 0) {
            holder = mExpandableItemAdapter.onCreateGroupViewHolder(parent, maskedViewType);
        } else {
            holder = mExpandableItemAdapter.onCreateChildViewHolder(parent, maskedViewType);
        }

        if (holder instanceof ExpandableItemViewHolder) {
            ((ExpandableItemViewHolder) holder).setExpandStateFlags(STATE_FLAG_INITIAL_VALUE);
        }

        return holder;

    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if (mExpandableItemAdapter == null) {
            return;
        }

        final long expandablePosition = mPositionTranslator.getExpandablePosition(position);
        final int groupPosition = ExpandableAdapterHelper.getPackedPositionGroup(expandablePosition);
        final int childPosition = ExpandableAdapterHelper.getPackedPositionChild(expandablePosition);
        final int viewType = (holder.getItemViewType() & (~VIEW_TYPE_FLAG_IS_GROUP));

        // update flags
        int flags = 0;

        if (childPosition == RecyclerView.NO_POSITION) {
            flags |= Constants.STATE_FLAG_IS_GROUP;
        } else {
            flags |= Constants.STATE_FLAG_IS_CHILD;
        }

        if (mPositionTranslator.isGroupExpanded(groupPosition)) {
            flags |= Constants.STATE_FLAG_IS_EXPANDED;
        }

        safeUpdateExpandStateFlags(holder, flags);

        if (childPosition == RecyclerView.NO_POSITION) {
            mExpandableItemAdapter.onBindGroupViewHolder(holder, groupPosition, viewType, payloads);
        } else {
            mExpandableItemAdapter.onBindChildViewHolder(holder, groupPosition, childPosition, viewType, payloads);
        }
    }

    boolean isGroup(int position) {
        final long expandablePosition = mPositionTranslator.getExpandablePosition(position);
        return ExpandableAdapterHelper.getPackedPositionChild(expandablePosition) == RecyclerView.NO_POSITION;
    }

    private void rebuildPositionTranslator() {
        if (mPositionTranslator != null) {
            long[] savedState = mPositionTranslator.getSavedStateArray();
            mPositionTranslator.build(
                    mExpandableItemAdapter,
                    ExpandablePositionTranslator.BUILD_OPTION_DEFAULT,
                    mExpandableListManager.getDefaultGroupsExpandedState());

            // NOTE: do not call hook routines and listener methods
            mPositionTranslator.restoreExpandedGroupItems(savedState, null, null, null);
        }
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder, int viewType) {
        if (holder instanceof ExpandableItemViewHolder) {
            ((ExpandableItemViewHolder) holder).setExpandStateFlags(STATE_FLAG_INITIAL_VALUE);
        }

        super.onViewRecycled(holder, viewType);
    }

    @Override
    protected void onHandleWrappedAdapterChanged() {
        rebuildPositionTranslator();
        super.onHandleWrappedAdapterChanged();
    }

    @Override
    protected void onHandleWrappedAdapterItemRangeChanged(int positionStart, int itemCount) {
        super.onHandleWrappedAdapterItemRangeChanged(positionStart, itemCount);
    }

    @Override
    protected void onHandleWrappedAdapterItemRangeInserted(int positionStart, int itemCount) {
        rebuildPositionTranslator();
        super.onHandleWrappedAdapterItemRangeInserted(positionStart, itemCount);
    }

    @Override
    protected void onHandleWrappedAdapterItemRangeRemoved(int positionStart, int itemCount) {
        if (itemCount == 1) {
            final long expandablePosition = mPositionTranslator.getExpandablePosition(positionStart);
            final int groupPosition = ExpandableAdapterHelper.getPackedPositionGroup(expandablePosition);
            final int childPosition = ExpandableAdapterHelper.getPackedPositionChild(expandablePosition);

            if (childPosition == RecyclerView.NO_POSITION) {
                mPositionTranslator.removeGroupItem(groupPosition);
            } else {
                mPositionTranslator.removeChildItem(groupPosition, childPosition);
            }
        } else {
            rebuildPositionTranslator();
        }

        super.onHandleWrappedAdapterItemRangeRemoved(positionStart, itemCount);
    }

    @Override
    protected void onHandleWrappedAdapterRangeMoved(int fromPosition, int toPosition, int itemCount) {
        rebuildPositionTranslator();
        super.onHandleWrappedAdapterRangeMoved(fromPosition, toPosition, itemCount);
    }

    // NOTE: This method is called from RecyclerViewExpandableItemManager
    /*package*/
    @SuppressWarnings("unchecked")
    boolean onTapItem(RecyclerView.ViewHolder holder, int position, int x, int y) {
        if (mExpandableItemAdapter == null) {
            return false;
        }

        //noinspection UnnecessaryLocalVariable
        final int flatPosition = position;
        final long expandablePosition = mPositionTranslator.getExpandablePosition(flatPosition);
        final int groupPosition = ExpandableAdapterHelper.getPackedPositionGroup(expandablePosition);
        final int childPosition = ExpandableAdapterHelper.getPackedPositionChild(expandablePosition);

        if (childPosition != RecyclerView.NO_POSITION) {
            return false;
        }

        final boolean expand = !(mPositionTranslator.isGroupExpanded(groupPosition));

        boolean result = mExpandableItemAdapter.onCheckCanExpandOrCollapseGroup(holder, groupPosition, x, y, expand);

        if (!result) {
            return false;
        }

        if (expand) {
            expandGroup(groupPosition, true, null);
        } else {
            collapseGroup(groupPosition, true, null);
        }

        return true;
    }

    /*package*/ void expandAll() {
        if (!mPositionTranslator.isEmpty() && !mPositionTranslator.isAllExpanded()) {
            mPositionTranslator.build(
                    mExpandableItemAdapter,
                    ExpandablePositionTranslator.BUILD_OPTION_EXPANDED_ALL,
                    mExpandableListManager.getDefaultGroupsExpandedState());
            notifyDataSetChanged();
        }
    }

    /*package*/ void collapseAll() {
        if (!mPositionTranslator.isEmpty() && !mPositionTranslator.isAllCollapsed()) {
            mPositionTranslator.build(
                    mExpandableItemAdapter,
                    ExpandablePositionTranslator.BUILD_OPTION_COLLAPSED_ALL,
                    mExpandableListManager.getDefaultGroupsExpandedState());
            notifyDataSetChanged();
        }
    }

    /*package*/ boolean collapseGroup(int groupPosition, boolean fromUser, Object payload) {
        if (!mPositionTranslator.isGroupExpanded(groupPosition)) {
            return false;
        }

        // call hook method
        if (!mExpandableItemAdapter.onHookGroupCollapse(groupPosition, fromUser, payload)) {
            return false;
        }

        if (mPositionTranslator.collapseGroup(groupPosition)) {
            final long packedPosition = ExpandableAdapterHelper.getPackedPositionForGroup(groupPosition);
            final int flatPosition = mPositionTranslator.getFlatPosition(packedPosition);
            final int childCount = mPositionTranslator.getChildCount(groupPosition);

            notifyItemRangeRemoved(flatPosition + 1, childCount);
        }


        {
            final long packedPosition = ExpandableAdapterHelper.getPackedPositionForGroup(groupPosition);
            final int flatPosition = mPositionTranslator.getFlatPosition(packedPosition);

            notifyItemChanged(flatPosition, payload);
        }

        // raise onGroupCollapse() event
        if (mOnGroupCollapseListener != null) {
            mOnGroupCollapseListener.onGroupCollapse(groupPosition, fromUser, payload);
        }

        return true;
    }

    /*package*/ boolean expandGroup(int groupPosition, boolean fromUser, Object payload) {
        if (mPositionTranslator.isGroupExpanded(groupPosition)) {
            return false;
        }

        // call hook method
        if (!mExpandableItemAdapter.onHookGroupExpand(groupPosition, fromUser, payload)) {
            return false;
        }

        if (mPositionTranslator.expandGroup(groupPosition)) {
            final long packedPosition = ExpandableAdapterHelper.getPackedPositionForGroup(groupPosition);
            final int flatPosition = mPositionTranslator.getFlatPosition(packedPosition);
            final int childCount = mPositionTranslator.getChildCount(groupPosition);

            notifyItemRangeInserted(flatPosition + 1, childCount);
        }

        {
            final long packedPosition = ExpandableAdapterHelper.getPackedPositionForGroup(groupPosition);
            final int flatPosition = mPositionTranslator.getFlatPosition(packedPosition);

            notifyItemChanged(flatPosition, payload);
        }

        // raise onGroupExpand() event
        if (mOnGroupExpandListener != null) {
            mOnGroupExpandListener.onGroupExpand(groupPosition, fromUser, payload);
        }

        return true;
    }

    /*package*/ boolean isGroupExpanded(int groupPosition) {
        return mPositionTranslator.isGroupExpanded(groupPosition);
    }

    /*package*/ long getExpandablePosition(int flatPosition) {
        return mPositionTranslator.getExpandablePosition(flatPosition);
    }

    /*package*/ long getPackedPositionGroup(int flatPosition) {
        return ExpandableAdapterHelper.getPackedPositionGroup(getExpandablePosition(flatPosition));
    }

    /*package*/ long getPackedPositionChild(int flatPosition) {
        return ExpandableAdapterHelper.getPackedPositionChild(getExpandablePosition(flatPosition));
    }

    /*package*/ int getFlatPosition(long packedPosition) {
        return mPositionTranslator.getFlatPosition(packedPosition);
    }

    /*package*/ long[] getExpandedItemsSavedStateArray() {
        if (mPositionTranslator != null) {
            return mPositionTranslator.getSavedStateArray();
        } else {
            return null;
        }
    }

    /*package*/ void setOnGroupExpandListener(RecyclerViewExpandableItemManager.OnGroupExpandListener listener) {
        mOnGroupExpandListener = listener;
    }

    /*package*/ void setOnGroupCollapseListener(RecyclerViewExpandableItemManager.OnGroupCollapseListener listener) {
        mOnGroupCollapseListener = listener;
    }

    /*package*/ void restoreState(long[] adapterSavedState, boolean callHook, boolean callListeners) {
        mPositionTranslator.restoreExpandedGroupItems(
                adapterSavedState,
                (callHook ? mExpandableItemAdapter : null),
                (callListeners ? mOnGroupExpandListener : null),
                (callListeners ? mOnGroupCollapseListener : null));
    }

    /*package*/ void notifyGroupItemChanged(int groupPosition, Object payload) {
        final long packedPosition = ExpandableAdapterHelper.getPackedPositionForGroup(groupPosition);
        final int flatPosition = mPositionTranslator.getFlatPosition(packedPosition);

        if (flatPosition != RecyclerView.NO_POSITION) {
            notifyItemChanged(flatPosition, payload);
        }
    }

    /*package*/ void notifyGroupAndChildrenItemsChanged(int groupPosition, Object payload) {
        final long packedPosition = ExpandableAdapterHelper.getPackedPositionForGroup(groupPosition);
        final int flatPosition = mPositionTranslator.getFlatPosition(packedPosition);
        final int visibleChildCount = mPositionTranslator.getVisibleChildCount(groupPosition);

        if (flatPosition != RecyclerView.NO_POSITION) {
            notifyItemRangeChanged(flatPosition, 1 + visibleChildCount, payload);
        }
    }

    /*package*/ void notifyChildrenOfGroupItemChanged(int groupPosition, Object payload) {
        final int visibleChildCount = mPositionTranslator.getVisibleChildCount(groupPosition);

        // notify if the group is expanded
        if (visibleChildCount > 0) {
            final long packedPosition = ExpandableAdapterHelper.getPackedPositionForChild(groupPosition, 0);
            final int flatPosition = mPositionTranslator.getFlatPosition(packedPosition);

            if (flatPosition != RecyclerView.NO_POSITION) {
                notifyItemRangeChanged(flatPosition, visibleChildCount, payload);
            }
        }
    }

    /*package*/ void notifyChildItemChanged(int groupPosition, int childPosition, Object payload) {
        notifyChildItemRangeChanged(groupPosition, childPosition, 1, payload);
    }

    /*package*/ void notifyChildItemRangeChanged(int groupPosition, int childPositionStart, int itemCount, Object payload) {
        final int visibleChildCount = mPositionTranslator.getVisibleChildCount(groupPosition);

        // notify if the group is expanded
        if ((visibleChildCount > 0) && (childPositionStart < visibleChildCount)) {
            final long packedPosition = ExpandableAdapterHelper.getPackedPositionForChild(groupPosition, 0);
            final int flatPosition = mPositionTranslator.getFlatPosition(packedPosition);

            if (flatPosition != RecyclerView.NO_POSITION) {
                final int startPosition = flatPosition + childPositionStart;
                final int count = Math.min(itemCount, (visibleChildCount - childPositionStart));

                notifyItemRangeChanged(startPosition, count, payload);
            }
        }
    }

    /*package*/ void notifyChildItemInserted(int groupPosition, int childPosition) {
        mPositionTranslator.insertChildItem(groupPosition, childPosition);

        final long packedPosition = ExpandableAdapterHelper.getPackedPositionForChild(groupPosition, childPosition);
        final int flatPosition = mPositionTranslator.getFlatPosition(packedPosition);

        if (flatPosition != RecyclerView.NO_POSITION) {
            notifyItemInserted(flatPosition);
        }
    }

    /*package*/ void notifyChildItemRangeInserted(int groupPosition, int childPositionStart, int itemCount) {
        mPositionTranslator.insertChildItems(groupPosition, childPositionStart, itemCount);

        final long packedPosition = ExpandableAdapterHelper.getPackedPositionForChild(groupPosition, childPositionStart);
        final int flatPosition = mPositionTranslator.getFlatPosition(packedPosition);

        if (flatPosition != RecyclerView.NO_POSITION) {
            notifyItemRangeInserted(flatPosition, itemCount);
        }
    }

    /*package*/ void notifyChildItemRemoved(int groupPosition, int childPosition) {
        final long packedPosition = ExpandableAdapterHelper.getPackedPositionForChild(groupPosition, childPosition);
        final int flatPosition = mPositionTranslator.getFlatPosition(packedPosition);

        mPositionTranslator.removeChildItem(groupPosition, childPosition);

        if (flatPosition != RecyclerView.NO_POSITION) {
            notifyItemRemoved(flatPosition);
        }
    }

    /*package*/ void notifyChildItemRangeRemoved(int groupPosition, int childPositionStart, int itemCount) {
        final long packedPosition = ExpandableAdapterHelper.getPackedPositionForChild(groupPosition, childPositionStart);
        final int flatPosition = mPositionTranslator.getFlatPosition(packedPosition);

        mPositionTranslator.removeChildItems(groupPosition, childPositionStart, itemCount);

        if (flatPosition != RecyclerView.NO_POSITION) {
            notifyItemRangeRemoved(flatPosition, itemCount);
        }
    }

    /*package*/ void notifyGroupItemInserted(int groupPosition, boolean expanded) {
        int insertedCount = mPositionTranslator.insertGroupItem(groupPosition, expanded);
        if (insertedCount > 0) {
            final long packedPosition = ExpandableAdapterHelper.getPackedPositionForGroup(groupPosition);
            final int flatPosition = mPositionTranslator.getFlatPosition(packedPosition);

            notifyItemInserted(flatPosition);

            // raise onGroupExpand() event
            raiseOnGroupExpandedSequentially(groupPosition, 1, false, null);
        }
    }

    /*package*/ void notifyGroupItemRangeInserted(int groupPositionStart, int count, boolean expanded) {
        int insertedCount = mPositionTranslator.insertGroupItems(groupPositionStart, count, expanded);
        if (insertedCount > 0) {
            final long packedPosition = ExpandableAdapterHelper.getPackedPositionForGroup(groupPositionStart);
            final int flatPosition = mPositionTranslator.getFlatPosition(packedPosition);

            notifyItemRangeInserted(flatPosition, insertedCount);

            raiseOnGroupExpandedSequentially(groupPositionStart, count, false, null);
        }
    }

    /*package*/ void notifyGroupItemMoved(int fromGroupPosition, int toGroupPosition) {
        long packedFrom = RecyclerViewExpandableItemManager.getPackedPositionForGroup(fromGroupPosition);
        long packedTo = RecyclerViewExpandableItemManager.getPackedPositionForGroup(toGroupPosition);
        int flatFrom = getFlatPosition(packedFrom);
        int flatTo = getFlatPosition(packedTo);
        boolean fromExpanded = isGroupExpanded(fromGroupPosition);
        boolean toExpanded = isGroupExpanded(toGroupPosition);

        mPositionTranslator.moveGroupItem(fromGroupPosition, toGroupPosition);

        if (!fromExpanded && !toExpanded) {
            notifyItemMoved(flatFrom, flatTo);
        } else {
            notifyDataSetChanged();
        }
    }

    /*package*/ void notifyChildItemMoved(int groupPosition, int fromChildPosition, int toChildPosition) {
        notifyChildItemMoved(groupPosition, fromChildPosition, groupPosition, toChildPosition);
    }

    /*package*/ void notifyChildItemMoved(int fromGroupPosition, int fromChildPosition, int toGroupPosition, int toChildPosition) {
        long packedFrom = RecyclerViewExpandableItemManager.getPackedPositionForChild(fromGroupPosition, fromChildPosition);
        long packedTo = RecyclerViewExpandableItemManager.getPackedPositionForChild(toGroupPosition, toChildPosition);
        int flatFrom = getFlatPosition(packedFrom);
        int flatTo = getFlatPosition(packedTo);

        mPositionTranslator.moveChildItem(fromGroupPosition, fromChildPosition, toGroupPosition, toChildPosition);

        if (flatFrom != RecyclerView.NO_POSITION && flatTo != RecyclerView.NO_POSITION) {
            notifyItemMoved(flatFrom, flatTo);
        } else if (flatFrom != RecyclerView.NO_POSITION) {
            notifyItemRemoved(flatFrom);
        } else if (flatTo != RecyclerView.NO_POSITION) {
            notifyItemInserted(flatTo);
        }
    }

    private void raiseOnGroupExpandedSequentially(int groupPositionStart, int count, boolean fromUser, Object payload) {
        if (mOnGroupExpandListener != null) {
            for (int i = 0; i < count; i++) {
                mOnGroupExpandListener.onGroupExpand(groupPositionStart + i, fromUser, payload);
            }
        }
    }

    /*package*/ void notifyGroupItemRemoved(int groupPosition) {
        final long packedPosition = ExpandableAdapterHelper.getPackedPositionForGroup(groupPosition);
        final int flatPosition = mPositionTranslator.getFlatPosition(packedPosition);

        int removedCount = mPositionTranslator.removeGroupItem(groupPosition);
        if (removedCount > 0) {
            notifyItemRangeRemoved(flatPosition, removedCount);
        }
    }

    /*package*/ void notifyGroupItemRangeRemoved(int groupPositionStart, int count) {
        final long packedPosition = ExpandableAdapterHelper.getPackedPositionForGroup(groupPositionStart);
        final int flatPosition = mPositionTranslator.getFlatPosition(packedPosition);

        int removedCount = mPositionTranslator.removeGroupItems(groupPositionStart, count);
        if (removedCount > 0) {
            notifyItemRangeRemoved(flatPosition, removedCount);
        }
    }

    /*package*/ int getGroupCount() {
        return mExpandableItemAdapter.getGroupCount();
    }

    /*package*/ int getChildCount(int groupPosition) {
        return mExpandableItemAdapter.getChildCount(groupPosition);
    }

    /*package*/ int getExpandedGroupsCount() {
        return mPositionTranslator.getExpandedGroupsCount();
    }

    /*package*/ int getCollapsedGroupsCount() {
        return mPositionTranslator.getCollapsedGroupsCount();
    }

    /*package*/ boolean isAllGroupsExpanded() {
        return mPositionTranslator.isAllExpanded();
    }

    /*package*/ boolean isAllGroupsCollapsed() {
        return mPositionTranslator.isAllCollapsed();
    }

    private static ExpandableItemAdapter getExpandableItemAdapter(RecyclerView.Adapter adapter) {
        return WrapperAdapterUtils.findWrappedAdapter(adapter, ExpandableItemAdapter.class);
    }

    private static void safeUpdateExpandStateFlags(RecyclerView.ViewHolder holder, int flags) {
        if (!(holder instanceof ExpandableItemViewHolder)) {
            return;
        }

        final ExpandableItemViewHolder holder2 = (ExpandableItemViewHolder) holder;

        final int curFlags = holder2.getExpandStateFlags();
        final int mask = ~Constants.STATE_FLAG_IS_UPDATED;

        // append HAS_EXPANDED_STATE_CHANGED flag
        if ((curFlags != STATE_FLAG_INITIAL_VALUE) && (((curFlags ^ flags) & Constants.STATE_FLAG_IS_EXPANDED) != 0)) {
            flags |= Constants.STATE_FLAG_HAS_EXPANDED_STATE_CHANGED;
        }

        // append UPDATED flag
        if ((curFlags == STATE_FLAG_INITIAL_VALUE) || (((curFlags ^ flags) & mask) != 0)) {
            flags |= Constants.STATE_FLAG_IS_UPDATED;
        }

        holder2.setExpandStateFlags(flags);
    }
}
