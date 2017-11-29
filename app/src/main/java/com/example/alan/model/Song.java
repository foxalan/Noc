package com.example.alan.model;

/**
 * Created by alan on 2017/3/22.
 */

public class Song {
    private int songLength;
    private String songName;
    private String songFilename;

    public int getSongLength() {
        return songLength;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
        this.songLength = songName.length();
    }

    public String getSongFilename() {
        return songFilename;
    }

    public void setSongFilename(String songFilename) {
        this.songFilename = songFilename;
    }
}
