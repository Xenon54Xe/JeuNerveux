package com.example.app;

import com.example.app.utils.FileUtils;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.IOException;
import java.net.URL;

public class Sound {

    // STATIC
    public final static int INTRO_SCENE = 0;

    // CLASS VARIABLES
    Clip clip;
    URL[] soundURL = new URL[30];


    public Sound() {

        soundURL[INTRO_SCENE] = getClass().getResource("/sounds/introscene.wav");
    }

    public void setFile(int i){
        try{
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play(){

        clip.start();
    }

    public void loop(){

        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop(){

        clip.stop();
    }


}
