package com.vittach.jumpjack.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.vittach.jumpjack.engine.MainEngine;

public class ImageHandler {
    private FrameBuffer frameBuffer;
    private SpriteBatch spriteBatch = new SpriteBatch();
    private int rotateDegree = 0;

    private final OrthographicCamera camera = new OrthographicCamera();

    public void dispose() {
        frameBuffer.dispose();
        spriteBatch.dispose();
    }

    public ImageHandler() {
        MainEngine engineInstance = MainEngine.getInstance();
        int renderWidth = engineInstance.renderWidth;
        int renderHeight = engineInstance.renderHeight;
        frameBuffer = new FrameBuffer(Format.RGBA8888, renderWidth, renderHeight, true);
        setCamera(renderWidth, renderHeight);
    }

    public ImageHandler clone(ImageHandler img) {
        frameBuffer = img.frameBuffer;
        rotateDegree = img.rotateDegree;
        spriteBatch = img.spriteBatch;
        return this;
    }

    public void blit(float x, float y, ImageHandler img) {
        blit(x, y, img, 0, 0, 0, 0);
    }

    public void blit(ImageHandler img) {
        blit(0, 0, img);
    }

    public void blit(float x, float y, ImageHandler imageHandler, int m, int n, int w, int h) {
        frameBuffer.begin();
        spriteBatch.begin();

        Sprite sprite;
        if (w != 0 && h != 0) {
            sprite = new Sprite(imageHandler.getTexture(), m, n, w, h);
            sprite.setPosition(x, y);
            sprite.rotate(imageHandler.rotateDegree);
            sprite.flip(false, true);
        } else {
            sprite = imageHandler.render();
            sprite.setPosition(x, y);
        }
        sprite.draw(spriteBatch);

        spriteBatch.end();
        frameBuffer.end();
    }

    public ImageHandler load(String path) {
        Texture blank = new Texture(path);
        frameBuffer = new FrameBuffer(Format.RGBA8888, blank.getWidth(), blank.getHeight(), true);
        setCamera(blank.getWidth(), blank.getHeight());

        frameBuffer.begin();
        spriteBatch.begin();
        spriteBatch.draw(blank, 0.f, 0.f);
        spriteBatch.end();
        frameBuffer.end();

        blank.dispose();
        return this;
    }

    public Sprite render() {
        Sprite sprite = new Sprite(frameBuffer.getColorBufferTexture());
        sprite.rotate(rotateDegree);
        sprite.flip(false, true);
        return sprite;
    }

    public Texture getTexture() {
        return frameBuffer.getColorBufferTexture();
    }

    public void fontPrint(FontHandler font, float x, float y, String text, com.vittach.jumpjack.utils.ColorImpl color) {
        BitmapFont bitmapFont = font.getBitmapFont();
        bitmapFont.setColor(color.getColor());

        frameBuffer.begin();
        spriteBatch.begin();
        bitmapFont.draw(spriteBatch, text, x, y);
        spriteBatch.end();
        frameBuffer.end();
    }

    public void fillColor(ColorImpl clr) {
        ShapeRenderer renderer = new ShapeRenderer();
        frameBuffer.begin();

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(clr.getColor());
        renderer.rect(0, 0, frameBuffer.getWidth(), frameBuffer.getHeight());
        renderer.end();

        frameBuffer.end();
        renderer.dispose();
    }

    public void clear() {
        setEmpty(this.frameBuffer.getWidth(), this.frameBuffer.getHeight());
    }

    public void setEmpty(int width, int height) {
        frameBuffer = new FrameBuffer(Format.RGBA8888, width, height, true);
        setCamera(width, height);
    }

    public void rotate(int rotateDegree) {
        this.rotateDegree = rotateDegree;
    }

    public int getHeight() {
        return frameBuffer.getHeight();
    }

    public int getWidth() {
        return frameBuffer.getWidth();
    }

    private void setCamera(int width, int height) {
        camera.setToOrtho(false, width, height);
        spriteBatch.setProjectionMatrix(camera.combined);
    }
}