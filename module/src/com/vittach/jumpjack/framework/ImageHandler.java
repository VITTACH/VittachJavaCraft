package com.vittach.jumpjack.framework;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.vittach.jumpjack.JJEngine;

public class ImageHandler {
    private int rotateAngle = 0;
    private FrameBuffer frameBuffer;
    private SpriteBatch spriteBatch = new SpriteBatch();
    private OrthographicCamera orthographicCamera = new OrthographicCamera();

    public ImageHandler() {
        int displayWidth = (int) JJEngine.getInstance().renderWidth;
        int displayHeight = (int) JJEngine.getInstance().renderHeight;
        frameBuffer = new FrameBuffer(Format.RGBA4444, displayWidth, displayHeight, true);
        setCamera(displayWidth, displayHeight);
    }

    public ImageHandler clone(ImageHandler img) {
        frameBuffer = img.frameBuffer;
        rotateAngle = img.rotateAngle;
        spriteBatch = img.spriteBatch;
        return this;
    }

    public void blit(float x, float y, ImageHandler img) {
        blit(x, y, img, 0, 0, 0, 0);
    }

    public void blit(ImageHandler img) {
        blit(0, 0, img);
    }

    public void blit(float x, float y, ImageHandler image, int m, int n, int w, int h) {
        TextureRegion textureRegion;
        Sprite sprite;
        frameBuffer.begin();
        spriteBatch.begin();
        if (w != 0 && h != 0) {
            textureRegion = new TextureRegion(image.render().getTexture(), m, n, w, h);
            sprite = new Sprite(textureRegion);
            sprite.setPosition(x, y);
            sprite.rotate(image.rotateAngle);
            sprite.flip(false, true);
            sprite.draw(spriteBatch);
        } else {
            sprite = image.render();
            sprite.setPosition(x, y);
            sprite.draw(spriteBatch);
        }
        spriteBatch.end();
        frameBuffer.end();
    }

    public ImageHandler load(String path) {
        Texture blankTexture = new Texture(path);
        frameBuffer = new FrameBuffer(Format.RGBA4444, blankTexture.getWidth(), blankTexture.getHeight(), true);
        setCamera(blankTexture.getWidth(), blankTexture.getHeight());
        frameBuffer.begin();
        spriteBatch.begin();
        spriteBatch.draw(blankTexture, 0.0f, 0);
        spriteBatch.end();
        frameBuffer.end();
        blankTexture.dispose();
        return this;
    }

    public Sprite render() {
        Sprite buffer = new Sprite(frameBuffer.getColorBufferTexture());
        buffer.rotate(rotateAngle);
        buffer.flip(false, true);
        return buffer;
    }

    public void fontPrint(FontHandler font, float x, float y, String text, ColorImpl color) {
        BitmapFont fnt = font.getFont();
        fnt.setColor(color.color());
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
        shape.setColor(clr.color());
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
        createEmpty(this.frameBuffer.getWidth(), frameBuffer.getHeight());
    }

    public void createEmpty(int width, int height) {
        setCamera(width, height);
        frameBuffer.dispose();
        frameBuffer = new FrameBuffer(Format.RGBA4444, width,height,true);
    }

    public void rotate(int degree) {
        rotateAngle = degree;
    }

    public int getHeight() {
        return frameBuffer.getHeight();
    }

    public int getWidth() {
        return frameBuffer.getWidth();
    }

    public void dispose() {
        frameBuffer.dispose();
        spriteBatch.dispose();
    }
}