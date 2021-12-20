package com.vittach.jumpjack.framework;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.vittach.jumpjack.MainEngine;

public class ImageHandler {
    private int rotationAngle = 0;
    private FrameBuffer frameBuffer;
    private SpriteBatch spriteBatch = new SpriteBatch();
    private OrthographicCamera orthographicCamera = new OrthographicCamera();

    public void dispose() {
        frameBuffer.dispose();
        spriteBatch.dispose();
    }

    public ImageHandler() {
        int renderWidth = MainEngine.getInstance().renderWidth;
        int renderHeight = MainEngine.getInstance().renderHeight;
        frameBuffer = new FrameBuffer(Format.RGBA4444, renderWidth, renderHeight, true);
        setCamera(renderWidth, renderHeight);
    }

    public ImageHandler clone(ImageHandler img) {
        frameBuffer = img.frameBuffer;
        rotationAngle = img.rotationAngle;
        spriteBatch = img.spriteBatch;
        return this;
    }

    public void blit(float x, float y, ImageHandler img) {
        blit(x, y, img, 0, 0, 0, 0);
    }

    public void blit(ImageHandler img) {
        blit(0, 0, img);
    }

    public void blit(float x, float y, ImageHandler img, int m, int n, int w, int h) {
        Sprite sprite;
        frameBuffer.begin();
        spriteBatch.begin();
        if (w != 0 && h != 0) {
            sprite = new Sprite(img.getTexture(), m, n, w, h);
            sprite.setPosition(x, y);
            sprite.rotate(img.rotationAngle);
            sprite.flip(false, true);
        } else {
            sprite = img.render();
            sprite.setPosition(x, y);
        }
        sprite.draw(spriteBatch);
        spriteBatch.end();
        frameBuffer.end();
    }

    public ImageHandler load(String path) {
        Texture blankTexture = new Texture(path);
        frameBuffer = new FrameBuffer(Format.RGBA4444, blankTexture.getWidth(), blankTexture.getHeight(), true);
        setCamera(blankTexture.getWidth(), blankTexture.getHeight());
        frameBuffer.begin();
        spriteBatch.begin();
        spriteBatch.draw(blankTexture, 0.f, 0.f);
        spriteBatch.end();
        frameBuffer.end();
        blankTexture.dispose();
        return this;
    }

    public Sprite render() {
        Sprite buffer = new Sprite(frameBuffer.getColorBufferTexture());
        buffer.rotate(rotationAngle);
        buffer.flip(false, true);
        return buffer;
    }

    public Texture getTexture() {
        return frameBuffer.getColorBufferTexture();
    }

    public void fontPrint(FontHandler font, float x, float y, String text, ColorImpl color) {
        BitmapFont fnt = font.getBitmapFont();
        fnt.setColor(color.getColor());
        frameBuffer.begin();
        spriteBatch.begin();
        fnt.draw(spriteBatch, text, x, y);
        spriteBatch.end();
        frameBuffer.end();
    }

    public void fillColor(ColorImpl clr) {
        ShapeRenderer shape = new ShapeRenderer();
        frameBuffer.begin();
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(clr.getColor());
        shape.rect(0, 0, frameBuffer.getWidth(), frameBuffer.getHeight());
        shape.end();
        frameBuffer.end();
        shape.dispose();
    }

    private void setCamera(int width, int height) {
        orthographicCamera.setToOrtho(false, width, height);
        spriteBatch.setProjectionMatrix(orthographicCamera.combined);
    }

    public void clear() {
        setEmpty(this.frameBuffer.getWidth(), this.frameBuffer.getHeight());
    }

    public void setEmpty(int width, int height) {
        frameBuffer.dispose();
        frameBuffer = new FrameBuffer(Format.RGBA4444, width, height, true);
        setCamera(width, height);
    }

    public void rotate(int degree) {
        rotationAngle = degree;
    }

    public int getHeight() {
        return frameBuffer.getHeight();
    }

    public int getWidth() {
        return frameBuffer.getWidth();
    }
}