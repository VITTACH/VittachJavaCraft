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

public class NewCreate extends Stage implements ProcessorInput {
    Sprite sprite;
    ScreenButton goback, create;
    private FontHandler ifont, lfont;
    TextField textField;
    SpriteBatch spritew;
    private ImageHandler cursor, ilogo, iSElector;
    private ImageHandler backg, ipaper, textfield;
    public int PressedKeyCode = -1;
    private ColorImpl color;

    @Override
    public void setIDOffset(int myid) {
    }

    @Override
    public boolean
    touchUp(int x, int y, int id, int btn) {
        if (create.MyTouch_Down(x, y))
            if (getName().length() > 3) PressedKeyCode = 1;
            else textField.setMessageText(" input correct name");
        if (goback.MyTouch_Down(x, y)) PressedKeyCode = 2;
        return true;
    }

    void Display(Viewport view) {
        view.apply();
        spritew.setProjectionMatrix(view.getCamera().combined);
        spritew.begin();
        sprite.draw(spritew);
        spritew.end();
        setViewport(view);
        act();
        draw();
        create.MyDisplay(view);
        goback.MyDisplay(view);
    }

    NewCreate() {
        ifont = new FontHandler();
        lfont = new FontHandler();
        ilogo = new ImageHandler();
        backg = new ImageHandler();
        sprite = new Sprite();
        cursor = new ImageHandler();
        ipaper = new ImageHandler();
        create = new ScreenButton();
        goback = new ScreenButton();
        iSElector = new ImageHandler();
        textfield = new ImageHandler();
        spritew = new SpriteBatch();
        ifont.load("arcadepix.ttf");
        ifont.setPixelSizes(14);
        lfont.load("jumpjack.ttf");
        lfont.setPixelSizes(12);
        backg.load("ui/background.png");

        color = new ColorImpl(1, 1, 1, 1);
        cursor.load("ui/cursor.png");
        iSElector.load("ui/cursorElement.png");
        textfield.load("ui/textField.png");
        ipaper.load("ui/foreground.png");
        ilogo.load("ui/jumpJackLogo.png");

        TextField.TextFieldStyle
                textFieldStyle = new TextField.TextFieldStyle(
                ifont.getFont(), color.color(),
                new TextureRegionDrawable(cursor.flip()),
                new TextureRegionDrawable(iSElector.flip()),
                new TextureRegionDrawable(textfield.flip())
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
        backg.blit(54, 27, ipaper);
        backg.blit(120, 164, ilogo);
        addActor(textField);
        sprite = backg.flip();

        create = new ScreenButton();
        create.choice = new ImageHandler();
        create.choice.load("ui/startChoice.png");
        create.background.load("ui/startButton.png");
        create.backgroundHandler.blit(create.background);
        create.setPosition(Preference.windowWidth / 2 - create.background.getWidth() / 2, 98);
        create.font = lfont;
        create.textMessage = "qnheary";
        create.textY = 19;
        create.textX = 80;

        goback = new ScreenButton();
        goback.choice = new ImageHandler();
        goback.choice.load("ui/startChoice.png");
        goback.background.load("ui/startButton.png");
        goback.backgroundHandler.blit(goback.background);
        goback.setPosition(Preference.windowWidth / 2 - goback.background.getWidth() / 2, 68);
        goback.font = lfont;
        goback.textMessage = "mahae";
        goback.textY = 19;
        goback.textX = 90;

        textField.setOnlyFontChars(true);
        textField.setMessageText(" Hi!");
        textField.setPosition(Preference.windowWidth / 2 - textfield.getWidth() / 2, 128);
        textField.setSize(textfield.getWidth(), textfield.getHeight());
    }

    public String getName() {
        int length = textField.getText().length();
        return length > 0 ? textField.getText().substring(1, length) + ".JJ" : "";
    }

    public void dispose() {
        iSElector.dispose();
        textfield.dispose();
        spritew.dispose();
        ipaper.dispose();
        cursor.dispose();
        goback.dispose();
        create.dispose();
        backg.dispose();
        ifont.dispose();
        lfont.dispose();
    }
}
