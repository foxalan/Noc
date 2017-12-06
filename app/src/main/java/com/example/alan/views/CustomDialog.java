package com.example.alan.views;

import android.app.Dialog;
import android.content.Context;

import com.example.alan.myapplication.R;

/**
 * Created by alan on 2017/3/29.
 */

public class CustomDialog extends Dialog {

    public static CustomDialog answerRightDialog;


    public CustomDialog(Context context) {
        this(context,0);

    }

    public CustomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static void createRightDialog(Context context){

        answerRightDialog = new CustomDialog(context, R.style.MyDialog);




    }



}
