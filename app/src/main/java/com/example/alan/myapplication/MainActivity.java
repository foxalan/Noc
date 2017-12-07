package com.example.alan.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alan.beans.Const;
import com.example.alan.model.IButtonListener;
import com.example.alan.model.WordButton;
import com.example.alan.utils.PreferenceUtil;
import com.example.alan.views.MyGridViewAdapter;
import com.example.alan.views.RandomStringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class MainActivity extends AppCompatActivity implements IButtonListener {
    //关于游戏的动画设置
    private Animation mPanelAnim;
    private Animation mGamePinSetAnim;
    private Animation mGamePinBackAnim;

    @BindView(R.id.iv_game_round)
    ImageView iv_game_round;
    @BindView(R.id.iv_game_pin)
    ImageView iv_game_pin;
    @BindView(R.id.ib_game_start)
    ImageButton ib_game_start;

    //设置按钮是否可以被点击
    private boolean isRunning = false;

    private List<WordButton> wordButtonList = new ArrayList<>();
    @BindView(R.id.mygridview)
    GridView gridView;

    @BindView(R.id.ll_music_name)
    LinearLayout ll_music_name;

    private List<WordButton> wordButtonListName = new ArrayList<>();
    private LayoutInflater inflater;

    private int currentSongPosition;
    private int songLength;
    private String currentSong;

    private final static String TAG = "MainActivity";

    private final static int MUSIC_STATE_RIGHT = 1;
    private final static int MUSIC_STATE_ERROR = 2;
    private final static int MUSIC_STATE_BUG = 3;

    @BindView(R.id.tv_coins)
    TextView tv_coins;
    @BindView(R.id.tv_passes)
    TextView tv_passes;
    @BindView(R.id.ib_remove_error)
    ImageButton ib_remove_error;
    @BindView(R.id.ib_get_tip)
    ImageButton ib_get_tip;
    @BindView(R.id.ib_share)
    ImageButton ib_share;

    private int coin_number = 2000;
    private List<Integer> music_id = new ArrayList<>();
    private List<Integer> tip_id = new ArrayList<>();

    private Unbinder unbinder;

    private MyGridViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉状态
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        inflater = LayoutInflater.from(this);
        unbinder = ButterKnife.bind(this);

        initData();
        initAnimations();
        initEvents();
        setNameSelect();
    }

    //初始化数据

    /**
     * 1.初始化与歌相关的内容
     * 2.初始化金币数量
     * 3.初始化所有的文字
     * 4.初始化
     */
    private void initData() {

        currentSongPosition = PreferenceUtil.getInt(Const.CURRENT_SONG_POSITION, 0);

        songLength = Const.SONG_INFO[currentSongPosition][1].length();
        currentSong = Const.SONG_INFO[currentSongPosition][1];

        coin_number = PreferenceUtil.getInt(Const.COIN_SIZE, Const.COIN_NUMBER);

        String[] word = RandomStringUtil.generateWords(currentSong, songLength);
        for (int i = 0; i < Const.TOTAL_WORD_SIZE; i++) {
            WordButton button = new WordButton();
            button.setId(i);
            button.setIsvisable(true);
            button.setWord(word[i]);
            wordButtonList.add(button);
        }

        music_id.clear();

        for (int j = 0; j < songLength; j++) {

            for (int i = 0; i < 24; i++) {
                if (wordButtonList.get(i).getWord().equals(currentSong.substring(j, j + 1))) {
                    music_id.add(i);
                    Log.e(TAG, "initData: " +i );
                }
            }

        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick({R.id.ib_share, R.id.ib_get_tip, R.id.ib_remove_error, R.id.ib_game_start})
    void onClick(View view) {

        switch (view.getId()) {
            case R.id.ib_share:

                break;
            case R.id.ib_get_tip:
                set_correct_answer();
                break;
            case R.id.ib_remove_error:
                remove_error_select();
                break;
            case R.id.ib_game_start:

                if (iv_game_pin == null) {
                    return;
                }
                if (isRunning == false) {
                    iv_game_pin.startAnimation(mGamePinSetAnim);
                    isRunning = true;
                    ib_game_start.setVisibility(View.INVISIBLE);
                }
                break;
        }

    }


    private void initEvents() {

        adapter = new MyGridViewAdapter(this, wordButtonList, R.layout.gridview_item);
        gridView.setAdapter(adapter);
        adapter.registerButtonListener(this);
        tv_coins.setText(coin_number + "");

    }


    /**
     * 1.去掉一个错误答案
     * 2.改变金币数据
     * 3.改变UI
     */
    private void remove_error_select() {

        if (PreferenceUtil.getInt(Const.COIN_SIZE, 2000) < Const.COIN_TAKE_ERROR) {
            Toast.makeText(this, "金币不足", Toast.LENGTH_LONG).show();
            return;
        }

        if ((tip_id.size() + music_id.size()) == 20) {
            Toast.makeText(this, "不能再去掉错误的答案了", Toast.LENGTH_LONG).show();
            return;
        }

        Random random = new Random();
        int error_select = random.nextInt(23);
        while (!judge_isCorrect(error_select)) {
            error_select = random.nextInt(23);
        }
        tip_id.add(error_select);

        mHandler.sendEmptyMessage(MSG_COIN_TEXT);

        wordButtonList.get(error_select).getmViewButton().setVisibility(View.INVISIBLE);
        wordButtonList.get(error_select).getmViewButton().setEnabled(false);
    }

    /**
     * 设置一个正确的答案
     */
    private void set_correct_answer() {

        if (PreferenceUtil.getInt(Const.COIN_SIZE,2000)<Const.COIN_TAKE_TIP){
            Toast.makeText(this,"金币不足",Toast.LENGTH_LONG).show();
            return;
        }

        boolean isCanSet = false;

        if (wordButtonListName.size() == 0) {
            isCanSet = true;
        } else {
            for (WordButton wordButton : wordButtonListName) {
                if (wordButton.getWord().equals("")) {
                    Log.e(TAG, "set_correct_answer: " + wordButton.getWord());
                    isCanSet = true;
                    break;

                }
            }
        }

        if (isCanSet) {
            for (int i = 0; i < wordButtonListName.size(); i++) {
                if (wordButtonListName.get(i).getWord().equals("")) {

                    Log.e(TAG, "set_correct_answer: " + wordButtonList.size() + ":" + music_id.size());

                    wordButtonList.get(music_id.get(i)).getmViewButton().performClick();
                    Log.e(TAG, "set_correct_answer: "+music_id.get(i) );
                    break;
                }
            }
        } else {
            Toast.makeText(this,"请先清除错误答案",Toast.LENGTH_LONG).show();
            return;
        }

        mHandler.sendEmptyMessage(0x121);


    }

    //判断删除的是正确的歌曲名称和重复
    private boolean judge_isCorrect(int random) {


        for (int m = 0; m < music_id.size(); m++) {
            if (random == music_id.get(m)) {
                return false;
            }
        }

        for (int n = 0; n < tip_id.size(); n++) {
            if (random == tip_id.get(n)) {
                return false;
            }
        }

        return true;
    }


    private void initAnimations() {
        mPanelAnim = AnimationUtils.loadAnimation(this, R.anim.rotate);
        mPanelAnim.setDuration(2000);
        mPanelAnim.setInterpolator(new LinearInterpolator());
        mPanelAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                iv_game_pin.startAnimation(mGamePinBackAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        mGamePinSetAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_go);
        mGamePinSetAnim.setDuration(300);
        mGamePinSetAnim.setInterpolator(new LinearInterpolator());
        mGamePinSetAnim.setFillAfter(true);
        mGamePinSetAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                iv_game_round.startAnimation(mPanelAnim);


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mGamePinBackAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_back);
        mGamePinBackAnim.setDuration(300);
        mGamePinBackAnim.setInterpolator(new LinearInterpolator());
        mGamePinBackAnim.setFillAfter(true);
        mGamePinBackAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isRunning = false;
                ib_game_start.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        iv_game_round.clearAnimation();
        iv_game_pin.clearAnimation();
    }

    //设置歌名长度
    public void setMusicNameDates() {

        for (int i = 0; i < songLength; i++) {

            View view = inflater.inflate(R.layout.gridview_item, null);
            final WordButton wordButton = new WordButton();

            Button button = (Button) view.findViewById(R.id.bt_gridview_item);
            wordButton.setmViewButton(button);
            wordButton.getmViewButton().setBackgroundResource(R.drawable.game_wordblank);
            wordButton.getmViewButton().setText(wordButton.getWord());
            wordButton.getmViewButton().setTextColor(Color.WHITE);

            wordButton.getmViewButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    wordButton.setWord("");
                    wordButton.getmViewButton().setText("");

                    wordButtonList.get(wordButton.getId()).getmViewButton().setVisibility(View.VISIBLE);
                    wordButtonList.get(wordButton.getId()).setIsvisable(true);

                }
            });
            wordButtonListName.add(wordButton);
        }
    }

    //设置歌曲框
    private void setNameSelect() {
        setMusicNameDates();
        ll_music_name.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100, 100);

        for (int i = 0; i < wordButtonListName.size(); i++) {
            ll_music_name.addView(wordButtonListName.get(i).getmViewButton(), params);
        }
    }


    /**
     * 当答案错了的时候，文字闪烁，并显示红色
     */
    private void setStateError() {
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            int count = 5;

            @Override
            public void run() {
                while (count >= 0) {
                    Log.d("tang", "timer" + count);
                    count--;
                    mHandler.sendEmptyMessage(MSG_ERROR);
                    isWhite = !isWhite;
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 0, 300);

    }

    private boolean isWhite = false;
    private final static int MSG_ERROR = 0x123;
    private final static int MSG_COIN_TEXT = 0x122;
    private final static int MSG_COIN_TIP = 0x121;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_ERROR:
                    for (int i = 0; i < wordButtonListName.size(); i++) {
                        wordButtonListName.get(i).getmViewButton().setTextColor(isWhite ? Color.WHITE : Color.RED);
                    }
                    break;
                case MSG_COIN_TEXT:
                    int current_coin = PreferenceUtil.getInt(Const.COIN_SIZE, 2000) - Const.COIN_TAKE_ERROR;
                    tv_coins.setText(current_coin + "");
                    PreferenceUtil.put(Const.COIN_SIZE, current_coin);
                    break;
                case MSG_COIN_TIP:
                    int coins = PreferenceUtil.getInt(Const.COIN_SIZE, 2000) - Const.COIN_TAKE_TIP;
                    tv_coins.setText(coins + "");
                    PreferenceUtil.put(Const.COIN_SIZE, coins);
                    break;

            }

        }
    };

    //判断歌曲文字的状态
    private int getMusicState() {

        for (int i = 0; i < songLength; i++) {
            if (wordButtonListName.get(i).getWord().equals("")) {
                return MUSIC_STATE_BUG;
            }
        }

        int j = 0;
        for (int i = 0; i < songLength; i++) {

            if (wordButtonListName.get(i).getWord().equals(currentSong.substring(i, i + 1))) {
                j++;
            }
        }

        return j == songLength ? MUSIC_STATE_RIGHT : MUSIC_STATE_ERROR;
    }

    @Override
    public void OnButtonClickLister(WordButton wordButton) {

        for (int i = 0; i < songLength; i++) {
            if (wordButtonListName.get(i).getWord().equals("")) {

                wordButtonListName.get(i).getmViewButton().setVisibility(View.VISIBLE);
                wordButtonListName.get(i).getmViewButton().setText(wordButton.getWord());
                wordButtonListName.get(i).setId(wordButton.getId());
                wordButtonListName.get(i).setWord(wordButton.getWord());

                wordButton.getmViewButton().setVisibility(View.INVISIBLE);
                wordButton.setIsvisable(false);

                break;
            }
        }

        adapter.notifyDataSetChanged();

        switch (getMusicState()) {
            case MUSIC_STATE_BUG:
                for (int i = 0; i < wordButtonListName.size(); i++) {
                    wordButtonListName.get(i).getmViewButton().setTextColor(Color.WHITE);
                }
                break;
            case MUSIC_STATE_RIGHT:

                for (int i = 0; i < wordButtonListName.size(); i++) {
                    wordButtonListName.get(i).getmViewButton().setTextColor(Color.WHITE);
                }
                Toast.makeText(MainActivity.this, "right", Toast.LENGTH_LONG).show();
                break;
            case MUSIC_STATE_ERROR:
                setStateError();
                break;
        }
    }
}
