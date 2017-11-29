package com.example.alan.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.example.alan.model.IbuttonListenr;
import com.example.alan.model.WordButton;
import com.example.alan.myapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alan on 2017/3/20.
 */

public class MyGridView extends GridView {

    private List<WordButton> mList = new ArrayList<>();
    private LayoutInflater inflater;
    private MyGridViewAdapter adapter;

    public final static int COUNTS_WORDS = 24;

    private Animation mAnimation;
    private Context mContext;

    private IbuttonListenr ibuttonListenr;

    public MyGridView(Context context) {
        this(context,null);
    }

    public MyGridView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflater = LayoutInflater.from(context);
        mContext = context;
        adapter = new MyGridViewAdapter();
        this.setAdapter(adapter);
    }

    //动态刷新
    public void reflashAdapter(List<WordButton> wordButtonList){
        mList = wordButtonList;
        this.setAdapter(adapter);
    }


    class MyGridViewAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final WordButton holder;

            mAnimation = AnimationUtils.loadAnimation(mContext,R.anim.buttonscale);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.gridview_item, null);
                holder = mList.get(position);
                holder.id = position;
                holder.mViewButton = (Button) convertView.findViewById(R.id.bt_gridview_item);
                holder.isvisable = true;
                convertView.setTag(holder);
            }else {
                holder = (WordButton) convertView.getTag();
            }

            holder.mViewButton.setText(holder.word);
            holder.mViewButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    ibuttonListenr.OnButtonClickLister(holder);
                }
            });

            AbsListView.LayoutParams param = new AbsListView.LayoutParams(95, 95);
            convertView.setLayoutParams(param);


            convertView.startAnimation(mAnimation);
            mAnimation.setStartOffset(position*100);
            return convertView;
        }
    }

    public  void resigterButtonLister(IbuttonListenr listenr){
        ibuttonListenr = listenr;
    }


}
