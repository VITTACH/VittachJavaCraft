package com.vittach.jumpjack;

import com.vittach.jumpjack.framework.FontHandler;
import com.vittach.jumpjack.framework.ImageHandler;
import com.vittach.jumpjack.framework.ColorImpl;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class WorldCreator extends Stage implements ProcessorInput {
    public Sprite sprite;
    public ScreenButton goBackButton;
    public ScreenButton createButton;
    private FontHandler arcadepixFont;
    private FontHandler jumpjackFont;
    public TextField textField;
    private SpriteBatch spriteBatch;
    private ImageHandler cursor;
    private ImageHandler logoImage;
    private ImageHandler selectorImage;
    private ImageHandler backgroundImahe;
    private ImageHandler paperImage;
    private ImageHandler textFieldImage;
    public int pressedKey = -1;
    private ColorImpl color;

    @Override
    public void setIDOffset(int id) {
    }

    @Override
    public boolean
    touchUp(int x, int y, int id, int btn) {
        if (createButton.touchDown(x, y))
            if (getName().length() > 3) pressedKey = 1;
            else textField.setMessageText(" input correct name");
        if (goBackButton.touchDown(x, y)) pressedKey = 2;
        return true;
    }

    void display(Viewport view) {
        view.apply();
        spriteBatch.setProjectionMatrix(view.getCamera().combined);
        spriteBatch.begin();
        sprite.draw(spriteBatch);
        spriteBatch.end();
        setViewport(view);
        act();
        draw();
        createButton.show(view);
        goBackButton.show(view);
    }

    WorldCreator() {
        arcadepixFont = new FontHandler();
        jumpjackFont = new FontHandler();
        logoImage = new ImageHandler();
        backgroundImahe = new ImageHandler();
        sprite = new Sprite();
        cursor = new ImageHandler();
        paperImage = new ImageHandler();
        createButton = new ScreenButton();
        goBackButton = new ScreenButton();
        selectorImage = new ImageHandler();
        textFieldImage = new ImageHandler();
        spriteBatch = new SpriteBatch();
        arcadepixFont.load("arcadepix.ttf");
        arcadepixFont.setPixelSizes(14);
        jumpjackFont.load("jumpjack.ttf");
        jumpjackFont.setPixelSizes(12);
        backgroundImahe.load("ui/background.png");

        color = new ColorImpl(1, 1, 1, 1);
        cursor.load("ui/cursor.png");
        selectorImage.load("ui/cursorElement.png");
        textFieldImage.load("ui/textField.png");
        paperImage.load("ui/foreground.png");
        logoImage.load("ui/jumpJackLogo.png");

        TextField.TextFieldStyle
                textFieldStyle = new TextField.TextFieldStyle(
                arcadepixFont.getFont(), color.color(),
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
                if (key == '\n')
                    textField1.getOnscreenKeyboard().show(false);
            }
        });

        textField.setMaxLength(9);
        backgroundImahe.blit(54, 27, paperImage);
        backgroundImahe.blit(120, 164, logoImage);
        addActor(textField);
        sprite = backgroundImahe.render();

        createButton = new ScreenButton();
        createButton.choice = new ImageHandler();
        createButton.choice.load("ui/startChoice.png");
        createButton.background.load("ui/startButton.png");
        createButton.screen.blit(createButton.background);
        createButton.setPosition(JJEngine.getInstance().renderWidth / 2 - createButton.background.getWidth() / 2, 98);
        createButton.font = jumpjackFont;
        createButton.textMessage = "qnheary";
        createButton.textY = 19;
        createButton.textX = 80;

        goBackButton = new ScreenButton();
        goBackButton.choice = new ImageHandler();
        goBackButton.choice.load("ui/startChoice.png");
        goBackButton.background.load("ui/startButton.png");
        goBackButton.screen.blit(goBackButton.background);
        goBackButton.setPosition(JJEngine.getInstance().renderWidth / 2 - goBackButton.background.getWidth() / 2, 68);
        goBackButton.font = jumpjackFont;
        goBackButton.textMessage = "mahae";
        goBackButton.textY = 19;
        goBackButton.textX = 90;

        textField.setOnlyFontChars(true);
        textField.setMessageText(" Hi!");
        textField.setPosition(JJEngine.getInstance().renderWidth / 2 - textFieldImage.getWidth() / 2, 128);
        textField.setSize(textFieldImage.getWidth(), textFieldImage.getHeight());
    }

    public String getName() {
        int length = textField.getText().length();
        return length > 0 ? textField.getText().substring(1, length) + ".JJ" : "";
    }

    public void dispose() {
        selectorImage.dispose();
        textFieldImage.dispose();
        spriteBatch.dispose();
        paperImage.dispose();
        cursor.dispose();
        goBackButton.dispose();
        createButton.dispose();
        backgroundImahe.dispose();
        arcadepixFont.dispose();
        jumpjackFont.dispose();
    }
}
