package com.vittach.jumpjack.engine.render;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.Vector3;

public class MeshObj {
    private Mesh mesh;
    private String symbol;
    private Vector3 position;

    public MeshObj(Mesh mesh, String symbol, Vector3 position) {
        this.mesh = mesh;
        this.symbol = symbol;
        this.position = position;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public Vector3 getPosition() {
        return position;
    }

    public String getSymbol() {
        return symbol;
    }
}
