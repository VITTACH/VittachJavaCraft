package com.vittach.jumpjack.ui.screen.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.vittach.jumpjack.MainEngine;
import com.vittach.jumpjack.Preferences;
import com.vittach.jumpjack.framework.FontHandler;
import com.vittach.jumpjack.framework.ImageHandler;
import com.vittach.jumpjack.ui.InputListener;
import com.vittach.jumpjack.ui.buttons.ButtonClickListener;
import com.vittach.jumpjack.ui.buttons.ScreenButton;

public class CreateWorldMenu extends InputListener {
    public Sprite sprite;
    private final ScreenButton backButton;
    private final ScreenButton startButton;
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
    private final Preferences preferenceInstance = Preferences.getInstance();

    public int pressedKey = -1;

    @Override
    public boolean touchUp(int x, int y, int id, int button) {
        super.touchDown(x, y, id, button);
        startButton.onClicked(x, y, new ButtonClickListener() {
            @Override
            public void onClicked() {
                if (getName().length() > 3) {
                    pressedKey = 1;
                } else textField.setMessageText(" input correct name");
            }
        });

        backButton.onClicked(x, y, new ButtonClickListener() {
            @Override
            public void onClicked() {
                pressedKey = 2;
            }
        });
        return true;
    }

    public void display(Viewport viewport) {
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        spriteBatch.begin();
        sprite.draw(spriteBatch);
        spriteBatch.end();

        startButton.draw(viewport);
        backButton.draw(viewport);

        setViewport(viewport);
        act();
        draw();
    }

    public void setUpListeners() {
        preferenceInstance.inputListener.addListener(startButton);
        preferenceInstance.inputListener.addListener(backButton);
        preferenceInstance.inputListener.addListener(this);
    }

    public CreateWorldMenu() {
        jumpjackFont = new FontHandler();
        arcadepixFont = new FontHandler();
        spriteBatch = new SpriteBatch();

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

        startButton = new ScreenButton();
        startButton.choice = new ImageHandler();
        startButton.choice.load("ui/button_selected.png");
        startButton.selectedBoxImage.load("ui/button_default.png");
        startButton.screen.blit(startButton.selectedBoxImage);
        startButton.setPosition(engineInstance.renderWidth / 2.0f - startButton.getWidth() / 2f, 98);
        startButton.font = jumpjackFont;
        startButton.message = "qnheary";
        startButton.textY = 19;
        startButton.textX = 80;

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

        startButton.dispose();
        backButton.dispose();
    }

    @Override
    public void setIdOffset(int idOffset) {

    }
}
