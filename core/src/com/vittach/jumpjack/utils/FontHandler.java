package com.vittach.jumpjack.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class FontHandler {
    private FreeTypeFontGenerator.FreeTypeFontParameter params;
    private BitmapFont bitmapFont;
    private FreeTypeFontGenerator font;

    public void dispose() {
        font.dispose();
    }

    public void setPixelSize(int size) {
        params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = size;
        bitmapFont = font.generateFont(params);
    }

    public void load(String path) {
        font = new FreeTypeFontGenerator(Gdx.files.internal(path));
    }

    public BitmapFont getBitmapFont() {
        return bitmapFont;
    }

    public int getSize() {
        return params.size;
    }
}