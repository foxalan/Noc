package com.example.alan.views;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.alan.beans.Const;
import com.example.alan.model.IButtonUpdate;
import com.example.alan.myapplication.R;
import com.example.alan.utils.PreferenceUtil;

/**
 * Created by alan on 2017/3/29.
 */

public class CustomDialog extends Dialog {

    private IButtonUpdate iButtonUpdate;

    public IButtonUpdate getiButtonUpdate() {
        return iButtonUpdate;
    }

    public void setiButtonUpdate(IButtonUpdate iButtonUpdate) {
        this.iButtonUpdate = iButtonUpdate;
    }

    public CustomDialog answerRightDialog;
    public TextView tv_current_position;
    public TextView tv_current_song_name;
    public ImageButton bt_next_game;
    public ImageButton ib_share;


    public CustomDialog(Context context) {
        this(context, 0);

    }

    public CustomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public void createRightDialog(Context context) {

        answerRightDialog = new CustomDialog(context, R.style.MyDialog);
        answerRightDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        answerRightDialog.setContentView(R.layout.dialog_right);

        tv_current_position = (TextView) answerRightDialog.findViewById(R.id.tv_dialog_current_position);
        tv_current_song_name = (TextView) answerRightDialog.findViewById(R.id.tv_current_song);

        bt_next_game = (ImageButton) answerRightDialog.findViewById(R.id.ib_game_next);
        ib_share = (ImageButton) answerRightDialog.findViewById(R.id.ib_share);


        bt_next_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = PreferenceUtil.getInt(Const.CURRENT_SONG_POSITION, 0) + 1;
                PreferenceUtil.put(Const.CURRENT_SONG_POSITION, position);

                iButtonUpdate.update();

            }
        });


        Window window = answerRightDialog.getWindow();
        Display display = window.getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();

        WindowManager.LayoutParams lp = window.getAttributes();
        //所在页面透明度
        lp.width = width;
        lp.height = height;
        lp.dimAmount = 0.0f;
        window.setAttributes(lp);

    }

    public void showRightDialog() {

        hideRightDialog();

        if (answerRightDialog != null) {
            int position = PreferenceUtil.getInt(Const.CURRENT_SONG_POSITION, 0);

            tv_current_position.setText("" + position);
            tv_current_song_name.setText(Const.SONG_INFO[position][1] + "");


            answerRightDialog.show();
        }
    }

    public  void hideRightDialog() {
        if (answerRightDialog != null) {
            answerRightDialog.dismiss();
        }
    }


}
