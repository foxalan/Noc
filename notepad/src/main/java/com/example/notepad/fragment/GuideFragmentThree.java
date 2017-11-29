package com.example.notepad.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.notepad.MainActivity;
import com.example.notepad.R;

/**
 * Created by alan on 2017/6/26.
 */

public class GuideFragmentThree extends BaseFragment {
    private Button bt_enter;

    private SharedPreferences sharedPreferences ;
    private SharedPreferences.Editor editor ;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide_three,null);
        bt_enter = (Button) view.findViewById(R.id.bt_enter);



        bt_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedPreferences =  getActivity().getSharedPreferences("isGuide", Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putBoolean("isguide",false);
                editor.commit();
                startActivity(new Intent(getActivity(), MainActivity.class));


            }
        });


        return view;
    }


}
