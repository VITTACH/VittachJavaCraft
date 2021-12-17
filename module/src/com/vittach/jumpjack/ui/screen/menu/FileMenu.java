package com.vittach.jumpjack.ui.screen.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.vittach.jumpjack.MainEngine;
import com.vittach.jumpjack.ui.InputListener;
import com.vittach.jumpjack.Preferences;
import com.vittach.jumpjack.ui.buttons.ScreenButton;
import com.vittach.jumpjack.framework.ColorImpl;
import com.vittach.jumpjack.framework.FontHandler;
import com.vittach.jumpjack.framework.ImageHandler;
import com.vittach.jumpjack.framework.MyTimer;

import java.util.ArrayList;

public class FileMenu extends InputListener {
    private ColorImpl color;
    private MyTimer scrollTimer;
    
    private Sprite sprite;
    private Sprite dirSprite;
    private Sprite selectSprite;
    private SpriteBatch spriteBatch;

    public ScreenButton sartButton;
    public ScreenButton backButton;
    public ScreenButton loadButton;

    public int pressedKey = -1;

    private ArrayList<FileHandle> fileHandles;
    private int stepY;
    private int oldIndex;
    private int startIndex;
    private int positionY = -1;
    private int oldPositionY = -1;
    private int countOfFiles = -1;
    private int offsetX = 126;
    private int offsetY = 183;

    private float scaleX;
    private float scaleY;
    
    private FontHandler arcadepixFont;

    private ImageHandler screen;
    private ImageHandler dirBackgroundImage;
    private ImageHandler dirForegroundImage;
    private ImageHandler backgroundImage;
    private ImageHandler foregroundImage;
    
    private Preferences prefInstance = Preferences.getInstance();
    private MainEngine engineInstance = MainEngine.getInstance();

    @Override
    public boolean keyDown(int ikey) {
        if (ikey == 19) scroll(oldPositionY++);
        if (ikey == 20) scroll(oldPositionY--);
        return true;
    }

    public void updateDir() {
        fileHandles.clear();
        countOfFiles = (startIndex = 0) - 1;
        for (FileHandle file : Gdx.files.local("").list()) {
            if (file.name().contains(".JJ")) {
                fileHandles.add(file);
            }
        }

        if (countOfFiles < 0) {
            countOfFiles = fileHandles.size();
            if (countOfFiles > 3) {
                countOfFiles = 3;
            }
        }
    }

    public void dispose() {
        arcadepixFont.dispose();
        dirBackgroundImage.dispose();
        backgroundImage.dispose();
        spriteBatch.dispose();
        screen.dispose();
        sartButton.dispose();
        dirForegroundImage.dispose();
        foregroundImage.dispose();
        backButton.dispose();
        loadButton.dispose();
    }

    public FileMenu() {
        scrollTimer = new MyTimer();
        arcadepixFont = new FontHandler();
        foregroundImage = new ImageHandler().load("ui/foreground.png");
        backgroundImage = new ImageHandler().load("ui/background.png");
        dirBackgroundImage = new ImageHandler().load("ui/dirBackground.png");
        dirForegroundImage = new ImageHandler().load("ui/dirForeground.png");
        screen = new ImageHandler();

        sartButton = new ScreenButton();
        sartButton.choice = new ImageHandler();
        backButton = new ScreenButton();
        backButton.choice = new ImageHandler();
        loadButton = new ScreenButton();
        loadButton.choice = new ImageHandler();

        color = new ColorImpl(1, 1, 1, 1);
        fileHandles = new ArrayList<FileHandle>();
        spriteBatch = new SpriteBatch();
        arcadepixFont.load("arcadepix.ttf");
        arcadepixFont.setPixelSize(14);
        stepY = arcadepixFont.getSize() + 2;

        foregroundImage.blit(66, 102, dirBackgroundImage);
        backgroundImage.blit(54, 27, foregroundImage);

        FontHandler buttonsFont = new FontHandler();
        buttonsFont.load("jumpjack.ttf");
        buttonsFont.setPixelSize(12);
        
        backButton.choice.load("ui/startChoice.png");
        backButton.background.load("ui/startButton.png");
        backButton.screen.blit(backButton.background);
        backButton.setPosition(engineInstance.renderWidth / 2 - backButton.getWidth() / 2f, 68);
        backButton.font = buttonsFont;
        backButton.textY = 19;
        backButton.textX = 90;
        backButton.message = "mahae";

        sartButton.choice.load("ui/startChoice.png");
        sartButton.background.load("ui/startButton.png");
        sartButton.screen.blit(sartButton.background);
        sartButton.setPosition(engineInstance.renderWidth / 2 - sartButton.getWidth() / 2f, 98);
        sartButton.font = buttonsFont;
        sartButton.textY = 19;
        sartButton.textX = 79;
        sartButton.message = "seakiry";

        loadButton.choice.load("ui/startChoice.png");
        loadButton.background.load("ui/startButton.png");
        loadButton.screen.blit(loadButton.background);
        loadButton.setPosition(engineInstance.renderWidth / 2 - loadButton.getWidth() / 2, 184);
        loadButton.font = buttonsFont;
        loadButton.textY = 19;
        loadButton.textX = 74;
        loadButton.message = "hadpshja";

        selectSprite = dirForegroundImage.render();
        selectSprite.setPosition(123, offsetY - dirForegroundImage.getHeight() + 1);
        sprite = backgroundImage.render();
        updateDir();
        getCurDir();
    }

    @Override
    public boolean touchDown(int x, int y, int id, int button) {
        scaleY = prefInstance.screenHeight / engineInstance.renderHeight;
        scaleX = prefInstance.screenWidth / engineInstance.renderWidth;

        if (fileHandles.size() > 0) {
            if (sartButton.touchDown(x, y)) delFile();
            if (loadButton.touchDown(x, y)) {
                pressedKey = 1;
            }
        }

        if (backButton.touchDown(x, y)) pressedKey = 2;

        y = prefInstance.displayHeight - y - (prefInstance.displayHeight - prefInstance.screenHeight) / 2;
        x -= (prefInstance.displayWidth - prefInstance.screenWidth) / 2;
        id = (countOfFiles <= 3) ? countOfFiles : 3;

        for (int i = 0; i < id; i++) {
            if (y >= (-i - 1) * stepY * scaleY + offsetY * scaleY
                    && y <= -i * stepY * scaleY + offsetY * scaleY && x >= 64 * scaleX && x <= 416 * scaleX) {
                selectSprite.setPosition(123, (-i - 1) * stepY + offsetY);
                oldIndex = i + startIndex;
                break;
            }
        }
        positionY = oldPositionY = y;
        return true;
    }

    @Override
    public boolean touchDragged(int xpos, int ypos, int TID) {
        ypos = prefInstance.displayHeight - ypos - (prefInstance.displayHeight - prefInstance.screenHeight) / 2;
        xpos -= (prefInstance.displayWidth - prefInstance.screenWidth) / 2;
        if (xpos >= selectSprite.getX() * scaleX && xpos <= offsetX + dirForegroundImage.getWidth() * scaleX
                && ypos <= offsetY * scaleY
                && ypos >= offsetY * scaleY - scaleY * dirForegroundImage.getHeight() * 3) {
            positionY = ypos;
        }
        return true;
    }

    public void getCurDir() {
        screen.clear();
        int j = 0, i;
        for (i = startIndex; i < countOfFiles; i++, j++) {
            screen.fontPrint(arcadepixFont, offsetX,
                    offsetY - 2 - j * stepY, i + ") "+ fileHandles.get(i).name().replace(".JJ", ""), color);
            screen.fontPrint(arcadepixFont, engineInstance.renderWidth / 2, offsetY - 2 - j * stepY,
                    "~" + (System.currentTimeMillis() - fileHandles.get(i).lastModified()) / 3600000
                            + " hrs ago", color);
        }
        if (countOfFiles < 3 && oldIndex > 0) {
            i = --oldIndex - startIndex;
            selectSprite.setPosition(123, (-i - 1) * stepY + offsetY);
        }
        dirSprite = screen.render();
    }

    public void display(Viewport viewport) {
        if (Math.abs(positionY - oldPositionY) >= dirForegroundImage.getHeight()) {
            scroll(positionY);
        }

        viewport.apply();

        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        sprite.draw(spriteBatch);
        dirSprite.draw(spriteBatch);
        if (selectSprite.getY() < offsetY && selectSprite.getY() >= dirForegroundImage.getHeight() * -3 + offsetY) {
            selectSprite.draw(spriteBatch);
        }
        spriteBatch.end();

        sartButton.show(viewport);
        backButton.show(viewport);
        loadButton.show(viewport);
    }

    public void scroll(int yPosition) {
        if (!scrollTimer.isActive()) {
            if (yPosition > oldPositionY) {
                if (countOfFiles < fileHandles.size()) {
                    startIndex += 1;
                    countOfFiles += 1;
                    selectSprite.setPosition(123, selectSprite.getY() + selectSprite.getHeight());
                }
            } else if (startIndex > 0) {
                startIndex -= 1;
                countOfFiles -= 1;
                selectSprite.setPosition(123, selectSprite.getY() - selectSprite.getHeight());
            }

            scrollTimer.start(200);
            oldPositionY = yPosition;
            getCurDir();
        }
    }

    public void delFile() {
        fileHandles.get(oldIndex).delete();
        updateDir();
        getCurDir();
    }
}
