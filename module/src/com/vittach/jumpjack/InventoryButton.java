package com.vittach.jumpjack;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.vittach.jumpjack.framework.ImageHandler;

public class InventoryButton extends ScreenButton {
    private ImageHandler cellBoxImage;
    private ImageHandler allBlocksImage;

    private int oldColumn = -1;
    private int oldRow = -1;

    private JJEngine engineInst = JJEngine.getInstance();

    public InventoryButton() {
        cellBoxImage = new ImageHandler().load("ui/cellBox.png");
        allBlocksImage = new ImageHandler().load("ui/allBlocksSprite.png");
        background.load("ui/selectedBlockBox.png");
    }

    public boolean touchDown(int x, int y, int pointer, int button) {
        if (touchDown(x, y, button)) {
            engineInst.controller.pressedKeys.add(Input.Keys.ESCAPE);
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
