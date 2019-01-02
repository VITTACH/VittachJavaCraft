package com.vittach.jumpjack.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class FontHandler {
    private int size;
    private BitmapFont font;
    private FreeTypeFontGenerator freeTypeFontGenerator;

    public void setPixelSizes(int size) {
        font = freeTypeFontGenerator.generateFont(this.size = size);
    }

    public void load(String path) {
        freeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal(path));
    }

    public FontHandler clone(FontHandler fontHandler) {
        size = fontHandler.size;
        font = fontHandler.font;
        return this;
    }

    public BitmapFont getFont() {
        return font;
    }

    public void dispose() {
        freeTypeFontGenerator.dispose();
    }

    public int getSize() {
        return size;
    }
}