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
import com.vittach.jumpjack.engine.MainEngine;
import com.vittach.jumpjack.engine.render.GameScene;

public class SunLight extends Light {

    private final Vector3 direction;
    private Texture depthMap;

    private final MainEngine engineInstance = MainEngine.getInstance();

    public SunLight(GameScene gameScene, final Vector3 position, final Vector3 direction) {
        super(gameScene);
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

        camera = new PerspectiveCamera(134f, engineInstance.renderWidth, engineInstance.renderHeight);
        camera.near = 0.1f;
        camera.far = 500f;
        camera.position.set(position);
        camera.lookAt(direction);
        camera.update();
    }

    @Override
    public void render(ModelInstance modelInstance, Matrix4 chunkTrans) {
        FrameBuffer frameBuffer = new FrameBuffer(
            Format.RGBA8888,
            engineInstance.renderWidth,
            engineInstance.renderHeight,
            true
        );
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
