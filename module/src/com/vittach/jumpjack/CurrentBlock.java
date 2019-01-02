package com.vittach.jumpjack;

import com.vittach.jumpjack.framework.ImageHandler;
import com.badlogic.gdx.utils.viewport.Viewport;

public class CurrentBlock extends ScreenButton {
    private ImageHandler cellBoxs, blockSprite;
    private int oldMouseX = -1, oldMouseY = -1;

    CurrentBlock() {
        cellBoxs = new ImageHandler();
        blockSprite = new ImageHandler();
        cellBoxs.load("ui/cellBox.png");
        blockSprite.load("ui/allBlocksSprite.png");
        background.load("ui/selectedBlockBox.png");
    }

    public boolean
    touchDown(int x, int y, int id, int b) {
        if (MyTouch_Down(x, y, b))
            JJEngine.human.pressedKey.add(131);
        return true;
    }

    public void display(Viewport viewport) {
        changeBox();
        MyDisplay(viewport);
    }

    private void changeBox() {
        if (JJEngine.world.mouseX != oldMouseX || JJEngine.world.mouseY != oldMouseY) {
            oldMouseX = JJEngine.world.mouseX;
            oldMouseY = JJEngine.world.mouseY;
            backgroundHandler.blit(10, 10, cellBoxs);
            backgroundHandler.blit(background);
            backgroundHandler.blit(17, 17, blockSprite, oldMouseX * 16, oldMouseY * 16, 16, 16);
        }
    }

    public void dispose() {
        blockSprite.dispose();
        super.dispose();
        cellBoxs.dispose();
    }
}
