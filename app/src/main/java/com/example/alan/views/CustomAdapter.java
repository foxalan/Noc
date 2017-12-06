package com.example.alan.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Function :
 * Author : Alan
 * Modify Date : 21/8/17
 * Issue : TODO
 * Whether solve :
 */

public abstract class CustomAdapter<T> extends BaseAdapter {

    protected Context mContext;
    protected List<T> mData;
    protected LayoutInflater inflater;
    private int resID;


    public CustomAdapter(Context mContext, List<T> mData, int resID) {
        this.mContext = mContext;
        this.mData = mData;
        inflater = LayoutInflater.from(mContext);
        this.resID = resID;

    }

    @Override
    public int getCount() {

        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position );
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CustomViewHolder viewHolder = CustomViewHolder.getViewHolder(convertView, mContext, parent, resID);

        convert(viewHolder, position );

        return viewHolder.getConvertView();
    }

    public abstract void convert(CustomViewHolder viewHolder, int position);
}
