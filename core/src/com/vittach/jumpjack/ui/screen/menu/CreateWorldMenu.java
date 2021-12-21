package com.vittach.jumpjack.ui.screen.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.vittach.jumpjack.MainEngine;
import com.vittach.jumpjack.framework.FontHandler;
import com.vittach.jumpjack.framework.ImageHandler;
import com.vittach.jumpjack.ui.InputListener;
import com.vittach.jumpjack.ui.buttons.ScreenButton;

public class CreateWorldMenu extends InputListener {
    public Sprite sprite;
    public ScreenButton backButton;
    public ScreenButton sartButton;
    public TextField textField;

    private final FontHandler arcadepixFont;
    private final FontHandler jumpjackFont;
    private final SpriteBatch spriteBatch;

    private final ImageHandler backgroundImage;
    private final ImageHandler textFieldImage;
    private final ImageHandler selectorImage;
    private final ImageHandler paperImage;
    private final ImageHandler cursor;

    private final MainEngine engineInstance = MainEngine.getInstance();

    public int pressedKey = -1;

    @Override
    public boolean touchDown(int x, int y, int id, int button) {
        super.touchDown(x, y, id, button);
        if (sartButton.touchDown(x, y))
            if (getName().length() > 3) pressedKey = 1;
            else {
                textField.setMessageText(" input correct name");
            }
        if (backButton.touchDown(x, y)) {
            pressedKey = 2;
        }
        return true;
    }

    public void display(Viewport viewport) {
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        sprite.draw(spriteBatch);
        spriteBatch.end();
        sartButton.draw(viewport);
        backButton.draw(viewport);
        setViewport(viewport);
        act();
        draw();
    }

    public CreateWorldMenu() {
        jumpjackFont = new FontHandler();
        arcadepixFont = new FontHandler();
        spriteBatch = new SpriteBatch();
        sartButton = new ScreenButton();
        backButton = new ScreenButton();

        arcadepixFont.load("arcadepix.ttf");
        arcadepixFont.setPixelSize(14);
        jumpjackFont.load("jumpjack.ttf");
        jumpjackFont.setPixelSize(12);
        sprite = new Sprite();

        backgroundImage = new ImageHandler().load("ui/background.png");

        cursor = new ImageHandler().load("ui/cursor.png");
        selectorImage = new ImageHandler().load("ui/selector.png");
        textFieldImage = new ImageHandler().load("ui/text_field.png");
        paperImage = new ImageHandler().load("ui/paper.png");

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle(
            arcadepixFont.getBitmapFont(), new Color(1, 1, 1, 1),
            new TextureRegionDrawable(cursor.render()),
            new TextureRegionDrawable(selectorImage.render()),
            new TextureRegionDrawable(textFieldImage.render())
        );

        textField = new TextField("", textFieldStyle);
        textField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField1, char key) {
                if (textField.getText().length() <= 1) {
                    textField.setText(" " + key);
                    textField.setCursorPosition(2);
                }
                if (key == '\n') {
                    textField1.getOnscreenKeyboard().show(false);
                }
            }
        });

        textField.setMaxLength(9);
        backgroundImage.blit(54, 27, paperImage);
        backgroundImage.blit(120, 164, new ImageHandler().load("ui/logo.png"));
        addActor(textField);
        sprite = backgroundImage.render();

        sartButton = new ScreenButton();
        sartButton.choice = new ImageHandler();
        sartButton.choice.load("ui/button_selected.png");
        sartButton.selectedBoxImage.load("ui/button_default.png");
        sartButton.screen.blit(sartButton.selectedBoxImage);
        sartButton.setPosition(engineInstance.renderWidth / 2.0f - sartButton.getWidth() / 2f, 98);
        sartButton.font = jumpjackFont;
        sartButton.message = "qnheary";
        sartButton.textY = 19;
        sartButton.textX = 80;

        backButton = new ScreenButton();
        backButton.choice = new ImageHandler();
        backButton.choice.load("ui/button_selected.png");
        backButton.selectedBoxImage.load("ui/button_default.png");
        backButton.screen.blit(backButton.selectedBoxImage);
        backButton.setPosition(engineInstance.renderWidth / 2.0f - backButton.getWidth() / 2f, 68);
        backButton.font = jumpjackFont;
        backButton.message = "mahae";
        backButton.textY = 19;
        backButton.textX = 90;

        textField.setOnlyFontChars(true);
        textField.setMessageText(" Hi!");
        textField.setPosition(engineInstance.renderWidth / 2 - textFieldImage.getWidth() / 2, 128);
        textField.setSize(textFieldImage.getWidth(), textFieldImage.getHeight());
    }

    public String getName() {
        String text = textField.getText();
        return text.length() > 0 ? text.substring(1) + ".JJ" : "";
    }

    public void dispose() {
        jumpjackFont.dispose();
        arcadepixFont.dispose();

        spriteBatch.dispose();
        cursor.dispose();

        selectorImage.dispose();
        textFieldImage.dispose();
        backgroundImage.dispose();
        paperImage.dispose();

        sartButton.dispose();
        backButton.dispose();
    }

    @Override
    public void setIdOffset(int idOffset) {

    }
}
