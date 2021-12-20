package com.vittach.jumpjack.ui.screen.menu;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.vittach.jumpjack.MainEngine;
import com.vittach.jumpjack.ui.InputListener;
import com.vittach.jumpjack.ui.buttons.ScreenButton;
import com.vittach.jumpjack.framework.ImageHandler;

public class StartMenu extends InputListener {
    public ScreenButton gameButton;
    public ScreenButton loadButton;
    public ScreenButton moreButton;

    public int pressedKey = -1;
    private Sprite sprite;
    private SpriteBatch spriteWindow;

    private ImageHandler background;
    private ImageHandler paperImage;

    @Override
    public boolean touchDown(int x, int y, int id, int b) {
        if (loadButton.touchDown(x, y)) pressedKey = 2;
        if (gameButton.touchDown(x, y)) pressedKey = 1;
        return true;
    }

    public void dispose() {
        background.dispose();
        spriteWindow.dispose();
        paperImage.dispose();
        gameButton.dispose();
        loadButton.dispose();
        moreButton.dispose();
    }

    public StartMenu() {
        ImageHandler logoImage = new ImageHandler();
        logoImage.load("ui/logo.png");

        gameButton = new ScreenButton();
        gameButton.choice = new ImageHandler();
        gameButton.choice.load("ui/button_selected.png");
        gameButton.selectedBoxImage.load("ui/button_default.png");
        gameButton.screen.blit(gameButton.selectedBoxImage);
        gameButton.setPosition(MainEngine.getInstance().renderWidth / 2 - gameButton.getWidth() / 2, 128);
        gameButton.font.load("jumpjack.ttf");
        gameButton.textY = 19;
        gameButton.textX = 84;
        gameButton.message = "idpary";
        gameButton.font.setPixelSize(12);

        loadButton = new ScreenButton();
        loadButton.choice = new ImageHandler();
        loadButton.choice.load("ui/button_selected.png");
        loadButton.selectedBoxImage.load("ui/button_default.png");
        loadButton.screen.blit(loadButton.selectedBoxImage);
        loadButton.setPosition(MainEngine.getInstance().renderWidth / 2 - loadButton.getWidth() / 2, 98);
        loadButton.font = gameButton.font;
        loadButton.textY = 19;
        loadButton.textX = 74;
        loadButton.message = "hadpshja";

        moreButton = new ScreenButton();
        moreButton.choice = new ImageHandler();
        moreButton.choice.load("ui/button_selected.png");
        moreButton.selectedBoxImage.load("ui/button_default.png");
        moreButton.screen.blit(moreButton.selectedBoxImage);
        moreButton.setPosition(MainEngine.getInstance().renderWidth / 2 - moreButton.getWidth() / 2, 68);
        moreButton.font = gameButton.font;
        moreButton.textY = 19;
        moreButton.textX = 74;
        moreButton.message = "n acrnpf";

        paperImage = new ImageHandler();
        paperImage.load("ui/paper.png");

        background = new ImageHandler();
        spriteWindow = new SpriteBatch();
        background.load("ui/background.png");
        background.blit(54, 27, paperImage);
        background.blit(120, 164, logoImage);

        sprite = background.render();
    }

    public void display(Viewport viewport) {
        viewport.apply();
        spriteWindow.setProjectionMatrix(viewport.getCamera().combined);
        spriteWindow.begin();
        sprite.draw(spriteWindow);
        spriteWindow.end();
        gameButton.draw(viewport);
        loadButton.draw(viewport);
        moreButton.draw(viewport);
    }
}
