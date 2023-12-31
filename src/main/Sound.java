package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;

public class Sound {
    Clip clip;
    URL[] soundURL = new URL[30];
    FloatControl fc;
    int volumeScale = 3;
    float volume;

    public Sound(){
        soundURL[0] = getClass().getClassLoader().getResource("Sound/BlueBoyAdventure.wav");
        soundURL[1] = getClass().getClassLoader().getResource("Sound/coin.wav");
        soundURL[2] = getClass().getClassLoader().getResource("Sound/powerup.wav");
        soundURL[3] = getClass().getClassLoader().getResource("Sound/unlock.wav");
        soundURL[4] = getClass().getClassLoader().getResource("Sound/fanfare.wav");
        soundURL[5] = getClass().getClassLoader().getResource("Sound/hitmonster.wav");
        soundURL[6] = getClass().getClassLoader().getResource("Sound/receivedamage.wav");
        soundURL[7] = getClass().getClassLoader().getResource("Sound/swingweapon.wav");
        soundURL[8] = getClass().getClassLoader().getResource("Sound/levelup.wav");
        soundURL[9] = getClass().getClassLoader().getResource("Sound/cursor.wav");
        soundURL[10] = getClass().getClassLoader().getResource("Sound/burning.wav");
        soundURL[11] = getClass().getClassLoader().getResource("Sound/cuttree.wav");
        soundURL[12] = getClass().getClassLoader().getResource("Sound/gameover.wav");
        soundURL[13] = getClass().getClassLoader().getResource("Sound/stairs.wav");

    }

    public void setFile(int i){
        try{
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
            fc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            checkVolume();
        }
        catch(Exception e){

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

    public void checkVolume(){
        switch(volumeScale){ // volume goes from -80f(no sound) to 6f
            case 0 -> volume = -80f;
            case 1 -> volume = -20f;
            case 2 -> volume = -12f;
            case 3 -> volume = -5f;
            case 4 -> volume =  1f;
            case 5 -> volume = 6f;
        }
        fc.setValue(volume);
    }

}
