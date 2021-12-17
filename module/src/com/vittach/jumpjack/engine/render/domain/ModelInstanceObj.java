package com.vittach.jumpjack.engine.render.domain;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

public class ModelInstanceObj {

    private ModelInstance modelInstance;
    private Texture deptMap;

    public ModelInstanceObj(ModelInstance modelInstance, Texture deptMap) {
        this.modelInstance = modelInstance;
        this.deptMap = deptMap;
    }

    public ModelInstance getModelInstance() {
        return modelInstance;
    }

    public Texture getDepthMap() {
        return deptMap;
    }

}
