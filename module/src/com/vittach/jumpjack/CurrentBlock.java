package com.vittach.jumpjack;

import com.vittach.jumpjack.framework.ImageHandler;
import com.badlogic.gdx.utils.viewport.Viewport;

public class CurrentBlock extends ScreenButton {
    private ImageHandler cellBox;
    private ImageHandler allBlocksSprite;
    private int oldMouseX = -1;
    private int oldMouseY = -1;
    private JJEngine engineInst = JJEngine.getInstance();

    CurrentBlock() {
        cellBox = new ImageHandler().load("ui/cellBox.png");
        allBlocksSprite = new ImageHandler().load("ui/allBlocksSprite.png");

        background.load("ui/selectedBlockBox.png");
    }

    public boolean
    touchDown(int x, int y, int id, int b) {
        if (touchDown(x, y, b)) {
            engineInst.human.pressedKey.add(131);
        }
        return true;
    }

    public void display(Viewport viewport) {
        changeBox();
        show(viewport);
    }

    private void changeBox() {
        if (engineInst.worldMapInst.mouseX != oldMouseX
                || engineInst.worldMapInst.mouseY != oldMouseY) {
            oldMouseX = engineInst.worldMapInst.mouseX;
            oldMouseY = engineInst.worldMapInst.mouseY;

            screen.blit(10, 10, cellBox);
            screen.blit(background);
            screen.blit(17, 17,
                    allBlocksSprite, oldMouseX * 16, oldMouseY * 16, 16, 16);
        }
    }

    public void dispose() {
        super.dispose();
        allBlocksSprite.dispose();
        cellBox.dispose();
    }
}
