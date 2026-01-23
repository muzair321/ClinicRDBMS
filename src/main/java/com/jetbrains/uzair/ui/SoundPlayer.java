package com.jetbrains.uzair.ui;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

public class SoundPlayer {
    public static void play(String soundFile) throws RuntimeException{
        try {
            InputStream audioSrc = SoundPlayer.class.getResourceAsStream("/sounds/" + soundFile);
            assert audioSrc != null;
            AudioInputStream ais = AudioSystem.getAudioInputStream(audioSrc);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            throw new RuntimeException("Error During Audio Play: " + e.getMessage());
        }
    }
}
