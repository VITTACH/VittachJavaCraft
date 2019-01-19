package com.vittach.jumpjack.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class FontHandler {
    private int size;
    private BitmapFont bitmapFont;
    private FreeTypeFontGenerator font;

    public void dispose() {
        font.dispose();
    }

    public void setPixelSize(int size) {
        bitmapFont = font.generateFont(this.size = size);
    }

    public void load(String path) {
        font = new FreeTypeFontGenerator(Gdx.files.internal(path));
    }

    public BitmapFont getBitmapFont() {
        return bitmapFont;
    }

    public int getSize() {
        return size;
    }
}