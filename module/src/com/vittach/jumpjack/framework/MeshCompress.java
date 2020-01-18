package com.vittach.jumpjack.framework;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;

import java.util.List;

public class MeshCompress {

    static void remapTextures(Mesh mesh, TextureRegion region) {
        final int textureOffset = mesh.getVertexAttributes().getOffset(VertexAttributes.Usage.TextureCoordinates);
        int vertexSize = mesh.getVertexSize() / 4; // divide to convert bytes to floats
        int length = vertexSize * mesh.getNumVertices();
        float[] vertices = new float[length];

        mesh.getVertices(vertices);
        for (int i = textureOffset, side = 0; i < length; i += vertexSize) {
            if (side == 0) {
                vertices[i] = region.getU();
                vertices[i + 1] = region.getV();
            } else if (side == 1) {
                vertices[i] = region.getU();
                vertices[i + 1] = region.getV2();
            } else if (side == 2) {
                vertices[i] = region.getU2();
                vertices[i + 1] = region.getV2();
            } else if (side == 3) {
                vertices[i] = region.getU2();
                vertices[i + 1] = region.getV();
                side = 0;
                continue;
            }
            side++;
        }

        mesh.setVertices(vertices);
    }

    public static Mesh mergeMeshes(List<Mesh> meshes, List<TextureRegion> textureRegions, List<Matrix4> matrix) {
        if (meshes == null || meshes.isEmpty()) return null;

        int indexTotal = 0;
        int vertexTotal = 0;

        for (Mesh mesh : meshes) {
            vertexTotal += mesh.getNumVertices() * mesh.getVertexSize() / 4;
            indexTotal += mesh.getNumIndices();
        }

        final short indices[] = new short[indexTotal];
        final float vertices[] = new float[vertexTotal];

        int destOffset = 0;
        int indexOffset = 0;
        int vertexOffset = 0;

        for (int i = 0; i < meshes.size(); i++) {
            final Mesh mesh = meshes.get(i);
            final VertexAttribute positionCoordinates = mesh.getVertexAttribute(VertexAttributes.Usage.Position);
            int numIndices = mesh.getNumIndices();
            int numberVertices = mesh.getNumVertices();
            int vertexSize = mesh.getVertexSize() / 4;
            int length = numberVertices * vertexSize;

            mesh.getVertices(0, length, vertices, destOffset);
            int offset = positionCoordinates.offset / 4;
            int dimension = positionCoordinates.numComponents;
            Mesh.transform(matrix.get(i), vertices, vertexSize, offset, dimension, vertexOffset, numberVertices);

            mesh.getIndices(indices, indexOffset);
            for (int c = indexOffset; c < (indexOffset + numIndices); c++) {
                indices[c] += vertexOffset;
            }

            remapTextures(mesh, textureRegions.get(i));

            indexOffset += numIndices;
            vertexOffset += numberVertices;
            destOffset += length;
        }

        Mesh mergeMesh = new Mesh(true, vertexOffset, indexTotal, meshes.get(0).getVertexAttributes());
        mergeMesh.setVertices(vertices);
        mergeMesh.setIndices(indices);
        return mergeMesh;
    }
}
