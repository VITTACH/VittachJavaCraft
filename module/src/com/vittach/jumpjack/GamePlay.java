package com.vittach.jumpjack;

import com.vittach.jumpjack.framework.ColorImpl;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class GamePlay implements GameScreen {
    private final JJEngine engine;
    private final ColorImpl white;
    private int inputControlKeys[];

    public void render(Viewport view) {
        engine.world.render(view);
        // engine.healthBar.display(view);
        engine.currentBlock.display(view);

        if (engine.deviceId == 1) {
            if (engine.leftStick.joy_states()) {
                //джойстик был отпущен
                engine.human.pressedKey.remove(32);
                engine.human.pressedKey.remove(47);
                engine.human.pressedKey.remove(29);
                engine.human.pressedKey.remove(51);
            } else if (engine.leftStick.joyTimer.visible && !engine.leftStick.joyTimer.isActive()) {
                if (engine.leftStick.analogX() > 10)
                    engine.human.pressedKey.add(inputControlKeys[3]);
                else JJEngine.human.keyUp(32);
                if (engine.leftStick.analogY() > 10)
                    engine.human.pressedKey.add(inputControlKeys[2]);
                else JJEngine.human.keyUp(47);
                if (engine.leftStick.analogX() < -10)
                    engine.human.pressedKey.add(inputControlKeys[1]);
                else JJEngine.human.keyUp(29);
                if (engine.leftStick.analogY() < -10)
                    engine.human.pressedKey.add(inputControlKeys[0]);
                else JJEngine.human.keyUp(51);
            }

            if (engine.rightStick.joy_states()) {
                //правый грибок отпустил
                engine.human.keyUp(34);
                engine.human.keyUp(57);
                engine.human.keyUp(59);
                engine.human.pressedKey.remove(62);
            } else if (engine.rightStick.joyTimer.visible && !engine.rightStick.joyTimer.isActive()) {
                if (engine.rightStick.analogX() > 10)
                    engine.human.keyDown(inputControlKeys[6]);
                else JJEngine.human.keyUp(62);
                if (engine.rightStick.analogY() > 10)
                    engine.human.keyDown(inputControlKeys[5]);
                if (engine.rightStick.analogY() < -10)
                    engine.human.pressedKey.add(inputControlKeys[4]);
                else JJEngine.human.keyUp(59);
                if (engine.rightStick.analogX() < -10)
                    engine.human.pressedKey.add(inputControlKeys[7]);
                else JJEngine.human.keyUp(57);
            }

            engine.leftStick.joyPrint(white);
            engine.rightStick.joyPrint(white);
        }

        if (engine.human.deathWinState > 0) engine.deathWin.Display(view);

        //вызов меню выбора игрового блока
        if (engine.human.pressedKey.contains(131)) {
            Preference.inputListener.clnProces();
            Preference.inputListener.addListener(engine.selectBoxs.isave);
            Preference.inputListener.addListener(engine.selectBoxs.iexit);
            Preference.inputListener.addListener(engine.selectBoxs.iload);
            Preference.inputListener.addListener(engine.selectBoxs.start);
            Preference.inputListener.addListener(engine.selectBoxs);
            if (engine.human.pressedKey.contains(engine.human.FLY)) {
                JJEngine.human.pressedKey.clear();
                engine.human.pressedKey.add(engine.human.FLY);
            } else engine.human.pressedKey.clear();
            engine.screen = 1;
        }

        engine.cursor.setProjectionMatrix(view.getCamera().combined);
        engine.cursor.begin(ShapeType.Filled);
        engine.cursor.circle(Preference.windowWidth / 2f, Preference.windowHeight / 2, 2);
        engine.cursor.end();
    }

    GamePlay(JJEngine jjEngine) {
        this.engine = jjEngine;
        white = new ColorImpl(1, 1, 1, 0.5f);

        if (engine.deviceId == 1) {
            inputControlKeys = new int[8];
            engine.leftStick = new Control(0);
            engine.rightStick = new Control(1);

            inputControlKeys[0] = 51; //W
            inputControlKeys[1] = 29; //A
            inputControlKeys[2] = 47; //S
            inputControlKeys[3] = 32; //D
            inputControlKeys[4] = 59; //SHIFT
            inputControlKeys[5] = 34; //F
            inputControlKeys[6] = 62; //SPACE
            inputControlKeys[7] = 57; //ALT_L
        }
    }
}
