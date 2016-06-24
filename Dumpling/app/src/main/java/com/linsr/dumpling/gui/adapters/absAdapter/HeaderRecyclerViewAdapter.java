/*
 * Copyright (C) 2015 Karumi.
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

package com.linsr.dumpling.gui.adapters.absAdapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * RecyclerView.Adapter extension created to add header capability support.
 */
public abstract class HeaderRecyclerViewAdapter<VH extends RecyclerView.ViewHolder>
        extends CursorRecyclerViewAdapter<VH> {

    protected static final int TYPE_HEADER = -2;
    protected static final int TYPE_ITEM = -1;


    protected LayoutInflater mInflater;

    public HeaderRecyclerViewAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        mInflater = LayoutInflater.from(context);
    }

    /**
     * Invokes onCreateHeaderViewHolder, onCreateItemViewHolder or onCreateFooterViewHolder methods
     * based on the view type param.
     */
    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        VH viewHolder;
        if (isHeaderType(viewType)) {
            viewHolder = onCreateHeaderViewHolder(parent, viewType);
        } else {
            viewHolder = onCreateItemViewHolder(parent, viewType);
        }
        return viewHolder;
    }

    /**
     * Invokes onBindHeaderViewHolder, onBindItemViewHolder or onBindFooterViewHOlder methods based
     * on the position param.
     */
    @Override
    public void onBindViewHolder(VH holder, Cursor cursor, int position) {
        if (isHeaderPosition(position)) {
            onBindHeaderViewHolder(holder, position);
        } else {
            onBindItemViewHolder(holder, cursor);
        }
    }

    /**
     * Returns the type associated to an item given a position passed as arguments. If the position
     * is related to a header item returns the constant TYPE_HEADER, if not, returns TYPE_ITEM.
     * <p/>
     * If your application has to support different types override this method and provide your
     * implementation. Remember that TYPE_HEADER and TYPE_ITEM are internal constants
     * can be used to identify an item given a position, try to use different values in your
     * application.
     */
    @Override
    public int getItemViewType(int position) {
        int viewType = TYPE_ITEM;
        if (isHeaderPosition(position)) {
            viewType = TYPE_HEADER;
        }
        return viewType;
    }

    /**
     * Returns the items list size if there is no a header configured or the size taking into account
     * that if a header or a footer is configured the number of items returned is going to include
     * this elements.
     */
    @Override
    public int getItemCount() {
        int size = super.getItemCount();
        if (hasHeader()) {
            size++;
        }
        return size;
    }

    protected abstract VH onCreateItemViewHolder(ViewGroup parent, int viewType);

    protected abstract void onBindItemViewHolder(VH holder, Cursor cursor);

    protected abstract VH onCreateHeaderViewHolder(ViewGroup parent, int viewType) ;

    protected abstract void onBindHeaderViewHolder(VH holder, int position) ;

    /**
     * Returns true if the view type parameter passed as argument is equals to TYPE_HEADER.
     */
    protected boolean isHeaderType(int viewType) {
        return viewType == TYPE_HEADER;
    }

    /**
     * Returns true if the position type parameter passed as argument is equals to 0 and the adapter
     * has a not null header already configured.
     */
    public boolean isHeaderPosition(int position) {
        return hasHeader() && position == 0;
    }




}
