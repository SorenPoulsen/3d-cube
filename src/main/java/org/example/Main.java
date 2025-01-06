package org.example;

import org.example.model.*;
import org.example.render.*;
import org.example.shader.ShaderSource;
import org.example.texture.TextureSource;
import org.example.window.Window;
import org.lwjgl.opengl.GL30C;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        new Main().run();
    }

    private static Scene getScene() {

        float[] vertices = new float[]{
                // Vertices are duplicated when doing texture mapping because each corner has different texture coordinates
                // depending on the side they are part of.

                // back
                -0.5f, 0.5f, -0.5f, // 0
                -0.5f, -0.5f, -0.5f, // 1
                0.5f, -0.5f, -0.5f, // 2
                0.5f, 0.5f, -0.5f, // 3

                // front
                -0.5f, 0.5f, 0.5f, // 4
                -0.5f, -0.5f, 0.5f, // 5
                0.5f, -0.5f, 0.5f, // 6
                0.5f, 0.5f, 0.5f, // 7

                // left side
                -0.5f, 0.5f, -0.5f, // 8 = 0
                -0.5f, 0.5f, 0.5f, // 9 = 4
                -0.5f, -0.5f, 0.5f, // 10 = 5
                -0.5f, -0.5f, -0.5f, // 11 = 1

                // right side
                0.5f, 0.5f, -0.5f, // 12
                0.5f, 0.5f, 0.5f, // 13
                0.5f, -0.5f, 0.5f, // 14
                0.5f, -0.5f, -0.5f, // 15

                // top
                -0.5f, 0.5f, -0.5f, // 16
                -0.5f, 0.5f, 0.5f, // 17
                0.5f, 0.5f, 0.5f, // 18
                0.5f, 0.5f, -0.5f, // 19

                // bottom
                -0.5f, -0.5f, -0.5f, // 20
                -0.5f, -0.5f, 0.5f, // 21
                0.5f, -0.5f, 0.5f, // 22
                0.5f, -0.5f, -0.5f // 23
        };
        float[] textureCoords = new float[]{
                // Map each vertex to a texture coordinate.

                // back
                0.0f, 0.0f, // 0
                0.0f, 1.0f, // 1
                1.0f, 1.0f, // 2
                1.0f, 0.0f, // 3

                // front
                0.0f, 0.0f, // 4
                0.0f, 1.0f, // 5
                1.0f, 1.0f, // 6
                1.0f, 0.0f, // 7

                // left side
                0.0f, 0.0f, // 8
                1.0f, 0.0f, // 9
                1.0f, 1.0f, // 10
                0.0f, 1.0f, // 11

                // right side
                1.0f, 0.0f, // 12
                0.0f, 0.0f, // 13
                0.0f, 1.0f, // 14
                1.0f, 1.0f, // 15

                // top
                0.0f, 0.0f, // 16
                0.0f, 1.0f, // 17
                1.0f, 1.0f, // 18
                1.0f, 0.0f, // 19

                // bottom
                0.0f, 0.0f, // 20
                0.0f, 1.0f, // 21
                1.0f, 1.0f, // 22
                1.0f, 0.0f // 23
        };
        int[] indices = new int[]{
                // Define triangles through indices of vertices.
                0, 1, 3,
                3, 1, 2,
                4, 5, 7,
                5, 6, 7,
                8, 9, 11,
                9, 10, 11,
                12, 13, 15,
                15, 13, 14,
                16, 17, 19,
                17, 18, 19,
                20, 21, 22,
                22, 23, 20

        };

        List<Model> models = List.of(new Model(vertices, textureCoords, indices,
                new TextureSource("/image/cat.jpg")));

        List<ShaderSource> shaderSources = List.of(
                new ShaderSource(GL30C.GL_VERTEX_SHADER, "/shaders/vertex.glsl"),
                new ShaderSource(GL30C.GL_FRAGMENT_SHADER, "/shaders/fragment.glsl"));

        List<RenderAction> renderActions = List.of(
                new TextureSamplerRenderAction(),
                new RotationUniformRenderAction(
                        new Rotation((float) Math.toRadians(50.0f), 0.2f, 1.0f, 0.5f)),
                new ProjectionUniformRenderAction(
                        new Projection((float) Math.toRadians(90.0f), 0.01f, 1000.0f)));

        return new Scene(models, shaderSources, renderActions,
                new Color(0.0f, 0.0f, 0.0f, 0.0f));
    }

    public void run() {
        Window window = new Window("Cube");
        Renderer renderer = new Renderer(window, getScene());
        renderer.loop();
        renderer.close();
        window.close();
    }

}
