package com.vittach.jumpjack.framework;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttribute;

import java.util.List;

public class MeshCompress {

    public static Mesh mergeMeshes(List<Mesh> meshes, List<Matrix4> transforms) {
        if (meshes == null || meshes.isEmpty()) return null;

        int indexOffset = 0;
        int vertexOffset = 0;
        int indexTotalSize = 0;
        int vertexTotalSize = 0;
        int vertexSizeOffsets = 0;

        for (Mesh mesh : meshes) {
            vertexTotalSize += mesh.getNumVertices() * mesh.getVertexSize() / 4;
            indexTotalSize += mesh.getNumIndices();
        }

        final short indices[] = new short[indexTotalSize];
        final float vertices[] = new float[vertexTotalSize];

        for (int i = 0; i < meshes.size(); i++) {
            Mesh meshAttribute = meshes.get(i);

            VertexAttribute posAttr = meshAttribute.getVertexAttribute(VertexAttributes.Usage.Position);
            int numIndices = meshAttribute.getNumIndices();
            int numVertices = meshAttribute.getNumVertices();
            int vertexSize = meshAttribute.getVertexSize() / 4;
            int baseSize = numVertices * vertexSize;

            {
                meshAttribute.getIndices(indices, indexOffset);
                for (int c = indexOffset; c < (indexOffset + numIndices); c++) {
                    indices[c] += vertexOffset;
                }
                indexOffset += numIndices;
            }

            meshAttribute.getVertices(0, baseSize, vertices, vertexSizeOffsets);
            int offset = posAttr.offset / 4;
            int dimension = posAttr.numComponents;
            Matrix4 matrix4 = transforms.get(i);
            Mesh.transform(matrix4, vertices, vertexSize, offset, dimension, vertexOffset, numVertices);

            vertexOffset += numVertices;
            vertexSizeOffsets += baseSize;
        }

        Mesh result = new Mesh(true, vertexOffset, indexTotalSize, meshes.get(0).getVertexAttributes());
        result.setVertices(vertices);
        result.setIndices(indices);
        return result;
    }
}
