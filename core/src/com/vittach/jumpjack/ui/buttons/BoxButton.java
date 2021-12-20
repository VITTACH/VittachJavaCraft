package com.vittach.jumpjack.ui.buttons;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.vittach.jumpjack.MainEngine;
import com.vittach.jumpjack.framework.ImageHandler;

public class BoxButton extends ScreenButton {
    private final ImageHandler cellBoxImage;
    private final ImageHandler cubesImage;

    private final int oldColumn = -1;
    private final int oldRow = -1;
    private final int boxSize = 16;

    private final MainEngine engineInstance = MainEngine.getInstance();

    public BoxButton() {
        cellBoxImage = new ImageHandler().load("ui/cell_box_default.png");
        cubesImage = new ImageHandler().load("ui/cubes_sprite.png");
        selectedBoxImage.load("ui/selected_box.png");
    }

    public boolean touchDown(int x, int y, int pointer, int button) {
        if (touchDown(x, y, button)) {
            engineInstance.fpController.onEscape(true);
        }
        return true;
    }

    public void display(Viewport viewport) {
        changeBox();
        draw(viewport);
    }

    private void changeBox() {
        screen.blit(10, 10, cellBoxImage);
        screen.blit(selectedBoxImage);
        screen.blit(17, 17, cubesImage, oldColumn * boxSize, oldRow * boxSize, boxSize, boxSize);
    }

    public void dispose() {
        cubesImage.dispose();
        cellBoxImage.dispose();
    }
}
