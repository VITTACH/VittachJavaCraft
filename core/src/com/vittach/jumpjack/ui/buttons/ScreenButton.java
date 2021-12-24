package com.vittach.jumpjack.ui.buttons;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.vittach.jumpjack.engine.MainEngine;
import com.vittach.jumpjack.Preferences;
import com.vittach.jumpjack.utils.ColorImpl;
import com.vittach.jumpjack.utils.FontHandler;
import com.vittach.jumpjack.utils.ImageHandler;
import com.vittach.jumpjack.ui.InputListener;

/**
 * Created by ZHARIKOV VITALIY at 12.02.2016.
 */

public class ScreenButton extends InputListener {
    private boolean isShowForeground = false;
    private String message;
    private int textX;
    private int textY;

    public FontHandler font = new FontHandler();
    public ColorImpl fontColor = new ColorImpl(1, 1, 1);
    public ImageHandler foreground = new ImageHandler();
    public ImageHandler screen = new ImageHandler();
    public ImageHandler choice;

    public float x;
    public float y;

    private final Preferences preferenceInstance = Preferences.getInstance();
    private final MainEngine engineInstance = MainEngine.getInstance();

    @Override
    public boolean mouseMoved(int x, int y) {
        if (choice != null) {
            if (touchDown(x, y, -1)) {
                if (isShowForeground) {
                    isShowForeground = false;
                    screen.clear();
                    screen.blit(choice);
                }
            } else if (!isShowForeground) {
                screen.clear();
                screen.blit(foreground);
                isShowForeground = true;
            }
            fontPrint();
        }
        return true;
    }

    @Override
    public boolean touchDragged(int x, int y, int id) {
        mouseMoved(x, y);
        return true;
    }

    public void onClicked(final int x, final int y, final ButtonClickListener listener) {
        final boolean isInArea = touchDown(x, y, 0);
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(150L);
                    if (isInArea) {
                        listener.onClicked();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void draw(SpriteBatch spriteBatch) {
        Sprite sprite = screen.render();
        sprite.setPosition(x, y);
        sprite.draw(spriteBatch);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getWidth() {
        return foreground.getWidth();
    }

    public float getHeight() {
        return foreground.getHeight();
    }

    public void setMessage(int textX, int textY, String message) {
        this.textX = textX;
        this.textY = textY;
        this.message = message;
        fontPrint();
    }

    private boolean touchDown(int x, int y, int id) {
        if (choice != null && !isShowForeground && id >= 0) {
            isShowForeground = true;
            screen.clear();
            screen.blit(foreground);
            fontPrint();
        }

        float scaleX = preferenceInstance.screenWidth / (float) engineInstance.renderWidth;
        float scaleY = preferenceInstance.screenHeight / (float) engineInstance.renderHeight;
        x -= (preferenceInstance.displayWidth - preferenceInstance.screenWidth) / 2;
        y -= (preferenceInstance.displayHeight - preferenceInstance.screenHeight) / 2;

        return x >= this.x * scaleX
            && x <= this.x * scaleX + foreground.getWidth() * scaleX
            && y >= (preferenceInstance.screenHeight - this.y * scaleY) - foreground.getHeight() * scaleY
            && y <= (preferenceInstance.screenHeight - this.y * scaleY);
    }

    private void fontPrint() {
        screen.fontPrint(font, textX, textY, message, fontColor);
    }

    public void dispose() {
        foreground.dispose();
        screen.dispose();
    }
}
