/**
 * Copyright (c) 2011 SORMA
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Author: sorma@gaoshin.com
 */
package com.gaoshin.mandarin;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.gaoshin.mandarin.data.Item;

public class WordListAdapter extends BaseAdapter {

    private List<Item> ids;

    public WordListAdapter(List<Item> ids) {
        this.ids = ids;
    }

    public void setItems(List<Item> ids) {
        this.ids = ids;
    }

    @Override
    public int getCount() {
        return ids.size();
    }

    @Override
    public Object getItem(int position) {
        return ids.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item item = ids.get(position);
        ViewHolder myViewHolder;
        if (convertView == null) {
            myViewHolder = new ViewHolder(item);
            LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = myViewHolder.inflate(inflater, parent);
            convertView.setTag(myViewHolder);
                
        } else {
            myViewHolder = (ViewHolder) convertView.getTag();
            myViewHolder.setItem(item);
        }
        
        myViewHolder.setup(parent.getContext());

        return convertView;
    }

}
