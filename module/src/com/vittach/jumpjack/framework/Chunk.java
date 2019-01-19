package com.vittach.jumpjack.framework;

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
