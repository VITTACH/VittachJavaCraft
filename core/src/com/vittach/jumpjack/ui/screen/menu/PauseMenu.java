package com.vittach.jumpjack.ui.screen.menu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.vittach.jumpjack.MainEngine;
import com.vittach.jumpjack.Preferences;
import com.vittach.jumpjack.framework.ImageHandler;
import com.vittach.jumpjack.ui.InputListener;
import com.vittach.jumpjack.ui.buttons.ButtonClickListener;
import com.vittach.jumpjack.ui.buttons.ScreenButton;

public class PauseMenu extends InputListener {
    private boolean isCubeChecked = false;

    private int oldRow = 0;
    private int oldColumn = 0;
    private final int columns = 10;
    private final int rows = 4;

    private final float width;
    private final float height;

    private final SpriteBatch spriteWindow;
    private Sprite resultSprite;

    private final ImageHandler backgroundImage;
    private final ImageHandler cellBoxImage;
    private final ImageHandler cellSelectImage;
    private final ImageHandler blockImage;
    private final ImageHandler screen;

    private final MainEngine engineInstance = MainEngine.getInstance();
    private final Preferences preferenceInstance = Preferences.getInstance();

    private final ScreenButton saveButton;
    private final ScreenButton loadButton;
    private final ScreenButton playButton;
    private final ScreenButton exitButton;

    public int pressedKey;

    public void dispose() {
        screen.dispose();
        playButton.dispose();
        spriteWindow.dispose();
        backgroundImage.dispose();
        saveButton.dispose();
        loadButton.dispose();
        exitButton.dispose();
    }

    public void setUpListeners() {
        preferenceInstance.inputListener.addListener(saveButton);
        preferenceInstance.inputListener.addListener(exitButton);
        preferenceInstance.inputListener.addListener(loadButton);
        preferenceInstance.inputListener.addListener(playButton);
        preferenceInstance.inputListener.addListener(this);
    }

    public PauseMenu() {
        screen = new ImageHandler();
        blockImage = new ImageHandler();
        cellBoxImage = new ImageHandler();
        cellSelectImage = new ImageHandler();

        width = engineInstance.renderWidth / 2f;
        height = engineInstance.renderHeight / 2f;

        exitButton = new ScreenButton();
        playButton = new ScreenButton();
        loadButton = new ScreenButton();
        saveButton = new ScreenButton();

        backgroundImage = new ImageHandler();

        exitButton.choice = new ImageHandler();
        saveButton.choice = new ImageHandler();
        loadButton.choice = new ImageHandler();
        playButton.choice = new ImageHandler();

        exitButton.textX = 16;
        exitButton.textY = 19;
        loadButton.textY = 19;
        playButton.textY = 19;
        playButton.textX = 13;
        saveButton.textY = 19;
        saveButton.textX = 6;
        loadButton.textX = 6;

        playButton.setPosition(298, 205);
        loadButton.setPosition(194, 45);
        exitButton.setPosition(298, 45);
        saveButton.setPosition(90, 45);

        playButton.font.load("jumpjack.ttf");
        playButton.font.setPixelSize(12);

        playButton.message = "idpary";
        loadButton.font = playButton.font;
        loadButton.message = "pfqrapr";
        saveButton.font = playButton.font;
        saveButton.message = "qnupami";
        exitButton.font = playButton.font;
        exitButton.message = "cxune";

        playButton.choice.load("ui/button_small_selected.png");
        playButton.selectedBoxImage.load("ui/button_small_default.png");
        playButton.screen.blit(playButton.selectedBoxImage);
        loadButton.choice.load("ui/button_small_selected.png");
        loadButton.selectedBoxImage.load("ui/button_small_default.png");
        loadButton.screen.blit(loadButton.selectedBoxImage);
        saveButton.choice.load("ui/button_small_selected.png");
        saveButton.selectedBoxImage.load("ui/button_small_default.png");
        saveButton.screen.blit(saveButton.selectedBoxImage);
        exitButton.choice.load("ui/button_small_selected.png");
        exitButton.selectedBoxImage.load("ui/button_small_default.png");
        exitButton.screen.blit(exitButton.selectedBoxImage);

        backgroundImage.load("ui/background.png");
        cellBoxImage.load("ui/cell_box_default.png");
        cellSelectImage.load("ui/cell_box_selected.png");
        blockImage.load("ui/cubes_sprite.png");
        spriteWindow = new SpriteBatch();

        backgroundImage.blit(54, 27, new ImageHandler().load("ui/paper.png"));

        for (int i = 0; i < columns; i++)
            for (int j = 0; j < rows; j++) {
                float x = width + (i - columns / 2) * cellBoxImage.getWidth();
                float y = height + (j - rows / 2f) * cellBoxImage.getHeight();

                backgroundImage.blit(x, y, cellBoxImage);

                int blockWidth = blockImage.getWidth() / columns;
                int blockHeight = blockImage.getHeight() / rows;
                y = y + (cellBoxImage.getHeight() - blockHeight) / 2;
                x = x + (cellBoxImage.getWidth() - blockWidth) / 2;

                backgroundImage.blit(x, y, blockImage, i * blockWidth, j * blockHeight, blockWidth, blockHeight);
            }

        screen.blit(backgroundImage);
        resultSprite = screen.render();
    }

    @Override
    public boolean keyUp(int keyCode) {
        pressedKey = -1;
        return true;
    }

    @Override
    public boolean keyDown(int keyCode) {
        pressedKey = keyCode;
        return true;
    }

    @Override
    public boolean mouseMoved(int x, int y) {
        float scaleX = cellBoxImage.getWidth() * preferenceInstance.screenWidth / (float) engineInstance.renderWidth;
        float scaleY = cellBoxImage.getHeight() * preferenceInstance.screenHeight / (float) engineInstance.renderHeight;

        x = x - (preferenceInstance.displayWidth - preferenceInstance.screenWidth) / 2;
        y = preferenceInstance.displayHeight - y - (preferenceInstance.displayHeight - preferenceInstance.screenHeight) / 2;

        boolean isSelected = false;
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                if (x > preferenceInstance.screenWidth / 2f + (i - columns / 2) * scaleX
                    && x <= preferenceInstance.screenWidth / 2f + (i - columns / 2) * scaleX + scaleX
                    && y > preferenceInstance.screenHeight / 2f + (j - rows / 2) * scaleY
                    && y <= preferenceInstance.screenHeight / 2f + (j - rows / 2) * scaleY + scaleY) {
                    isSelected = true;

                    if (oldColumn != i || oldRow != j) {
                        screen.clear();
                        screen.blit(backgroundImage);
                        screen.blit(
                            width + (i - columns / 2) * cellBoxImage.getWidth(),
                            height + (j - rows / 2) * cellBoxImage.getHeight(),
                            cellSelectImage
                        );
                        oldColumn = i;
                        oldRow = j;
                        resultSprite = screen.render();
                    } else {
                        i = columns;
                        break;
                    }
                }
            }
        }
        isCubeChecked = isSelected;
        return true;
    }

    @Override
    public boolean touchDragged(int x, int y, int id) {
        return mouseMoved(x, y);
    }

    @Override
    public boolean touchUp(int x, int y, int id, int button) {
        mouseMoved(x, y);

        if (isCubeChecked) {
            pressedKey = Input.Keys.ESCAPE;
        }

        playButton.onClicked(x, y, new ButtonClickListener() {
            @Override
            public void onClicked() {
                pressedKey = Input.Keys.ESCAPE;
            }
        });

        loadButton.onClicked(x, y, new ButtonClickListener() {
            @Override
            public void onClicked() {
                pressedKey = Input.Keys.ESCAPE;
            }
        });

        saveButton.onClicked(x, y, new ButtonClickListener() {
            @Override
            public void onClicked() {
            }
        });

        exitButton.onClicked(x, y, new ButtonClickListener() {
            @Override
            public void onClicked() {
                pressedKey = 2;
            }
        });

        return true;
    }

    public void display(Viewport view) {
        view.apply();
        spriteWindow.setProjectionMatrix(view.getCamera().combined);

        spriteWindow.begin();
        resultSprite.draw(spriteWindow);
        spriteWindow.end();

        playButton.draw(view);
        loadButton.draw(view);
        saveButton.draw(view);
        exitButton.draw(view);
    }
}
