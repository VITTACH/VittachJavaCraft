package com.vittach.jumpjack;

import com.vittach.jumpjack.framework.FontHandler;
import com.vittach.jumpjack.framework.ColorImpl;
import com.vittach.jumpjack.framework.ImageHandler;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by ZHARIKOV VITALIY at 12.02.2016.
 */

public class ScreenButton extends InputListener {
    FontHandler font;
    private SpriteBatch spriteBatch;
    String textMessage;
    private boolean hasBackground;
    int textX;
    int textY;
    ColorImpl color;
    ImageHandler backgroundHandler;
    ImageHandler choice;
    ImageHandler background;
    public int x;
    public int y;

    ScreenButton() {
        font = new FontHandler();
        backgroundHandler = new ImageHandler();
        background = new ImageHandler();
        color = new ColorImpl(1, 1, 1, 1);
        spriteBatch = new SpriteBatch();
    }

    @Override
    public boolean
    mouseMoved(int screenx, int screeny) {
        if (choice != null) {
            if (MyTouch_Down(screenx, screeny, -1)) {
                if (hasBackground) {
                    backgroundHandler.clear();
                    hasBackground = false;
                    backgroundHandler.blit(choice);
                }
            } else if (!hasBackground) {
                backgroundHandler.clear();
                backgroundHandler.blit(background);
                hasBackground = true;
            }
        }
        return true;
    }

    @Override
    public boolean touchDragged(int xpos, int ypos, int TID) {
        mouseMoved(xpos, ypos);
        return true;
    }

    public boolean MyTouch_Down(int my_xposi, int my_yposi) {
        return MyTouch_Down(my_xposi, my_yposi, 0);
    }

    public boolean MyTouch_Down(int xops, int ypos, int id) {
        if (choice != null && !hasBackground && id >= 0) {
            backgroundHandler.clear();
            backgroundHandler.blit(background);
            hasBackground = true;
        }

        float scalex = Preference.screenWidth / (float) Preference.windowWidth;
        float scaley = Preference.screenHeight / (float) Preference.windowHeight;
        xops -= (Preference.width - Preference.screenWidth) / 2;
        ypos -= (Preference.height - Preference.screenHeight) / 2;

        if (xops >= x * scalex && xops <= x * scalex + background.getWidth()
                * scalex && ypos >= (Preference.screenHeight - y * scaley) - background.getHeight()
                * scaley && ypos <= (Preference.screenHeight - y * scaley))
            return true;
        else return false;
    }

    //отображение кнопки на экране
    public void MyDisplay(Viewport viewport) {
        if (textMessage != null) backgroundHandler.fontPrint(font, textX, textY, textMessage, color);
        Sprite sprite = backgroundHandler.flip();
        sprite.setPosition(x, y);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        sprite.draw(spriteBatch);
        spriteBatch.end();
    }

    //установка позиции кнопки
    void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    void dispose() {
        spriteBatch.dispose();
        background.dispose();
        backgroundHandler.dispose();
    }
}
