package com.vittach.jumpjack.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class PlayerMusic {
    private Music sound;

    public void stop() {
        sound.stop();
    }

    public void play() {
        sound.play();
    }

    public void load(String path) {
        sound = Gdx.audio.newMusic(Gdx.files.internal(path));
    }

    public void setVolume(float volume) {
        sound.setVolume(volume);
    }

    public void setLoop(boolean loop) {
        sound.setLooping(loop);
    }

    public PlayerMusic clone(PlayerMusic sounds) {
        sound = sounds.sound;
        return this;
    }

    public boolean getStatus() {
        return sound.isPlaying();
    }

    public float getVolume() {
        return sound.getVolume();
    }

    public void pause() {
        sound.pause();
    }
}