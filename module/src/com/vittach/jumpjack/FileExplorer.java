package com.vittach.jumpjack;

import com.badlogic.gdx.Gdx;
import com.vittach.jumpjack.framework.FontHandler;
import com.vittach.jumpjack.framework.ColorImpl;
import com.vittach.jumpjack.framework.ImageHandler;
import com.vittach.jumpjack.framework.MyTimer;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public class FileExplorer extends InputListener {
    private MyTimer scroll;
    private ColorImpl color;
    private SpriteBatch spriteBatch;
    private ImageHandler background;

    public ScreenButton acceptButton;
    public ScreenButton backButton;
    public ScreenButton loadButton;

    private int oldY = -1;
    private int positionY = -1;
    public int pressedKey = -1;
    private int countOfFiles = -1;
    private ArrayList<FileHandle> savedFiles;
    private float scaleX;
    private float scaleY;
    private int start;
    private int stepY;
    private int old;
    private int biasX = 126;
    private int biasY = 183;

    private Sprite sprite;
    private Sprite dirSprite;
    private Sprite selectSprite;
    private FontHandler dirFont;
    private ImageHandler dirGoBack;
    private ImageHandler selectedLine;
    private ImageHandler imageHandler;
    private ImageHandler backgroundImage;

    @Override
    public boolean keyDown(int ikey) {
        if (ikey == 19) scrol(oldY++);
        if (ikey == 20) scrol(oldY--);
        return true;
    }

    public void updateDir() {
        savedFiles.clear();
        countOfFiles = (start = 0) - 1;
        for (FileHandle file : Gdx.files.local("").list())
            if (file.name().contains(".JJ"))
                savedFiles.add(file);
        if (countOfFiles < 0) {
            countOfFiles = savedFiles.size();
            if (countOfFiles > 3) {
                countOfFiles = 3;
            }
        }
    }

    public FileExplorer() {
        scroll = new MyTimer();
        FontHandler buttonFont = new FontHandler();
        dirFont = new FontHandler();
        dirGoBack = new ImageHandler();
        selectedLine = new ImageHandler();
        imageHandler = new ImageHandler();
        backgroundImage = new ImageHandler();

        backButton = new ScreenButton();
        loadButton = new ScreenButton();
        acceptButton = new ScreenButton();

        backButton.choice = new ImageHandler();
        loadButton.choice = new ImageHandler();
        acceptButton.choice = new ImageHandler();

        background = new ImageHandler();
        color = new ColorImpl(1, 1, 1, 1);
        savedFiles = new ArrayList<FileHandle>();
        spriteBatch = new SpriteBatch();
        dirFont.load("arcadepix.ttf");
        buttonFont.load("jumpjack.ttf");
        dirFont.setPixelSizes(14);
        buttonFont.setPixelSizes(12);
        backgroundImage.load("ui/foreground.png");
        background.load("ui/background.png");
        stepY = dirFont.getSize() + 2;

        dirGoBack.load("ui/dirBackground.png");
        selectedLine.load("ui/dirForeground.png");
        backgroundImage.blit(66, 102, dirGoBack);
        background.blit(54, 27, backgroundImage);

        backButton.choice.load("ui/startChoice.png");
        backButton.background.load("ui/startButton.png");
        backButton.screen.blit(backButton.background);
        backButton.setPosition(JJEngine.getInstance().renderWidth / 2 - backButton.background.getWidth() / 2, 68);
        backButton.font = buttonFont;
        backButton.textY = 19;
        backButton.textX = 90;
        backButton.textMessage = "mahae";

        acceptButton.choice.load("ui/startChoice.png");
        acceptButton.background.load("ui/startButton.png");
        acceptButton.screen.blit(acceptButton.background);
        acceptButton.setPosition(JJEngine.getInstance().renderWidth / 2 - acceptButton.background.getWidth() / 2, 98);
        acceptButton.font = buttonFont;
        acceptButton.textY = 19;
        acceptButton.textX = 79;
        acceptButton.textMessage = "seakiry";

        loadButton.choice.load("ui/startChoice.png");
        loadButton.background.load("ui/startButton.png");
        loadButton.screen.blit(loadButton.background);
        loadButton.setPosition(JJEngine.getInstance().renderWidth / 2 - loadButton.background.getWidth() / 2, 184);
        loadButton.font = buttonFont;
        loadButton.textY = 19;
        loadButton.textX = 74;
        loadButton.textMessage = "hadpshja";

        selectSprite = selectedLine.render();
        selectSprite.setPosition(123, biasY - selectedLine.getHeight() + 1);
        sprite = background.render();
        updateDir();
        getCurDir();
    }

    @Override
    public boolean
    touchDown(int x, int y, int id, int b) {
        scaleY = Preference.getInstance().screenHeight / (float) JJEngine.getInstance().renderHeight;
        scaleX = Preference.getInstance().screenWidth / (float) JJEngine.getInstance().renderWidth;

        if (savedFiles.size() > 0) {
            if (acceptButton.touchDown(x, y)) delMaps();
            if (loadButton.touchDown(x, y)) pressedKey = 1;
        }

        if (backButton.touchDown(x, y)) pressedKey = 2;

        y = Preference.getInstance().displayHeight - y - (Preference.getInstance().displayHeight - Preference.getInstance().screenHeight) / 2;
        x -= (Preference.getInstance().displayWidth - Preference.getInstance().screenWidth) / 2;
        id = (countOfFiles <= 3) ? countOfFiles : 3;

        for (int i = 0; i < id; i++) {
            if (y >= (-i - 1) * stepY * scaleY + biasY * scaleY && y <= -i * stepY * scaleY + biasY * scaleY &&
                    x >= 64 * scaleX && x <= 416 * scaleX) {
                selectSprite.setPosition(123, (-i - 1) * stepY + biasY);
                old = i + start;
                break;
            }
        }
        positionY = oldY = y;
        return true;
    }

    @Override
    public boolean touchDragged(int xpos, int ypos, int TID) {
        ypos = Preference.getInstance().displayHeight - ypos - (Preference.getInstance().displayHeight - Preference.getInstance().screenHeight) / 2;
        xpos -= (Preference.getInstance().displayWidth - Preference.getInstance().screenWidth) / 2;
        if (xpos >= selectSprite.getX() * scaleX && xpos <= biasX + selectedLine.getWidth() * scaleX
                && ypos <= biasY * scaleY && ypos >= biasY * scaleY - scaleY * selectedLine.getHeight() * 3) {
            positionY = ypos;
        }
        return true;
    }

    public void getCurDir() {
        imageHandler.clear();
        int j = 0, i;
        for (i = start; i < countOfFiles; i++, j++) {
            imageHandler.fontPrint(dirFont, biasX, biasY - 2 - j * stepY, i + ") " + savedFiles.get(i).name().replace(".JJ", ""), color);
            imageHandler.fontPrint(dirFont, JJEngine.getInstance().renderWidth / 2, biasY - 2 - j * stepY, "~" + (System.currentTimeMillis() - savedFiles.
                    get(i).lastModified()) / 3600000 + " hrs ago", color);
        }
        if (countOfFiles < 3 && old > 0) {
            i = --old - start;
            selectSprite.setPosition(123, (-i - 1) * stepY + biasY);
        }
        dirSprite = imageHandler.render();
    }

    public void Display(Viewport iview) {
        if (Math.abs(positionY - oldY) >= selectedLine.getHeight()) {
            scrol(positionY);
        }
        iview.apply();
        spriteBatch.setProjectionMatrix(iview.getCamera().combined);
        spriteBatch.begin();
        sprite.draw(spriteBatch);
        dirSprite.draw(spriteBatch);
        if (selectSprite.getY() < biasY && selectSprite.getY() >= selectedLine.getHeight() * -3 + biasY) {
            selectSprite.draw(spriteBatch);
        }
        spriteBatch.end();
        backButton.show(iview);
        loadButton.show(iview);
        acceptButton.show(iview);
    }

    public void scrol(int yPosition) {
        if (!scroll.isActive()) {
            if (yPosition > oldY) {
                if (countOfFiles < savedFiles.size()) {
                    start += 1;
                    countOfFiles += 1;
                    selectSprite.setPosition(123, selectSprite.getY() + selectSprite.getHeight());
                }
            } else if (start > 0) {
                start -= 1;
                countOfFiles -= 1;
                selectSprite.setPosition(123, selectSprite.getY() - selectSprite.getHeight());
            }
            scroll.start(200);
            oldY = yPosition;
            getCurDir();
        }
    }

    public String getName() {
        return savedFiles.get(old).name();
    }

    public void delMaps() {
        savedFiles.get(old).delete();
        updateDir();
        getCurDir();
    }

    public void dispose() {
        imageHandler.dispose();
        dirFont.dispose();
        dirGoBack.dispose();
        background.dispose();
        spriteBatch.dispose();
        selectedLine.dispose();
        backgroundImage.dispose();
        backButton.dispose();
        loadButton.dispose();
        acceptButton.dispose();
    }
}
