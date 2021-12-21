package com.vittach.jumpjack.ui.buttons;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.vittach.jumpjack.MainEngine;
import com.vittach.jumpjack.Preferences;
import com.vittach.jumpjack.framework.ColorImpl;
import com.vittach.jumpjack.framework.FontHandler;
import com.vittach.jumpjack.framework.ImageHandler;
import com.vittach.jumpjack.ui.InputListener;

/**
 * Created by ZHARIKOV VITALIY at 12.02.2016.
 */

public class ScreenButton extends InputListener {
    public FontHandler font = new FontHandler();
    private SpriteBatch spriteBatch = new SpriteBatch();
    public String message;
    private boolean hasBackground;
    public int textX;
    public int textY;
    public ColorImpl color = new ColorImpl(1, 1, 1);
    public ImageHandler selectedBoxImage = new ImageHandler();
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
                if (hasBackground) {
                    screen.clear();
                    hasBackground = false;
                    screen.blit(choice);
                }
            } else if (!hasBackground) {
                screen.clear();
                screen.blit(selectedBoxImage);
                hasBackground = true;
            }
        }
        return true;
    }

    @Override
    public boolean touchDragged(int x, int y, int id) {
        mouseMoved(x, y);
        return true;
    }

    public boolean touchDown(int x, int y) {
        return touchDown(x, y, 0);
    }

    public boolean touchDown(int x, int y, int id) {
        if (choice != null && !hasBackground && id >= 0) {
            screen.clear();
            screen.blit(selectedBoxImage);
            hasBackground = true;
        }

        float scaleX = preferenceInstance.screenWidth / (float) engineInstance.renderWidth;
        float scaleY = preferenceInstance.screenHeight / (float) engineInstance.renderHeight;
        x -= (preferenceInstance.displayWidth - preferenceInstance.screenWidth) / 2;
        y -= (preferenceInstance.displayHeight - preferenceInstance.screenHeight) / 2;

        return x >= this.x * scaleX
            && x <= this.x * scaleX + selectedBoxImage.getWidth() * scaleX
            && y >= (preferenceInstance.screenHeight - this.y * scaleY) - selectedBoxImage.getHeight() * scaleY
            && y <= (preferenceInstance.screenHeight - this.y * scaleY);
    }

    public void draw(Viewport viewport) {
        if (message != null) {
            screen.fontPrint(font, textX, textY, message, color);
        }

        Sprite sprite = screen.render();
        sprite.setPosition(x, y);

        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        spriteBatch.begin();
        sprite.draw(spriteBatch);
        spriteBatch.end();
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getWidth() {
        return selectedBoxImage.getWidth();
    }

    public float getHeight() {
        return selectedBoxImage.getHeight();
    }

    public void dispose() {
        spriteBatch.dispose();
        selectedBoxImage.dispose();
        screen.dispose();
    }
}
