package com.vittach.jumpjack;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class GamePlay implements GameScreen {
    private final ShapeRenderer mouseCursor = new ShapeRenderer();

    public void render(Viewport view) {
        JJEngine instance = JJEngine.getInstance();
        instance.worldMapInst.render(view);

        instance.currentBlock.display(view);

        // вызов меню выбора нового игрового блока
        if (instance.human.pressedKey.contains(131)) {
            Preference.getInstance().inputListener.cleanProcesses();
            Preference.getInstance().inputListener.addListener(instance.blockSelector.saveButton);
            Preference.getInstance().inputListener.addListener(instance.blockSelector.exitButton);
            Preference.getInstance().inputListener.addListener(instance.blockSelector.loadButton);
            Preference.getInstance().inputListener.addListener(instance.blockSelector.startButton);
            Preference.getInstance().inputListener.addListener(instance.blockSelector);

            if (instance.human.pressedKey.contains(instance.human.FLY)) {
                instance.human.pressedKey.clear();
                instance.human.pressedKey.add(instance.human.FLY);
            } else {
                instance.human.pressedKey.clear();
            }

            instance.currentScreen = 1;
        }

        mouseCursor.setProjectionMatrix(view.getCamera().combined);
        mouseCursor.begin(ShapeType.Filled);
        mouseCursor.circle(JJEngine.getInstance().renderWidth / 2f, JJEngine.getInstance().renderHeight / 2f, 2);
        mouseCursor.end();
    }
}
