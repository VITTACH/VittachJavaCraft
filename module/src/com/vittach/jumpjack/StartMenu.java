package com.vittach.jumpjack;

import com.vittach.jumpjack.framework.ImageHandler;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class StartMenu extends InputListener {
    ImageHandler background;
    private ImageHandler ilogo;
    private Sprite sprite;
    SpriteBatch spritewnd;
    public int DWNKey = -1;
    private ImageHandler ipaper;
    public ScreenButton game;
    public ScreenButton load;
    public ScreenButton exit;

    @Override
    public boolean
    touchDown(int x, int y, int id, int b) {
        if (load.MyTouch_Down(x, y)) DWNKey = 2;
        if (game.MyTouch_Down(x, y)) DWNKey = 1;
        return true;
    }

    public StartMenu() {
        ilogo = new ImageHandler();
        ilogo.load("ui/jumpJackLogo.png");

        game = new ScreenButton();
        game.choice = new ImageHandler();
        game.choice.load("ui/startChoice.png");
        game.background.load("ui/startButton.png");
        game.backgroundHandler.blit(game.background);
        game.setPosition(Preference.windowWidth / 2 - game.background.getWidth() / 2, 128);
        game.font.load("jumpjack.ttf");
        game.textY = 19;
        game.textX = 84;
        game.textMessage = "idpary";
        game.font.setPixelSizes(12);

        load = new ScreenButton();
        load.choice = new ImageHandler();
        load.choice.load("ui/startChoice.png");
        load.background.load("ui/startButton.png");
        load.backgroundHandler.blit(load.background);
        load.setPosition(Preference.windowWidth / 2 - load.background.getWidth() / 2, 98);
        load.font = game.font;
        load.textY = 19;
        load.textX = 74;
        load.textMessage = "hadpshja";

        exit = new ScreenButton();
        exit.choice = new ImageHandler();
        exit.choice.load("ui/startChoice.png");
        exit.background.load("ui/startButton.png");
        exit.backgroundHandler.blit(exit.background);
        exit.setPosition(Preference.windowWidth / 2 - exit.background.getWidth() / 2, 68);
        exit.font = game.font;
        exit.textY = 19;
        exit.textX = 74;
        exit.textMessage = "n acrnpf";

        ipaper = new ImageHandler();
        ipaper.load("ui/foreground.png");

        background = new ImageHandler();
        spritewnd = new SpriteBatch();
        background.load("ui/background.png");
        background.blit(54, 27, ipaper);
        background.blit(120, 164, ilogo);

        sprite = background.flip();
    }

    public void Display(Viewport viewport) {
        viewport.apply();
        spritewnd.setProjectionMatrix(viewport.getCamera().combined);
        spritewnd.begin();
        sprite.draw(spritewnd);
        spritewnd.end();
        game.MyDisplay(viewport);
        load.MyDisplay(viewport);
        exit.MyDisplay(viewport);
    }

    public void dispose() {
        background.dispose();
        spritewnd.dispose();
        ipaper.dispose();
        game.dispose();
        load.dispose();
        exit.dispose();
    }
}
