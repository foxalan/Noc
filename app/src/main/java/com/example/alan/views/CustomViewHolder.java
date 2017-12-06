package com.example.alan.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Function : 封装holder
 * Author : Alan
 * Modify Date : 21/8/17
 * Issue : 1.convertView不太明白
 * Whether solve :
 */

public class CustomViewHolder {

    private View convertView;
    private static CustomViewHolder holder;

    /**
     * 提供一个容器，专门存每个Item布局中的所有控件，
     * 而且还要能够查找出来；既然需要查找，那么ListView肯定是不行了，
     * 需要一个键值对进行保存，键为控件的Id，值为控件的引用，相信大家立刻就能想到Map；
     * 但是我们不用Map，因为有更好的替代类，就是我们android提供的SparseArray这个类，和Map类似，但是比Map效率，
     * 不过键只能为Integer.
     */

    private final SparseArray<View> mViews;

    public CustomViewHolder(Context context, ViewGroup parent, int resID) {

        this.mViews = new SparseArray<View>();
        convertView = LayoutInflater.from(context).inflate(resID, parent, false);
        convertView.setTag(this);
    }

    public static CustomViewHolder getViewHolder(View convertView, Context context, ViewGroup parent, int resID) {

        if (convertView == null) {
            holder = new CustomViewHolder(context, parent, resID);
        } else {
            holder = (CustomViewHolder) convertView.getTag();
        }

        return holder;
    }

    public <T extends View> T getView(int resId) {

        View view = mViews.get(resId);
        if (view == null){
            view = convertView.findViewById(resId);
            mViews.put(resId,view);
        }

        return (T) view;
    }

    /**
     * 进一步简化
     * @param str
     * @param resId
     */

    public void setText(String str, int resId){
        TextView textView = getView(resId);
        textView.setText(str);
    }

    public void setBitmap(Bitmap bitmap, int resId){
        ImageView imageView = getView(resId);
        imageView.setImageBitmap(bitmap);
    }



    public View getConvertView()
    {
        return convertView;
    }


}
