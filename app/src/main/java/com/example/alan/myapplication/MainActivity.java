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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alan.beans.Const;
import com.example.alan.model.IbuttonListenr;
import com.example.alan.model.WordButton;
import com.example.alan.utils.SpUtils;
import com.example.alan.views.MyGridView;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements IbuttonListenr {
    //关于游戏的动画设置
    private Animation mPanelAnim;
    private Animation mGamePinSetAnim;
    private Animation mGamePinBackAnim;

    private ImageView iv_game_round;
    private ImageView iv_game_pin;
    private ImageButton ib_game_start;

    //设置按钮是否可以被点击
    private boolean isrunning = false;

    private List<WordButton> wordButtonList = new ArrayList<>();
    private MyGridView gridView;

    private LinearLayout ll_music_name;
    private List<WordButton> wordButtonListName = new ArrayList<>();

    private LayoutInflater inflater;

    private int currentSongitem = 1;
    private int songlength;
    private String currentSong;

    private final static String TAG = "MainActivity";

    private final static int MUSIC_STATE_RIGHT = 1;
    private final static int MUSIC_STATE_ERROR = 2;
    private final static int MUSIC_STATE_BUG = 3;

    private SpUtils sp_coin_size;
    private SpUtils sp_pass_size;

    private final static String COIN_SIZE = "coin_size";
    private final static String PASS_SIZE = "pass_size";
    private final static String COINS = "coins";
    private final static String PASSES ="passes";

    private TextView tv_coins;
    private TextView tv_passes;


    private int coin_number = 2000;
    private int coin_error = 90;
    private int coin_tip = 30;

    private ImageButton ib_remove_error;
    private ImageButton ib_get_tip;
    private ImageButton ib_share;

    private List<Integer> music_id = new ArrayList<>();
    private List<Integer> tip_id = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        inflater = LayoutInflater.from(this);

        songlength = Const.SONG_INFO[++currentSongitem][1].length();
        currentSong = Const.SONG_INFO[currentSongitem++][1];

        sp_coin_size = new SpUtils(this,COIN_SIZE);

        initviews();
        initanimations();
        initevents();

        setGridView();
        setNameseclect();
    }

    private void initevents() {

        gridView.resigterButtonLister(this);

        ib_game_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iv_game_pin == null) {
                    return;
                }
                if (isrunning == false) {
                    iv_game_pin.startAnimation(mGamePinSetAnim);
                    isrunning = true;
                    ib_game_start.setVisibility(View.INVISIBLE);
                }
            }
        });

        ib_get_tip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove_error_select();
            }
        });

        tv_coins.setText(sp_coin_size.getInt(COINS)+"");
    }

    private void remove_error_select() {
        coin_number = sp_coin_size.getInt(COINS);
        Random random = new Random();

        sp_coin_size.putInt(COINS,coin_number-=coin_tip);
        tv_coins.setText(sp_coin_size.getInt(COINS)+"");

        int error_select = random.nextInt(23);
        while (!judg_iscrrort(error_select)){
            error_select = random.nextInt(23);
        }
        tip_id.add(error_select);

        wordButtonList.get(error_select).mViewButton.setVisibility(View.INVISIBLE);
        wordButtonList.get(error_select).mViewButton.setEnabled(false);
    }

    //判断删除的是正确的歌曲名称和重复
    private boolean judg_iscrrort(int random) {
        for (int i =0 ; i<24;i++){
            for (int j = 0; j<songlength;j++){
                if (wordButtonList.get(i).word.equals(currentSong.substring(j,j+1))){
                    music_id.add(i);
                }
            }
        }

        for (int m = 0;m<music_id.size();m ++){
            if (random == music_id.get(m)){
                return false;
            }
        }

        for (int n = 0; n<tip_id.size(); n++){
            if (random == tip_id.get(n)){
                return  false;
            }
        }

        return true;
    }

    private void initviews() {
        iv_game_pin = (ImageView) findViewById(R.id.iv_game_pin);
        iv_game_round = (ImageView) findViewById(R.id.iv_game_round);
        ib_game_start = (ImageButton) findViewById(R.id.ib_game_start);

        gridView = (MyGridView) findViewById(R.id.mygridview);
        ll_music_name = (LinearLayout) findViewById(R.id.ll_music_name);

        tv_coins = (TextView) findViewById(R.id.tv_coins);
        tv_passes = (TextView) findViewById(R.id.tv_passes);

        ib_remove_error = (ImageButton) findViewById(R.id.ib_remove_error);
        ib_get_tip = (ImageButton) findViewById(R.id.ib_get_tip);
        ib_share = (ImageButton) findViewById(R.id.ib_share);
    }

    private void initanimations() {
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
                isrunning = false;
                ib_game_start.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //刷新数据
    private void setGridView() {
        getDates();
        gridView.reflashAdapter(wordButtonList);
    }

    @Override
    protected void onPause() {
        super.onPause();
        iv_game_round.clearAnimation();
        iv_game_pin.clearAnimation();
    }

    //获得所有选择文字
    public void getDates() {
        String[] word = generateWords();
        for (int i = 0; i < 24; i++) {
            WordButton button = new WordButton();
            button.word = word[i];
            wordButtonList.add(button);
        }
    }

    //设置歌名长度
    public void setMusicNameDates() {

        for (int i = 0; i < songlength; i++) {

            View view = inflater.inflate(R.layout.gridview_item, null);
            final WordButton button = new WordButton();
            button.mViewButton = (Button) view.findViewById(R.id.bt_gridview_item);
            button.mViewButton.setBackgroundResource(R.drawable.game_wordblank);
            button.mViewButton.setText(button.word);
            button.mViewButton.setTextColor(Color.WHITE);

            button.mViewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    button.mViewButton.setText(null);
                    button.word = null;

                    wordButtonList.get(button.id).mViewButton.setVisibility(View.VISIBLE);
                }
            });
            wordButtonListName.add(button);
        }
    }

    //设置歌曲框
    private void setNameseclect() {
        setMusicNameDates();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100, 100);

        for (int i = 0; i < wordButtonListName.size(); i++) {
            ll_music_name.addView(wordButtonListName.get(i).mViewButton, params);
        }

    }

    //点击选择文字事件
    @Override
    public void OnButtonClickLister(WordButton wordButton) {

        for(int i = 0;i < songlength;i++){
            if (wordButtonListName.get(i).word == null){
                wordButtonListName.get(i).mViewButton.setVisibility(View.VISIBLE);
                wordButtonListName.get(i).word = wordButton.word;
                wordButtonListName.get(i).mViewButton.setText(wordButton.word);
                wordButtonListName.get(i).id = wordButton.id;

                wordButton.mViewButton.setVisibility(View.INVISIBLE);
                wordButton.isvisable = false;

                break;
            }
        }

        switch (getMusicState()){
            case MUSIC_STATE_BUG:

                for (int i = 0 ;i<wordButtonListName.size();i++){
                    wordButtonListName.get(i).mViewButton.setTextColor(Color.WHITE);
                }
                break;
            case MUSIC_STATE_RIGHT:
                Toast.makeText(MainActivity.this,"right",Toast.LENGTH_LONG).show();

                break;
            case MUSIC_STATE_ERROR:
                setStateError();
                break;
        }

    }

    /*
    * 当答案错了的时候，文字闪烁，并显示红色
    * */

    private void setStateError() {
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            int count = 5;

            @Override
            public void run() {
                while (count >=0){
                    Log.d("tang","timer"+count);
                    count -- ;
                    mHandler.sendEmptyMessage(MSG_ERROR);
                    iswhite = !iswhite;
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        },0,300);

    }

    private boolean iswhite = false;
    private final static int MSG_ERROR =0x123;
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_ERROR){
                Log.d("tang","message");
                for (int i = 0 ;i<wordButtonListName.size();i++){
                    wordButtonListName.get(i).mViewButton.setTextColor(iswhite?Color.WHITE:Color.RED);
                }
            }
        }
    };

    /**
     * 生成随机汉字
     *
     * @return
     */
    private char getRandomChar() {
        String str = "";
        int hightPos;
        int lowPos;

        Random random = new Random();

        hightPos = (176 + Math.abs(random.nextInt(39)));
        lowPos = (161 + Math.abs(random.nextInt(93)));

        byte[] b = new byte[2];
        b[0] = (Integer.valueOf(hightPos)).byteValue();
        b[1] = (Integer.valueOf(lowPos)).byteValue();

        try {
            str = new String(b, "GBK");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return str.charAt(0);
    }

    /**
     * 生成所有的待选文字
     *
     * @return
     */
    private String[] generateWords() {
        Random random = new Random();

        String[] words = new String[MyGridView.COUNTS_WORDS];

        // 存入歌名
        for (int i = 0; i < songlength; i++) {
            words[i] = currentSong.substring(i,i+1);
        }

        // 获取随机文字并存入数组
        for (int i = songlength;
             i < MyGridView.COUNTS_WORDS; i++) {
            words[i] = getRandomChar() + "";
        }

        // 打乱文字顺序：首先从所有元素中随机选取一个与第一个元素进行交换，
        // 然后在第二个之后选择一个元素与第二个交换，知道最后一个元素。
        // 这样能够确保每个元素在每个位置的概率都是1/n。
        for (int i = MyGridView.COUNTS_WORDS - 1; i >= 0; i--) {
            int index = random.nextInt(i + 1);

            String buf = words[index];
            words[index] = words[i];
            words[i] = buf;
        }

        return words;
    }

    //判断歌曲文字的状态

    private int getMusicState(){

        for (int i = 0; i < songlength; i++){
            if (wordButtonListName.get(i).word == null){
                return MUSIC_STATE_BUG;
            }
        }

        int j =0;
        for (int i = 0; i < songlength; i++){

            if (wordButtonListName.get(i).word.equals(currentSong.substring(i,i+1))){
                j++;
            }
        }


        return j == songlength?MUSIC_STATE_RIGHT:MUSIC_STATE_ERROR;
    }

}
