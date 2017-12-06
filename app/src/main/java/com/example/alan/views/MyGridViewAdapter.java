package com.example.alan.views;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.example.alan.model.IButtonListener;
import com.example.alan.model.WordButton;
import com.example.alan.myapplication.R;

import java.util.List;

/**
 * Created by Alan on 2017/12/6.
 */

public class MyGridViewAdapter extends CustomAdapter<WordButton> {


    private IButtonListener iButtonListener;

    public MyGridViewAdapter(Context mContext, List<WordButton> mData, int resID) {
        super(mContext, mData, resID);
    }




    @Override
    public void convert(CustomViewHolder viewHolder, final int position) {

        Button button = viewHolder.getView(R.id.bt_gridview_item);
        button.setText(mData.get(position).getWord());
        if (!mData.get(position).isIsvisable()){
            button.setVisibility(View.INVISIBLE);
        }else {
            button.setVisibility(View.VISIBLE);
        }

        mData.get(position).setmViewButton(button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iButtonListener.OnButtonClickLister(mData.get(position));
            }
        });

    }

    public void registerButtonListener(IButtonListener listener) {
        iButtonListener = listener;
    }
}


