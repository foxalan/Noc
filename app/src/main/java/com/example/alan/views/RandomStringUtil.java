package com.example.alan.views;

import com.example.alan.beans.Const;

import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * Created by Alan on 2017/12/6.
 */

public class RandomStringUtil {

    public static String[] generateWords(String currentSong,int songLength) {
        Random random = new Random();

        String[] words = new String[Const.TOTAL_WORD_SIZE];

        // 存入歌名
        for (int i = 0; i < songLength; i++) {
            words[i] = currentSong.substring(i, i + 1);
        }

        // 获取随机文字并存入数组
        for (int i = songLength;
             i < Const.TOTAL_WORD_SIZE; i++) {
            words[i] = getRandomChar() + "";
        }

        // 打乱文字顺序：首先从所有元素中随机选取一个与第一个元素进行交换，
        // 然后在第二个之后选择一个元素与第二个交换，知道最后一个元素。
        // 这样能够确保每个元素在每个位置的概率都是1/n。
        for (int i = Const.TOTAL_WORD_SIZE - 1; i >= 0; i--) {
            int index = random.nextInt(i + 1);

            String buf = words[index];
            words[index] = words[i];
            words[i] = buf;
        }

        return words;
    }

    /**
     * 生成随机汉字
     *
     * @return
     */
    private static char getRandomChar() {
        String str = "";
        int highPos;
        int lowPos;

        Random random = new Random();

        highPos = (176 + Math.abs(random.nextInt(39)));
        lowPos = (161 + Math.abs(random.nextInt(93)));

        byte[] b = new byte[2];
        b[0] = (Integer.valueOf(highPos)).byteValue();
        b[1] = (Integer.valueOf(lowPos)).byteValue();

        try {
            str = new String(b, "GBK");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return str.charAt(0);
    }
}
