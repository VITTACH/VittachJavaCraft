package com.vittach.jumpjack;

import com.vittach.jumpjack.framework.FontHandler;
import com.vittach.jumpjack.framework.ImageHandler;
import com.vittach.jumpjack.framework.MyTimer;
import com.vittach.jumpjack.framework.ColorImpl;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DeathWin {
    private ColorImpl color;
    private SpriteBatch spriteBatch;
    private FontHandler fontHandler;
    private ImageHandler titleImage;
    private MyTimer timer;
    private Sprite sprite;
    private long fallTime;

    void dispose() {
        titleImage.dispose();
        spriteBatch.dispose();
    }

    DeathWin() {
        fontHandler = new FontHandler();
        timer = new MyTimer();
        timer.visible = true;

        fontHandler.load("jumpjack.ttf");
        fontHandler.setPixelSizes(14);

        titleImage = new ImageHandler();
        titleImage.load("ui/title.png");
        color = new ColorImpl(0, 0, 0);
        spriteBatch = new SpriteBatch();
    }

    void printMeText() {
        if (!timer.isActive()) {
            if (!timer.visible) {
                timer.visible = true;
                JJEngine.human.deathWinState = 0;
                JJEngine.human.pressedKey.add(131);
                JJEngine.human.fallingTimer.set_Start(JJEngine.human.fallingTimer.getMe_Start() + System.currentTimeMillis() - fallTime);

                return;
            }

            Preference.screenWindow.clear();
            Preference.inputListener.clnProces();
            fallTime = System.currentTimeMillis();
            Preference.screenWindow.blit(Preference.screenWindow.getWidth() / 2 - titleImage.getWidth() / 2,
                    Preference.screenWindow.getHeight() - titleImage.getHeight() - 5, titleImage);

            if (JJEngine.human.deathWinState == 2) {
                Preference.screenWindow.fontPrint(fontHandler,
                        Preference.screenWindow.getWidth() / 2 - fontHandler.getSize() * 9 / 2,
                        Preference.screenWindow.getHeight() - titleImage.getHeight() / 2 + 3, "Rx Cxidpak!", color);
            }

            if (JJEngine.human.deathWinState == 1) {
                Preference.screenWindow.fontPrint(fontHandler,
                        Preference.screenWindow.getWidth() / 2 - fontHandler.getSize() * 9 / 2,
                        Preference.screenWindow.getHeight() - titleImage.getHeight() / 2 + 3, "Rx Opnidpak", color);
            }

            sprite = Preference.screenWindow.flip();
            timer.start(2000);
        }
    }

    void Display(Viewport view) {
        view.apply();
        printMeText();
        spriteBatch.setProjectionMatrix(view.getCamera().combined);
        spriteBatch.begin();
        sprite.draw(spriteBatch);
        spriteBatch.end();
    }
}
