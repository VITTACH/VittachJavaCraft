package com.vittach.jumpjack.framework;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.Vector3;

public class MeshObj {
    private Mesh mesh;
    private Vector3 position;

    public MeshObj(Mesh mesh, float x, float y, float z) {
        this.mesh = mesh;
        position = new Vector3(x, y, z);
    }

    public Mesh getMesh() {
        return mesh;
    }

    public Vector3 getPosition() {
        return position;
    }
}
