package com.vittach.jumpjack;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.vittach.jumpjack.engine.InputListener;
import com.vittach.jumpjack.engine.ScreenButton;
import com.vittach.jumpjack.framework.ImageHandler;

public class StartMenu extends InputListener {
    public com.vittach.jumpjack.engine.ScreenButton gameButton;
    public com.vittach.jumpjack.engine.ScreenButton loadButton;
    public com.vittach.jumpjack.engine.ScreenButton exitButton;

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
        exitButton.dispose();
    }

    public StartMenu() {
        ImageHandler logoImage = new ImageHandler();
        logoImage.load("ui/jumpJackLogo.png");

        gameButton = new com.vittach.jumpjack.engine.ScreenButton();
        gameButton.choice = new ImageHandler();
        gameButton.choice.load("ui/startChoice.png");
        gameButton.background.load("ui/startButton.png");
        gameButton.screen.blit(gameButton.background);
        gameButton.setPosition(MainEngine.getInstance().renderWidth / 2 - gameButton.getWidth() / 2, 128);
        gameButton.font.load("jumpjack.ttf");
        gameButton.textY = 19;
        gameButton.textX = 84;
        gameButton.message = "idpary";
        gameButton.font.setPixelSize(12);

        loadButton = new com.vittach.jumpjack.engine.ScreenButton();
        loadButton.choice = new ImageHandler();
        loadButton.choice.load("ui/startChoice.png");
        loadButton.background.load("ui/startButton.png");
        loadButton.screen.blit(loadButton.background);
        loadButton.setPosition(MainEngine.getInstance().renderWidth / 2 - loadButton.getWidth() / 2, 98);
        loadButton.font = gameButton.font;
        loadButton.textY = 19;
        loadButton.textX = 74;
        loadButton.message = "hadpshja";

        exitButton = new ScreenButton();
        exitButton.choice = new ImageHandler();
        exitButton.choice.load("ui/startChoice.png");
        exitButton.background.load("ui/startButton.png");
        exitButton.screen.blit(exitButton.background);
        exitButton.setPosition(MainEngine.getInstance().renderWidth / 2 - exitButton.getWidth() / 2, 68);
        exitButton.font = gameButton.font;
        exitButton.textY = 19;
        exitButton.textX = 74;
        exitButton.message = "n acrnpf";

        paperImage = new ImageHandler();
        paperImage.load("ui/foreground.png");

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
        gameButton.show(viewport);
        loadButton.show(viewport);
        exitButton.show(viewport);
    }
}
