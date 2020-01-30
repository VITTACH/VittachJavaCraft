package com.vittach.jumpjack.mesh;

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
        if (index < 0 || index >= meshes.size()) {
            return true;
        }
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
            int index = i - (floorSize * (i / floorSize)) - 1;
            result = index % chunkLength == 0 || isEmptyAtIndex(meshes, i + 1); // front
        } else if (j >= sideLength && j < 2 * sideLength) {
            int index = i - (floorSize * (i / floorSize));
            result = index % chunkLength == 0 || isEmptyAtIndex(meshes, i - 1); // back
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
        Map<String, List<TextureRegion>> regions,
        List<Matrix4> translates,
        Integer chunkLength
    ) {
        if (meshes == null || meshes.isEmpty()) {
            return null;
        }

        final List<Float> vertexList = new ArrayList<Float>();
        final List<Short> indiceList = new ArrayList<Short>();

        int destOffset = 0;
        int indexOffset = 0;
        int vertexOffset = 0;

        for (int i = 0; i < meshes.size(); i++) {
            if ("".equals(meshes.get(i).getSymbol())) {
                continue;
            }
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

            // Experimental culling invisible surface
            int sideLength = 4 * vertexSize;
            for (int j = 0, k = 0; j < length; j++) {
                if (hasSurface(meshes, i, j, sideLength, chunkLength)) {
                    vertexList.add(destOffset + k, vertices[j]);
                    k++;
                } else if (j % vertexSize == 0) {
                    numberVertices--;
                }
            }

            length = numberIndices;
            sideLength = numberIndices / 6;
            for (int j = 0, k = 0; j < length; j++) {
                if (hasSurface(meshes, i, j, sideLength, chunkLength)) {
                    indices[j] += vertexOffset;
                    indiceList.add(indexOffset + k, indices[j]);
                    k++;
                } else {
                    numberIndices--;
                }
            }

            remapTexture(mesh, regions.get(meshes.get(i).getSymbol()));

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
        float[] vertices = new float[vertexList.size()];
        for (float val : vertexList) vertices[i++] = val;
        mergedMesh.setVertices(vertices);

        i = 0;
        short[] indices = new short[indiceList.size()];
        for (short val : indiceList) indices[i++] = val;
        mergedMesh.setIndices(indices);

        return mergedMesh;
    }

    private static void remapTexture(Mesh mergeMesh, List<TextureRegion> regions) {
        if (regions == null || regions.isEmpty()) return;
        int texture = mergeMesh.getVertexAttributes().getOffset(VertexAttributes.Usage.TextureCoordinates);
        int vertexSize = mergeMesh.getVertexSize() / 4; // divide to convert bytes to floats
        int length = vertexSize * mergeMesh.getNumVertices();
        float[] vertices = new float[length];

        mergeMesh.getVertices(vertices);
        for (int i = texture, corner = 0, sides = 0; i < length; i += vertexSize) {
            if (corner == 0) {
                vertices[i] = regions.get(sides).getU();
                vertices[i + 1] = regions.get(sides).getV();
            } else if (corner == 1) {
                vertices[i] = regions.get(sides).getU();
                vertices[i + 1] = regions.get(sides).getV2();
            } else if (corner == 2) {
                vertices[i] = regions.get(sides).getU2();
                vertices[i + 1] = regions.get(sides).getV2();
            } else if (corner == 3) {
                vertices[i] = regions.get(sides).getU2();
                vertices[i + 1] = regions.get(sides).getV();
                corner = 0;
                sides++;
                if (sides >= regions.size()) {
                    sides = 0;
                }
                continue;
            }
            corner++;
        }

        mergeMesh.setVertices(vertices);
    }
}
