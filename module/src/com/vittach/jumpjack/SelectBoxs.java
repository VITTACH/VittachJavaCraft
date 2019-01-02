package com.vittach.jumpjack;

import com.vittach.jumpjack.framework.ImageHandler;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SelectBoxs extends InputListener {
    private boolean check;
    private Sprite sprite;
    int oldi = 0, oldj = 0;
    private ImageHandler ipaper;
    private ImageHandler screen;
    ImageHandler select, blocks;
    public ScreenButton isave;
    public ScreenButton iload;
    public ScreenButton start;
    public ScreenButton iexit;
    int width, height, DWNKey;
    ImageHandler background, cellBoxs;
    SpriteBatch spritewind;
    int i, j, widt = 10, heigh = 4;

    @Override
    public boolean keyUp(int key_U) {
        DWNKey = -1;
        return true;
    }

    @Override
    public boolean keyDown(int keyD) {
        DWNKey = keyD;
        return true;
    }

    SelectBoxs() {
        cellBoxs = new ImageHandler();
        screen = new ImageHandler();
        ipaper = new ImageHandler();
        blocks = new ImageHandler();
        select = new ImageHandler();
        width = Preference.windowWidth / 2;
        height = Preference.windowHeight / 2;

        iexit = new ScreenButton();
        start = new ScreenButton();
        iload = new ScreenButton();
        isave = new ScreenButton();
        background = new ImageHandler();

        iexit.choice = new ImageHandler();
        isave.choice = new ImageHandler();
        iload.choice = new ImageHandler();
        start.choice = new ImageHandler();

        iexit.textX = 16;
        iexit.textY = 19;
        iload.textY = 19;
        start.textY = 19;
        start.textX = 13;
        isave.textY = 19;
        isave.textX = 6;
        iload.textX = 6;

        start.setPosition(298, 205);
        iload.setPosition(194, 45);
        iexit.setPosition(298, 45);
        isave.setPosition(90, 45);

        start.font.load("jumpjack.ttf");
        start.font.setPixelSizes(12);

        start.textMessage = "idpary";
        iload.font = start.font;
        iload.textMessage = "pfqrapr";
        isave.font = start.font;
        isave.textMessage = "qnupami";
        iexit.font = start.font;
        iexit.textMessage = "cxune";

        start.choice.load("ui/btnForeground.png");
        start.background.load("ui/btnBackground.png");
        start.backgroundHandler.blit(start.background);
        iload.choice.load("ui/btnForeground.png");
        iload.background.load("ui/btnBackground.png");
        iload.backgroundHandler.blit(iload.background);
        isave.choice.load("ui/btnForeground.png");
        isave.background.load("ui/btnBackground.png");
        isave.backgroundHandler.blit(isave.background);
        iexit.choice.load("ui/btnForeground.png");
        iexit.background.load("ui/btnBackground.png");
        iexit.backgroundHandler.blit(iexit.background);

        background.load("ui/background.png");
        cellBoxs.load("ui/cellBox.png");
        blocks.load("ui/allBlocksSprite.png");
        ipaper.load("ui/foreground.png");
        select.load("ui/blockSelector.png");
        spritewind = new SpriteBatch();

        background.blit(54, 27, ipaper);
        printScreen();
    }

    @Override
    public boolean mouseMoved(int x, int y) {
        float scaleX = cellBoxs.getWidth() * Preference.screenWidth / Preference.windowWidth;
        float scaleY = cellBoxs.getHeight() * Preference.screenHeight / Preference.windowHeight;
        y = Preference.height - y - (Preference.height - Preference.screenHeight) / 2;
        x -= (Preference.width - Preference.screenWidth) / 2;

        for (i = 0; i < widt; i++)
            for (j = 0; j < heigh; j++)
                if (x > Preference.screenWidth / 2 + (i - widt / 2) * scaleX &&
                        x <= Preference.screenWidth / 2 + (i - widt / 2.0f) * scaleX + scaleX &&
                        y > Preference.screenHeight / 2 + (j - heigh / 2) * scaleY &&
                        y <= Preference.screenHeight / 2 + (j - heigh / 2.0) * scaleY + scaleY) {
                    check = true;
                    if (oldi != i || oldj != j) {
                        screen.clear();
                        screen.blit(background);
                        screen.blit((i - widt / 2) * cellBoxs.getWidth() + this.width,
                                this.height + (j - heigh / 2) * cellBoxs.getHeight() - 1, select);
                        oldi = i;
                        oldj = j;
                        sprite = screen.flip();
                    } else {
                        i = widt;
                        break;
                    }
                }
        return true;
    }

    private void printScreen() {
        for (i = 0; i < widt; i++)
            for (j = 0; j < heigh; j++) {
                background.blit(width + (i - widt / 2) * cellBoxs.getWidth(),
                        height + (j - heigh / 2) * cellBoxs.getHeight(), cellBoxs);
                background.blit(
                        width + (i - widt / 2) * cellBoxs.getWidth() +
                                (cellBoxs.getWidth() - blocks.getWidth() / widt) / 2,
                        height + (j - heigh / 2) * cellBoxs.getHeight() +
                                (cellBoxs.getHeight() - blocks.getHeight() / heigh) / 2,
                        blocks, i * blocks.getWidth() / widt, j *
                                blocks.getHeight() / heigh, blocks.getWidth() / widt,
                        blocks.getHeight() / heigh);
            }
        screen.blit(background);
        sprite = screen.flip();
    }

    @Override
    public boolean touchDragged(int x, int y, int touchId) {
        return mouseMoved(x, y);
    }

    @Override
    public boolean touchDown(int x, int y, int id, int button) {
        check = false;
        mouseMoved(x, y);
        if (check) {
            JJEngine.world.mouseX = oldi;
            JJEngine.world.mouseY = oldj;
            DWNKey = 131;
        }
        if (start.MyTouch_Down(x, y))
            DWNKey = 131;
        if (iload.MyTouch_Down(x, y)) {
            JJEngine.fileLoader.loadWorld();
            DWNKey = 131;
        }
        if (isave.MyTouch_Down(x, y))
            JJEngine.fileLoader.saveWorld();
        if (iexit.MyTouch_Down(x, y))
            DWNKey = 67;
        return true;
    }

    void Display(Viewport view) {
        view.apply();
        spritewind.setProjectionMatrix(view.getCamera().combined);
        spritewind.begin();
        sprite.draw(spritewind);
        spritewind.end();
        start.MyDisplay(view);
        iload.MyDisplay(view);
        isave.MyDisplay(view);
        iexit.MyDisplay(view);
    }

    void dispose() {
        screen.dispose();
        background.dispose();
        spritewind.dispose();
        isave.dispose();
        iload.dispose();
        iexit.dispose();
        start.dispose();
    }
}
