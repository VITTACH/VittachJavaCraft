package com.vittach.jumpjack;

import com.vittach.jumpjack.framework.ImageHandler;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BlockSelector extends InputListener {
    private boolean check;
    private Sprite sprite;
    private int oldColumn = 0;
    private int oldRow = 0;
    public ScreenButton saveButton;
    public ScreenButton loadButton;
    public ScreenButton startButton;
    public ScreenButton exitButton;
    private float width;
    private float height;
    int pressedKey;
    private ImageHandler screen;
    private ImageHandler select;
    private ImageHandler blocks;
    private ImageHandler background;
    private ImageHandler cellBoxes;
    private SpriteBatch spriteWindow;
    private int countColumns = 10;
    private int countRows = 4;

    @Override
    public boolean keyUp(int key_U) {
        pressedKey = -1;
        return true;
    }

    @Override
    public boolean keyDown(int keyD) {
        pressedKey = keyD;
        return true;
    }

    BlockSelector() {
        cellBoxes = new ImageHandler();
        screen = new ImageHandler();
        ImageHandler backgroundPaper = new ImageHandler();
        blocks = new ImageHandler();
        select = new ImageHandler();
        width = JJEngine.getInstance().renderWidth / 2;
        height = JJEngine.getInstance().renderHeight / 2;

        exitButton = new ScreenButton();
        startButton = new ScreenButton();
        loadButton = new ScreenButton();
        saveButton = new ScreenButton();
        background = new ImageHandler();

        exitButton.choice = new ImageHandler();
        saveButton.choice = new ImageHandler();
        loadButton.choice = new ImageHandler();
        startButton.choice = new ImageHandler();

        exitButton.textX = 16;
        exitButton.textY = 19;
        loadButton.textY = 19;
        startButton.textY = 19;
        startButton.textX = 13;
        saveButton.textY = 19;
        saveButton.textX = 6;
        loadButton.textX = 6;

        startButton.setPosition(298, 205);
        loadButton.setPosition(194, 45);
        exitButton.setPosition(298, 45);
        saveButton.setPosition(90, 45);

        startButton.font.load("jumpjack.ttf");
        startButton.font.setPixelSizes(12);

        startButton.textMessage = "idpary";
        loadButton.font = startButton.font;
        loadButton.textMessage = "pfqrapr";
        saveButton.font = startButton.font;
        saveButton.textMessage = "qnupami";
        exitButton.font = startButton.font;
        exitButton.textMessage = "cxune";

        startButton.choice.load("ui/btnForeground.png");
        startButton.background.load("ui/btnBackground.png");
        startButton.screen.blit(startButton.background);
        loadButton.choice.load("ui/btnForeground.png");
        loadButton.background.load("ui/btnBackground.png");
        loadButton.screen.blit(loadButton.background);
        saveButton.choice.load("ui/btnForeground.png");
        saveButton.background.load("ui/btnBackground.png");
        saveButton.screen.blit(saveButton.background);
        exitButton.choice.load("ui/btnForeground.png");
        exitButton.background.load("ui/btnBackground.png");
        exitButton.screen.blit(exitButton.background);

        background.load("ui/background.png");
        cellBoxes.load("ui/cellBox.png");
        blocks.load("ui/allBlocksSprite.png");
        backgroundPaper.load("ui/foreground.png");
        select.load("ui/blockSelector.png");
        spriteWindow = new SpriteBatch();

        background.blit(54, 27, backgroundPaper);
        printScreen();
    }

    @Override
    public boolean mouseMoved(int x, int y) {
        float scaleX = cellBoxes.getWidth() * Preference.getInstance().screenWidth / JJEngine.getInstance().renderWidth;
        float scaleY = cellBoxes.getHeight() * Preference.getInstance().screenHeight / JJEngine.getInstance().renderHeight;
        y = Preference.getInstance().displayHeight - y - (Preference.getInstance().displayHeight - Preference.getInstance().screenHeight) / 2;
        x -= (Preference.getInstance().displayWidth - Preference.getInstance().screenWidth) / 2;

        for (int i = 0; i < countColumns; i++)
            for (int j = 0; j < countRows; j++)
                if (x > Preference.getInstance().screenWidth / 2 + (i - countColumns / 2) * scaleX &&
                        x <= Preference.getInstance().screenWidth / 2 + (i - countColumns / 2.0f) * scaleX + scaleX &&
                        y > Preference.getInstance().screenHeight / 2 + (j - countRows / 2) * scaleY &&
                        y <= Preference.getInstance().screenHeight / 2 + (j - countRows / 2.0) * scaleY + scaleY) {
                    check = true;
                    if (oldColumn != i || oldRow != j) {
                        screen.clear();
                        screen.blit(background);
                        screen.blit((i - countColumns / 2) * cellBoxes.getWidth() + this.width,
                                this.height + (j - countRows / 2) * cellBoxes.getHeight() - 1, select);
                        oldColumn = i;
                        oldRow = j;
                        sprite = screen.render();
                    } else {
                        i = countColumns;
                        break;
                    }
                }
        return true;
    }

    private void printScreen() {
        for (int i = 0; i < countColumns; i++)
            for (int j = 0; j < countRows; j++) {
                background.blit(width + (i - countColumns / 2) * cellBoxes.getWidth(),
                        height + (j - countRows / 2) * cellBoxes.getHeight(),
                        cellBoxes);

                background.blit(width + (i - countColumns / 2) * cellBoxes.getWidth() + (cellBoxes.getWidth() - blocks.getWidth() / countColumns) / 2,
                        height + (j - countRows / 2) * cellBoxes.getHeight() + (cellBoxes.getHeight() - blocks.getHeight() / countRows) / 2,
                        blocks,
                        i * blocks.getWidth() / countColumns,
                        j * blocks.getHeight() / countRows,
                        blocks.getWidth() / countColumns,
                        blocks.getHeight() / countRows);
            }

        screen.blit(background);
        sprite = screen.render();
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
            JJEngine.getInstance().worldMapInst.mouseX = oldColumn;
            JJEngine.getInstance().worldMapInst.mouseY = oldRow;
            pressedKey = 131;
        }

        if (startButton.touchDown(x, y)) {
            pressedKey = 131;
        } else if (loadButton.touchDown(x, y)) {
            JJEngine.getInstance().fileController.loadWorld();
            pressedKey = 131;
        } else if (saveButton.touchDown(x, y)) {
            JJEngine.getInstance().fileController.saveWorld();
        } else if (exitButton.touchDown(x, y)) {
            pressedKey = 67;
        }

        return true;
    }

    void display(Viewport viewport) {
        viewport.apply();
        spriteWindow.setProjectionMatrix(viewport.getCamera().combined);

        spriteWindow.begin();
        sprite.draw(spriteWindow);
        spriteWindow.end();

        startButton.show(viewport);
        loadButton.show(viewport);
        saveButton.show(viewport);
        exitButton.show(viewport);
    }

    void dispose() {
        screen.dispose();
        background.dispose();
        startButton.dispose();
        spriteWindow.dispose();
        saveButton.dispose();
        loadButton.dispose();
        exitButton.dispose();
    }
}
