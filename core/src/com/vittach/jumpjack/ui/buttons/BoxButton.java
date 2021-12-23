package com.vittach.jumpjack.ui.buttons;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.vittach.jumpjack.MainEngine;
import com.vittach.jumpjack.framework.ImageHandler;

public class BoxButton extends ScreenButton {
    private final SpriteBatch spriteBatch;

    private final ImageHandler cellBoxImage;
    private final ImageHandler cubesImage;

    private final int oldColumn = -1;
    private final int oldRow = -1;
    private final int boxSize = 16;

    private final MainEngine engineInstance = MainEngine.getInstance();

    public BoxButton() {
        spriteBatch = new SpriteBatch();

        cellBoxImage = new ImageHandler().load("ui/cell_box_default.png");
        cubesImage = new ImageHandler().load("ui/cubes_sprite.png");
        foreground.load("ui/selected_box.png");
        changeBox();
    }

    public boolean touchUp(int x, int y, int id, int button) {
        onClicked(x, y, new ButtonClickListener() {
            @Override
            public void onClicked() {
                engineInstance.fpController.onEscape(true);
            }
        });
        return true;
    }

    public void display(Viewport viewport) {
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        spriteBatch.begin();
        draw(spriteBatch);
        spriteBatch.end();
    }

    private void changeBox() {
        screen.blit(10, 10, cellBoxImage);
        screen.blit(foreground);
        screen.blit(17, 17, cubesImage, oldColumn * boxSize, oldRow * boxSize, boxSize, boxSize);
    }

    public void dispose() {
        cubesImage.dispose();
        cellBoxImage.dispose();
        spriteBatch.dispose();
    }
}
