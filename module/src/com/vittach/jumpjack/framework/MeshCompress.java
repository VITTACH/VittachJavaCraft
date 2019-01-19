package com.vittach.jumpjack.framework;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttribute;

import java.util.List;

public class MeshCompress {

    public static Mesh mergeMeshes(List<Mesh> meshes, List<Matrix4> transforms) {
        if (meshes == null || meshes.isEmpty()) return null;

        int indexTotal = 0, vertexTotal = 0, vertexSizeOffset = 0, indexOffset = 0, vertexOffset = 0;

        for (Mesh mesh : meshes) {
            vertexTotal += mesh.getNumVertices() * mesh.getVertexSize() / 4;
            indexTotal += mesh.getNumIndices();
        }

        final short indices[] = new short[indexTotal];
        final float vertices[] = new float[vertexTotal];
        for (int i = 0; i < meshes.size(); i++) {
            Mesh meshAttribute = meshes.get(i);
            VertexAttribute posAttr = meshAttribute.getVertexAttribute(VertexAttributes.Usage.Position);
            int numIndices = meshAttribute.getNumIndices();
            int numVertices = meshAttribute.getNumVertices();
            int vertexSize = meshAttribute.getVertexSize() / 4;
            int count = numVertices * vertexSize;

            meshAttribute.getVertices(0, count, vertices, vertexSizeOffset);
            int offset = posAttr.offset / 4;
            int dimension = posAttr.numComponents;
            Matrix4 matrix4 = transforms.get(i);
            Mesh.transform(matrix4, vertices, vertexSize, offset, dimension, vertexOffset, numVertices);

            meshAttribute.getIndices(indices, indexOffset);
            for (int c = indexOffset; c < (indexOffset + numIndices); c++) {
                indices[c] += vertexOffset;
            }

            indexOffset += numIndices;
            vertexOffset += numVertices;
            vertexSizeOffset += count;
        }

        Mesh mergeMesh = new Mesh(true, vertexOffset, indexTotal, meshes.get(0).getVertexAttributes());
        mergeMesh.setVertices(vertices);
        mergeMesh.setIndices(indices);
        return mergeMesh;
    }
}
