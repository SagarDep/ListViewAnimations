/*
 * Copyright 2013 Niek Haarman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nhaarman.listviewanimations.extras.itemmanipulation;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;

import com.nhaarman.listviewanimations.BaseAdapterDecorator;
import com.nhaarman.listviewanimations.ListViewSetter;
import com.nhaarman.listviewanimations.extras.itemmanipulation.widget.DynamicListView;
import com.nhaarman.listviewanimations.extras.itemmanipulation.widget.DynamicListView.Swappable;

/**
 * A decorator class that enables decoration of an instance of the BaseAdapter
 * class.
 *
 * Classes extending this class can override methods and provide extra
 * functionality before or after calling the super method.
 */
public abstract class BaseAdapterDecoratorExtended extends BaseAdapterDecorator implements DynamicListView.Swappable {
	private int mResIdTouchChild;

	public BaseAdapterDecoratorExtended(final BaseAdapter baseAdapter) {
		super(baseAdapter);
	}

	@Override
	public void setAbsListView(final AbsListView listView) {
		super.setAbsListView(listView);

		if (mListView instanceof DynamicListView) {
			DynamicListView dynListView = (DynamicListView) mListView;
			dynListView.setIsParentHorizontalScrollContainer(mIsParentHorizontalScrollContainer);
			dynListView.setDynamicTouchChild(mResIdTouchChild);
		}
	}

	@Override
	public void notifyDataSetChanged() {
		if (!(mDecoratedBaseAdapter instanceof ArrayAdapter<?>)) {
			// fix #35 dirty trick !
			// leads to an infinite loop when trying because ArrayAdapter triggers notifyDataSetChanged itself
			mDecoratedBaseAdapter.notifyDataSetChanged();
		}
	}
	
	/**
	 * Helper function if you want to force notifyDataSetChanged()
	 */
	@SuppressWarnings("UnusedDeclaration")
    public void notifyDataSetChanged(final boolean force) {
		if (force || !(mDecoratedBaseAdapter instanceof ArrayAdapter<?>)) {
			// leads to an infinite loop when trying because ArrayAdapter triggers notifyDataSetChanged itself
			mDecoratedBaseAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void swapItems(final int positionOne, final int positionTwo) {
		if (mDecoratedBaseAdapter instanceof Swappable) {
			((Swappable) mDecoratedBaseAdapter).swapItems(positionOne, positionTwo);
		}
	}

	/**
	 * If the adapter's list-view is hosted inside a parent(/grand-parent/etc) that can scroll horizontally, horizontal swipes won't
	 * work, because the parent will prevent touch-events from reaching the list-view.
	 *
	 * Call this method with the value 'true' to fix this behavior.
	 * Note that this will prevent the parent from scrolling horizontally when the user touches anywhere in a list-item.
	 */
	public void setIsParentHorizontalScrollContainer(final boolean isParentHorizontalScrollContainer) {
		super.setIsParentHorizontalScrollContainer(isParentHorizontalScrollContainer);
		if (mListView instanceof DynamicListView) {
			DynamicListView dynListView = (DynamicListView) mListView;
			dynListView.setIsParentHorizontalScrollContainer(mIsParentHorizontalScrollContainer);
		}
	}

	/**
	 * If the adapter's list-view is hosted inside a parent(/grand-parent/etc) that can scroll horizontally, horizontal swipes won't
	 * work, because the parent will prevent touch-events from reaching the list-view.
	 *
	 * If a list-item view has a child with the given resource-ID, the user can still swipe the list-item by touching that child.
	 * If the user touches an area outside that child (but inside the list-item view), then the swipe will not happen and the parent
	 * will do its job instead (scrolling horizontally).
	 *
	 * @param childResId The resource-ID of the list-items' child that the user should touch to be able to swipe the list-items.
	 */
	public void setTouchChild(final int childResId) {
		mResIdTouchChild = childResId;
		if (mListView instanceof DynamicListView) {
			DynamicListView dynListView = (DynamicListView) mListView;
			dynListView.setDynamicTouchChild(mResIdTouchChild);
		}
	}

	public int getTouchChild() {
		return mResIdTouchChild;
	}
}