package com.vittach.jumpjack.ui.screen.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.vittach.jumpjack.engine.MainEngine;
import com.vittach.jumpjack.Preferences;
import com.vittach.jumpjack.utils.ColorImpl;
import com.vittach.jumpjack.utils.FontHandler;
import com.vittach.jumpjack.utils.ImageHandler;
import com.vittach.jumpjack.ui.InputListener;
import com.vittach.jumpjack.ui.buttons.ButtonClickListener;
import com.vittach.jumpjack.ui.buttons.ScreenButton;

import java.util.ArrayList;

public class LoadAndSaveMenu extends InputListener {
    private final ColorImpl color;

    private final Sprite sprite;
    private Sprite dirSprite;
    private final Sprite selectSprite;
    private final SpriteBatch spriteBatch;

    private final ScreenButton startButton;
    private final ScreenButton backButton;
    private final ScreenButton loadButton;

    public int pressedKey = -1;

    private final ArrayList<FileHandle> fileHandles;
    private final int stepY;
    private int oldIndex;
    private int startIndex;
    private int positionY = -1;
    private int oldY = -1;
    private int countOfFiles = -1;
    private int offsetX = 126;
    private int offsetY = 183;

    private float scaleX;
    private float scaleY;

    private FontHandler arcadepixFont;

    private final ImageHandler screen;
    private final ImageHandler dirBackgroundImage;
    private final ImageHandler itemSelectedImage;
    private final ImageHandler backgroundImage;
    private final ImageHandler paperImage;

    private final Preferences preferenceInstance = Preferences.getInstance();
    private final MainEngine engineInstance = MainEngine.getInstance();

    @Override
    public boolean keyDown(int keyCode) {
        if (keyCode == 19) scroll(oldY++);
        if (keyCode == 20) scroll(oldY--);
        return true;
    }

    private void updateDir() {
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

    public void setUpListeners() {
        preferenceInstance.inputListener.addListener(backButton);
        preferenceInstance.inputListener.addListener(loadButton);
        preferenceInstance.inputListener.addListener(startButton);
        preferenceInstance.inputListener.addListener(this);

        updateDir();
        getCurrentDir();
    }

    public void dispose() {
        arcadepixFont.dispose();
        dirBackgroundImage.dispose();
        backgroundImage.dispose();
        spriteBatch.dispose();
        screen.dispose();
        startButton.dispose();
        itemSelectedImage.dispose();
        paperImage.dispose();
        backButton.dispose();
        loadButton.dispose();
    }

    public LoadAndSaveMenu() {
        paperImage = new ImageHandler().load("ui/paper.png");
        backgroundImage = new ImageHandler().load("ui/background.png");
        dirBackgroundImage = new ImageHandler().load("ui/list_background.png");
        itemSelectedImage = new ImageHandler().load("ui/item_selected.png");
        screen = new ImageHandler();

        startButton = new ScreenButton();
        startButton.choice = new ImageHandler();
        backButton = new ScreenButton();
        backButton.choice = new ImageHandler();
        loadButton = new ScreenButton();
        loadButton.choice = new ImageHandler();

        color = new ColorImpl(1, 1, 1, 1);
        fileHandles = new ArrayList<FileHandle>();
        spriteBatch = new SpriteBatch();

        paperImage.blit(66, 102, dirBackgroundImage);
        backgroundImage.blit(54, 27, paperImage);

        arcadepixFont = new FontHandler();
        arcadepixFont.load("arcadepix.ttf");
        arcadepixFont.setPixelSize(14);
        stepY = arcadepixFont.getSize() + 2;

        FontHandler buttonsFont = new FontHandler();
        buttonsFont.load("jumpjack.ttf");
        buttonsFont.setPixelSize(12);

        backButton.choice.load("ui/button_selected.png");
        backButton.foreground.load("ui/button_default.png");
        backButton.screen.blit(backButton.foreground);
        backButton.setPosition(engineInstance.renderWidth / 2 - backButton.getWidth() / 2f, 68);
        backButton.font = buttonsFont;
        backButton.setMessage(90, 19, "mahae");

        startButton.choice.load("ui/button_selected.png");
        startButton.foreground.load("ui/button_default.png");
        startButton.screen.blit(startButton.foreground);
        startButton.setPosition(engineInstance.renderWidth / 2 - startButton.getWidth() / 2f, 98);
        startButton.font = buttonsFont;
        startButton.setMessage(79, 19, "seakiry");

        loadButton.choice.load("ui/button_selected.png");
        loadButton.foreground.load("ui/button_default.png");
        loadButton.screen.blit(loadButton.foreground);
        loadButton.setPosition(engineInstance.renderWidth / 2 - loadButton.getWidth() / 2, 184);
        loadButton.font = buttonsFont;
        loadButton.setMessage(74, 19, "hadpshja");

        selectSprite = itemSelectedImage.render();
        selectSprite.setPosition(123, offsetY - itemSelectedImage.getHeight() + 1);
        sprite = backgroundImage.render();
        updateDir();
        getCurrentDir();
    }

    @Override
    public boolean touchUp(int x, int y, int id, int button) {
        scaleX = preferenceInstance.screenWidth / (float) engineInstance.renderWidth;
        scaleY = preferenceInstance.screenHeight / (float) engineInstance.renderHeight;

        if (fileHandles.size() > 0) {
            startButton.onClicked(x, y, new ButtonClickListener() {
                @Override
                public void onClicked() {
                    delFile();
                }
            });

            loadButton.onClicked(x, y, new ButtonClickListener() {
                @Override
                public void onClicked() {
                    pressedKey = 1;
                }
            });
        }

        backButton.onClicked(x, y, new ButtonClickListener() {
            @Override
            public void onClicked() {
                pressedKey = 2;
            }
        });

        y = preferenceInstance.displayHeight - y - (preferenceInstance.displayHeight - preferenceInstance.screenHeight) / 2;
        x -= (preferenceInstance.displayWidth - preferenceInstance.screenWidth) / 2;
        id = (countOfFiles <= 3) ? countOfFiles : 3;

        for (int i = 0; i < id; i++) {
            if (y >= (-i - 1) * stepY * scaleY + offsetY * scaleY
                && y <= -i * stepY * scaleY + offsetY * scaleY && x >= 64 * scaleX && x <= 416 * scaleX) {
                selectSprite.setPosition(123, (-i - 1) * stepY + offsetY);
                oldIndex = i + startIndex;
                break;
            }
        }
        positionY = oldY = y;
        return true;
    }

    @Override
    public boolean touchDragged(int x, int y, int id) {
        y = preferenceInstance.displayHeight - y - (preferenceInstance.displayHeight - preferenceInstance.screenHeight) / 2;
        x -= (preferenceInstance.displayWidth - preferenceInstance.screenWidth) / 2;
        if (x >= selectSprite.getX() * scaleX && x <= offsetX + itemSelectedImage.getWidth() * scaleX
            && y <= offsetY * scaleY
            && y >= offsetY * scaleY - scaleY * itemSelectedImage.getHeight() * 3) {
            positionY = y;
        }
        return true;
    }

    private void getCurrentDir() {
        screen.clear();
        int j = 0, i;
        for (i = startIndex; i < countOfFiles; i++, j++) {
            screen.fontPrint(arcadepixFont, offsetX,
                offsetY - 2 - j * stepY, i + ") " + fileHandles.get(i).name().replace(".JJ", ""), color);
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
        if (Math.abs(positionY - oldY) >= itemSelectedImage.getHeight()) {
            scroll(positionY);
        }

        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        spriteBatch.begin();
        sprite.draw(spriteBatch);
        dirSprite.draw(spriteBatch);
        if (selectSprite.getY() < offsetY && selectSprite.getY() >= itemSelectedImage.getHeight() * -3 + offsetY) {
            selectSprite.draw(spriteBatch);
        }
        startButton.draw(spriteBatch);
        backButton.draw(spriteBatch);
        loadButton.draw(spriteBatch);
        spriteBatch.end();
    }

    public void scroll(int y) {
        if (y > oldY) {
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

        oldY = y;
        getCurrentDir();
    }

    public void delFile() {
        fileHandles.get(oldIndex).delete();
        updateDir();
        getCurrentDir();
    }
}
