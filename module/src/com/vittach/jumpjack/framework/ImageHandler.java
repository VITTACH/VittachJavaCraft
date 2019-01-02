package com.vittach.jumpjack.framework;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.vittach.jumpjack.Preference;

public class ImageHandler {
    private int rotateAngle = 0;
    private FrameBuffer frameBuffer;
    private SpriteBatch spriteBatch;
    private OrthographicCamera orthographicCamera;

    public ImageHandler clone(ImageHandler img) {
        frameBuffer = img.frameBuffer;
        rotateAngle = img.rotateAngle;
        spriteBatch = img.spriteBatch;
        return this;
    }

    public void blit(int x, int y, ImageHandler img, int m, int n, int w, int h) {
        TextureRegion regon;
        Sprite sprt;
        frameBuffer.begin();
        spriteBatch.begin();
        if (w != 0 && h != 0) {
            regon = new TextureRegion(img.flip().getTexture(), m, n, w, h);
            sprt = new Sprite(regon);
            sprt.setPosition(x, y);
            sprt.rotate(img.rotateAngle);
            sprt.flip(false, true);
            sprt.draw(spriteBatch);
        } else {
            sprt = img.flip();
            sprt.setPosition(x, y);
            sprt.draw(spriteBatch);
        }
        spriteBatch.end();
        frameBuffer.end();
    }

    public ImageHandler() {
        spriteBatch = new SpriteBatch();
        frameBuffer = new FrameBuffer(Format.RGBA4444, Preference.windowWidth, Preference.windowHeight, true);
        orthographicCamera = new OrthographicCamera();
        setCamera(Preference.windowWidth, Preference.windowHeight);
    }

    public void load(String path) {
        Texture blankTexture = new Texture(path);
        frameBuffer = new FrameBuffer(Format.RGBA4444, blankTexture.getWidth(), blankTexture.getHeight(), true);
        setCamera(blankTexture.getWidth(), blankTexture.getHeight());
        frameBuffer.begin();
        spriteBatch.begin();
        spriteBatch.draw(blankTexture, 0.0f, 0);
        spriteBatch.end();
        frameBuffer.end();
        blankTexture.dispose();
    }

    public Sprite flip() {
        Sprite buffer = new Sprite(frameBuffer.getColorBufferTexture());
        buffer.rotate(rotateAngle);
        buffer.flip(false, true);
        return buffer;
    }

    public void fontPrint(FontHandler font, int x, int y, String text, ColorImpl color) {
        BitmapFont fnt = font.getFont();
        fnt.setColor(color.color());
        frameBuffer.begin();
        spriteBatch.begin();
        fnt.draw(spriteBatch, text, x, y);
        spriteBatch.end();
        frameBuffer.end();
    }

    public void clear(ColorImpl clr) {
        ShapeRenderer shape = new ShapeRenderer();
        frameBuffer.begin();
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(clr.color());
        shape.rect(0, 0, frameBuffer.getWidth(), frameBuffer.getHeight());
        shape.end();
        frameBuffer.end();
        shape.dispose();
    }

    private void setCamera(int wid, int hei) {
        orthographicCamera.setToOrtho(false, wid, hei);
        spriteBatch.setProjectionMatrix(orthographicCamera.combined);
    }

    public int get_Pixel(int x, int y) {
        Pixmap TexurePix = frameBuffer.getColorBufferTexture().getTextureData().consumePixmap();
        return TexurePix.getPixel(x, y);
    }

    public void clear() {
        createEmpty(this.frameBuffer.getWidth(), frameBuffer.getHeight());
    }

    public void createEmpty(int width, int height) {
        setCamera(width, height);
        frameBuffer.dispose();
        frameBuffer = new FrameBuffer(Format.RGBA4444, width,height,true);
    }

    public void blit(int x, int y, ImageHandler img) {
        blit(x, y, img, 0, 0, 0, 0);
    }

    public void blit(ImageHandler img) {
        blit(0, 0, img);
    }

    public void rotateToTheDegrees(int rotateDegree) {
        rotateAngle = rotateDegree;
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