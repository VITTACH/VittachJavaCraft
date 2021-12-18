package com.vittach.jumpjack.ui.buttons;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.vittach.jumpjack.MainEngine;
import com.vittach.jumpjack.framework.ImageHandler;

public class BoxButton extends ScreenButton {
    private final ImageHandler cellBoxImage;
    private final ImageHandler allBlocksImage;

    private int oldColumn = -1;
    private int oldRow = -1;

    private MainEngine engineInstance = MainEngine.getInstance();

    public BoxButton() {
        cellBoxImage = new ImageHandler().load("ui/cell_box_default.png");
        allBlocksImage = new ImageHandler().load("ui/cubes_sprite.png");
        background.load("ui/selected_box.png");
    }

    public boolean touchDown(int x, int y, int pointer, int button) {
        if (touchDown(x, y, button)) {
            engineInstance.fpController.pressedKeys.add(Input.Keys.ESCAPE);
        }
        return true;
    }

    public void display(Viewport viewport) {
        changeBox();
        show(viewport);
    }

    private void changeBox() {
        screen.blit(10, 10, cellBoxImage);
        screen.blit(background);
        screen.blit(17, 17, allBlocksImage, oldColumn * 16, oldRow * 16, 16, 16);
    }

    public void dispose() {
        allBlocksImage.dispose();
        cellBoxImage.dispose();
    }
}
