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
    private ImageHandler title;
    private SpriteBatch spriteBatch;
    private FontHandler font;
    private MyTimer timer;
    private Sprite sprite;

    DeathWin() {
        font = new FontHandler();
        timer = new MyTimer();
        timer.visible = true;

        spriteBatch = new SpriteBatch();
        color = new ColorImpl(0, 0, 0);

        title = new ImageHandler();
        title.load("ui/title.png");
        font.load("jumpjack.ttf");
        font.setPixelSizes(14);
    }

    void display(Viewport view) {
        view.apply();
        render();
        spriteBatch.setProjectionMatrix(view.getCamera().combined);
        spriteBatch.begin();
        sprite.draw(spriteBatch);
        spriteBatch.end();
    }

    private void render() {
        if (!timer.isActive()) {

            Preference.getInstance().screenWindow.clear();
            Preference.getInstance().inputListener.cleanProcesses();

            Preference.getInstance().screenWindow.blit(
                    Preference.getInstance().screenWindow.getWidth() / 2 - title.getWidth() / 2,
                    Preference.getInstance().screenWindow.getHeight() - title.getHeight() - 5, title);

            if (JJEngine.getInstance().human.deathWinState == 2) {
                Preference.getInstance().screenWindow.fontPrint(font,
                        Preference.getInstance().screenWindow.getWidth() / 2 - font.getSize() * 9 / 2,
                        Preference.getInstance().screenWindow.getHeight() - title.getHeight() / 2 + 3,
                        "Rx Cxidpak!", color);
            }

            if (JJEngine.getInstance().human.deathWinState == 1) {
                Preference.getInstance().screenWindow.fontPrint(font,
                        Preference.getInstance().screenWindow.getWidth() / 2 - font.getSize() * 9 / 2,
                        Preference.getInstance().screenWindow.getHeight() - title.getHeight() / 2 + 3,
                        "Rx Opnidpak", color);
            }

            sprite = Preference.getInstance().screenWindow.render();
            timer.start(2000);
        }
    }

    void dispose() {
        title.dispose();
        spriteBatch.dispose();
    }
}
