package com.example.alan.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by alan on 2017/3/30.
 */

public class SpUtils {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SpUtils(Context context,String string){
        sharedPreferences =context.getSharedPreferences(string,Context.MODE_PRIVATE) ;
        editor = sharedPreferences.edit();
    }

    public void putString(String key,String value){
        editor.putString(key,value);
    }

    public String getString(String key){

        return sharedPreferences.getString(key,null);
    }

    public void putInt(String key,int value){
        editor.putInt(key,value);
        editor.commit();
    }

    public int getInt(String key){
        return sharedPreferences.getInt(key,2000);
    }



}
