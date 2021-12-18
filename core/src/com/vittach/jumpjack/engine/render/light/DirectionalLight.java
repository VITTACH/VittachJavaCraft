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

    public DirectionalLight(MainScreen mainScreen, final Vector3 position, final Vector3 direction) {
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
        camera.near = 0.1f;
        camera.far = engine.fpController.viewDistance;
        camera.position.set(position);
        camera.lookAt(direction);
        camera.update();
    }

    @Override
    public void render(ModelInstance modelInstance, Matrix4 chunkTrans) {
        frameBuffer = new FrameBuffer(Format.RGBA8888, engine.renderWidth, engine.renderHeight, true);
        frameBuffer.begin();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        applyShader(chunkTrans);

        modelBatch.begin(camera);
        modelBatch.render(modelInstance);
        modelBatch.end();

        frameBuffer.end();

        depthMap = frameBuffer.getColorBufferTexture();
    }

    private void applyShader(Matrix4 chunkTrans) {
        shaderProgram.begin();
        shaderProgram.setUniformf("u_cameraFar", camera.far);
        shaderProgram.setUniformf("u_lightPosition", camera.position);
        shaderProgram.setUniformMatrix("u_chunkTrans", chunkTrans);
    }
}
