package com.vittach.jumpjack.engine.render;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MeshCompress {

    private static boolean isEmptyAtIndex(List<MeshObj> meshes, Integer index) {
        if (index < 0 || index >= meshes.size()) return true;

        return "".equals(meshes.get(index).getSymbol());
    }

    private static boolean hasSurface(
        List<MeshObj> meshes,
        Integer i,
        Integer j,
        Integer sideLength,
        Integer chunkLength
    ) {

        int floorSize = chunkLength * chunkLength;
        boolean result;
        if (j >= 0 && j < sideLength) {
            int index = i - (floorSize * (i / floorSize));
            result = index % chunkLength == 0 || (index % chunkLength != 0 && isEmptyAtIndex(meshes, i - 1)); // front
        } else if (j >= sideLength && j < 2 * sideLength) {
            int index = i - (floorSize * (i / floorSize)) + 1;
            result = index % chunkLength == 0 || (index % chunkLength != 0 && isEmptyAtIndex(meshes, i + 1)); // back
        } else if (j >= 2 * sideLength && j < 3 * sideLength) {
            result = isEmptyAtIndex(meshes, i - floorSize); // bottom
        } else if (j >= 3 * sideLength && j < 4 * sideLength) {
            result = isEmptyAtIndex(meshes, i + floorSize); // top
        } else if (j >= 4 * sideLength && j < 5 * sideLength) {
            int index = i - (floorSize * (i / floorSize));
            if (index < chunkLength) {
                result = true;
            } else {
                result = isEmptyAtIndex(meshes, i - chunkLength); // left;
            }
        } else {
            int index = i - (floorSize * (i / floorSize));
            if (index >= floorSize - chunkLength) {
                result = true;
            } else {
                result = isEmptyAtIndex(meshes, i + chunkLength); // right
            }
        }

        return result;
    }

    public static Mesh compressMeshes(
        List<MeshObj> meshes,
        Map<String, List<TextureRegion>> textureMap,
        List<Matrix4> translates,
        Integer chunkLength
    ) {
        if (meshes == null || meshes.isEmpty()) return null;

        final List<Float> vertexList = new ArrayList<Float>();
        final List<Short> indiceList = new ArrayList<Short>();

        int destOffset = 0;
        int indexOffset = 0;
        int vertexOffset = 0;

        for (int i = 0; i < meshes.size(); i++) {
            if (isEmptyAtIndex(meshes, i)) continue;

            final Mesh mesh = meshes.get(i).getMesh();
            VertexAttribute positionCoordinates = mesh.getVertexAttribute(VertexAttributes.Usage.Position);
            int dimensions = positionCoordinates.numComponents;
            int offset = positionCoordinates.offset / 4;

            int numberIndices = mesh.getNumIndices();
            int numberVertices = mesh.getNumVertices();
            int vertexSize = mesh.getVertexSize() / 4;
            int length = numberVertices * vertexSize;

            float[] vertices = new float[length];
            short[] indices = new short[numberIndices];
            mesh.getIndices(indices);
            mesh.getVertices(vertices);

            Mesh.transform(translates.get(i), vertices, vertexSize, offset, dimensions, 0, numberVertices);

            reMappingTexture(mesh, vertices, textureMap.get(meshes.get(i).getSymbol()));

            // Experimental culling invisible surface
            int sideLength = 4 * vertexSize;
            for (int j = 0, k = 0; j < length; j++) {
                if (hasSurface(meshes, i, j, sideLength, chunkLength)) {
                    vertexList.add(destOffset + k++, vertices[j]);
                } else if (j % vertexSize == 0) {
                    numberVertices--;
                }
            }

            for (int j = 0; j < numberIndices; j++) {
                indiceList.add(indexOffset + j, (short) (indices[j] + vertexOffset));
            }

            destOffset += numberVertices * vertexSize;
            vertexOffset += numberVertices;
            indexOffset += numberIndices;
        }

        return mergeMesh(vertexList, indiceList, meshes);
    }

    private static Mesh mergeMesh(List<Float> vertexList, List<Short> indiceList, List<MeshObj> meshes) {
        Mesh mergedMesh = new Mesh(
            true,
            vertexList.size(),
            indiceList.size(),
            meshes.get(0).getMesh().getVertexAttributes()
        );

        int i = 0;
        float[] vertexArray = new float[vertexList.size()];
        for (float val : vertexList) {
            vertexArray[i++] = val;
        }
        mergedMesh.setVertices(vertexArray);

        i = 0;
        short[] indiceArray = new short[indiceList.size()];
        for (short val : indiceList) {
            indiceArray[i++] = val;
        }
        mergedMesh.setIndices(indiceArray);

        return mergedMesh;
    }

    private static void reMappingTexture(Mesh mesh, float[] vertices, List<TextureRegion> textures) {
        if (textures == null || textures.isEmpty()) return;

        VertexAttribute uvAttribute = mesh.getVertexAttribute(VertexAttributes.Usage.TextureCoordinates);
        int vertexSize = mesh.getVertexSize() / 4; // divide to convert bytes to floats
        int length = vertexSize * mesh.getNumVertices();

        for (int i = uvAttribute.offset / 4, corner = 0, side = 0; i < length; i += vertexSize) {
            if (corner == 0) {
                vertices[i] = textures.get(side).getU();
                vertices[i + 1] = textures.get(side).getV();
            } else if (corner == 1) {
                vertices[i] = textures.get(side).getU();
                vertices[i + 1] = textures.get(side).getV2();
            } else if (corner == 2) {
                vertices[i] = textures.get(side).getU2();
                vertices[i + 1] = textures.get(side).getV2();
            } else if (corner == 3) {
                vertices[i] = textures.get(side).getU2();
                vertices[i + 1] = textures.get(side).getV();
                corner = 0;
                side++;
                if (side >= textures.size()) {
                    side = 0;
                }
                continue;
            }
            corner++;
        }
    }
}
