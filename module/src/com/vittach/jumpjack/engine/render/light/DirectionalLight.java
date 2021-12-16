package com.vittach.jumpjack.engine.render.light;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.vittach.jumpjack.MainEngine;
import com.vittach.jumpjack.engine.render.MainScreen;

public class DirectionalLight extends Light {

    public Vector3 direction;
    public FrameBuffer frameBuffer;
    public Texture depthMap;

    private final MainEngine engine = MainEngine.getInstance();

    public DirectionalLight(final MainScreen mainScreen, final Vector3 position, final Vector3 direction) {
        super(mainScreen);
        this.position = position;
        this.direction = direction;
        init();
    }

    public Texture getDepthMap() {
        return depthMap;
    }

    @Override
    public void init() {
        super.init();

        camera = new PerspectiveCamera(120f, engine.renderWidth, engine.renderHeight);
        camera.near = 1f;
        camera.far = 70;
        camera.position.set(position);
        camera.lookAt(direction);
        camera.update();
    }

    @Override
    public void render(ModelInstance modelInstance, Matrix4 chunk) {
        if (frameBuffer == null) {
            frameBuffer = new FrameBuffer(Format.RGBA8888, engine.renderWidth, engine.renderHeight, true);
        }

        frameBuffer.begin();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        shaderProgram.bind();
        shaderProgram.setUniformf("u_cameraFar", camera.far);
        shaderProgram.setUniformf("u_lightPosition", camera.position);
        shaderProgram.setUniformMatrix("u_chunkTrans", chunk);

        modelBatch.begin(camera);
        modelBatch.render(modelInstance);
        modelBatch.end();

        frameBuffer.end();

        depthMap = frameBuffer.getColorBufferTexture();
    }
}
