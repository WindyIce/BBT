package com.windyice.bbt;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import java.util.Random;

import static android.R.attr.src;
import static com.iflytek.sunflower.config.a.C;
import static com.windyice.bbt.R.raw.xi;

public class MusicService extends Service {
    private MediaPlayer mediaPlayer;
    private final int numOfMusics=4;
    private Random random=new Random();


    public void play(){
        mediaPlayer.reset();
        setMediaPlayerRandom();
        mediaPlayer.start();
    }

    public MusicService() {
    }


    private void setMediaPlayerRandom(){
        //TODO make sure that the next music won't be the same as the last one
        int pos= random.nextInt(numOfMusics);
        switch (pos){
            case 0:
                mediaPlayer=MediaPlayer.create(this,R.raw.xi);
                break;
            case 1:
                mediaPlayer=MediaPlayer.create(this,R.raw.nameoflife);
                break;
            case 2:
                mediaPlayer=MediaPlayer.create(this,R.raw.always);
                break;
            case 3:
                mediaPlayer=MediaPlayer.create(this,R.raw.tripl);
                break;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        if(mediaPlayer==null){

            //mediaPlayer=MediaPlayer.create(this,R.raw.xi);
            setMediaPlayerRandom();
            mediaPlayer.setLooping(false);
            mediaPlayer.start();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }
}
