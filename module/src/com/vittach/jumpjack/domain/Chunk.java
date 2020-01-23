package com.vittach.jumpjack.domain;

import com.vittach.jumpjack.mesh.MeshObj;

import java.util.List;

public class Chunk {
    private List<MeshObj> meshObjs;

    public Chunk(List<MeshObj> meshObjs) {
        this.meshObjs = meshObjs;
    }

    public List<MeshObj> getMeshObjs() {
        return meshObjs;
    }
}
