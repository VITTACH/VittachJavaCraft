package com.vittach.jumpjack.engine.render.light;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.vittach.jumpjack.engine.render.GameScene;
import com.vittach.jumpjack.engine.render.shader.DepthMapShader;

public abstract class Light {
    public static ShaderProgram shaderProgram = null;
    public static ModelBatch modelBatch = null;

    public Camera camera;
    public GameScene gameScene;

    public Vector3 position = new Vector3();

    public Light(final GameScene gameScene) {
        this.gameScene = gameScene;
    }

    public abstract Texture getDepthMap();

    public abstract void render(ModelInstance modelInstance, Matrix4 chunk);

    public Camera getCamera() {
        return camera;
    }

    public void init() {
        if (shaderProgram == null) {
            shaderProgram = gameScene.setupShader("depthmap");
            modelBatch = new ModelBatch(new DefaultShaderProvider() {
                @Override
                protected Shader createShader(final Renderable renderable) {
                    return new DepthMapShader(renderable, shaderProgram);
                }
            });
        }
    }

}
