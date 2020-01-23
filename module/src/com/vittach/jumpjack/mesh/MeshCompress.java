package com.vittach.jumpjack.mesh;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;

import java.util.List;
import java.util.Map;

public class MeshCompress {

    public static Mesh mergeMeshes(
        List<MeshObj> meshObjs,
        Map<String, List<TextureRegion>> textureRegions,
        List<Matrix4> matrix
    ) {
        if (meshObjs == null || meshObjs.isEmpty()) return null;

        int indexTotal = 0;
        int vertexTotal = 0;

        for (MeshObj meshObj : meshObjs) {
            Mesh curMesh = meshObj.getMesh();
            vertexTotal += curMesh.getNumVertices() * curMesh.getVertexSize() / 4;
            indexTotal += curMesh.getNumIndices();
        }

        final short indices[] = new short[indexTotal];
        final float vertices[] = new float[vertexTotal];

        int destOffset = 0;
        int indexOffset = 0;
        int vertexOffset = 0;

        for (int i = 0; i < meshObjs.size(); i++) {
            Mesh mesh = meshObjs.get(i).getMesh();
            final VertexAttribute positionCoordinates = mesh.getVertexAttribute(VertexAttributes.Usage.Position);
            int numIndices = mesh.getNumIndices();
            int numberVertices = mesh.getNumVertices();
            int vertexSize = mesh.getVertexSize() / 4;
            int length = numberVertices * vertexSize;

            mesh.getVertices(0, length, vertices, destOffset);

            // Experimental culling invisible surface
            int oneSideVertices = 4 * vertexSize;
            for (int j = 0; j < length; j++) {
                // top surface
                if (j >= 2 * oneSideVertices && j < 3 * oneSideVertices) {
                    continue;
                }
                vertices[j + destOffset] = 0;
            }

            int offset = positionCoordinates.offset / 4;
            int dimension = positionCoordinates.numComponents;
            Mesh.transform(matrix.get(i), vertices, vertexSize, offset, dimension, vertexOffset, numberVertices);

            mesh.getIndices(indices, indexOffset);
            for (int index = indexOffset; index < (indexOffset + numIndices); index++) {
                indices[index] += vertexOffset;
            }

            reInitTextures(mesh, textureRegions.get(meshObjs.get(i).getSymbol()));

            indexOffset += numIndices;
            vertexOffset += numberVertices;
            destOffset += length;
        }

        Mesh newMesh = new Mesh(true, vertexOffset, indexTotal, meshObjs.get(0).getMesh().getVertexAttributes());
        newMesh.setVertices(vertices);
        newMesh.setIndices(indices);
        return newMesh;
    }

    static void reInitTextures(Mesh mergeMesh, List<TextureRegion> regions) {
        int textureOffset = mergeMesh.getVertexAttributes().getOffset(VertexAttributes.Usage.TextureCoordinates);
        int vertexSize = mergeMesh.getVertexSize() / 4; // divide to convert bytes to floats
        int length = vertexSize * mergeMesh.getNumVertices();
        float[] vertices = new float[length];

        mergeMesh.getVertices(vertices);
        for (int i = textureOffset, corner = 0, side = 0; i < length; i += vertexSize) {
            if (corner == 0) {
                vertices[i] = regions.get(side).getU();
                vertices[i + 1] = regions.get(side).getV();
            } else if (corner == 1) {
                vertices[i] = regions.get(side).getU();
                vertices[i + 1] = regions.get(side).getV2();
            } else if (corner == 2) {
                vertices[i] = regions.get(side).getU2();
                vertices[i + 1] = regions.get(side).getV2();
            } else if (corner == 3) {
                vertices[i] = regions.get(side).getU2();
                vertices[i + 1] = regions.get(side).getV();
                corner = 0;
                side++;
                if (side >= regions.size()) {
                    side = 0;
                }
                continue;
            }
            corner++;
        }

        mergeMesh.setVertices(vertices);
    }
}
