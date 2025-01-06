package org.example.model;

import org.example.texture.TextureManager;
import org.lwjgl.opengl.GL30C;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL30C.*;

/**
 * Create and manage model buffers and textures. Buffers and textures are deleted when close() is invoked.
 *
 * @author Søren Thalbitzer Poulsen
 */
public class ModelManager implements AutoCloseable {

    private final ArrayList<ModelBinding> modelBindings = new ArrayList<>();

    /**
     * Create OpenGL buffers and textures for model.
     *
     * @param model
     * @return
     */
    public ModelBinding createModelBinding(Model model, TextureManager textureManager) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            int numVertices = model.indices().length;
            List<Integer> vboIdList = new ArrayList<>();

            int vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            // Vertices buffer
            int vboId = glGenBuffers();
            vboIdList.add(vboId);
            FloatBuffer positionsBuffer = stack.callocFloat(model.vertices().length);
            positionsBuffer.put(0, model.vertices());
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, positionsBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            // Texture coordinates VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            FloatBuffer textCoordsBuffer = stack.callocFloat(model.textureCoords().length);
            textCoordsBuffer.put(0, model.textureCoords());
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(1);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

            // Indices buffer
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            IntBuffer indicesBuffer = stack.callocInt(model.indices().length);
            indicesBuffer.put(0, model.indices());
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            // Create model texture
            TextureManager.RBGAImage RBGAImage = textureManager.decodeImageToRGBA(
                    textureManager.readImageResource(model.textureSource().resourcePath(), 150000));
            int textureId = textureManager.createTexture(RBGAImage);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);

            ModelBinding modelBinding = new ModelBinding(model, numVertices, vaoId, vboIdList, textureId);
            modelBindings.add(modelBinding);
            return modelBinding;
        }
    }

    public ArrayList<ModelBinding> getModelBindings() {
        return modelBindings;
    }

    @Override
    public void close() {
        for (ModelBinding modelBinding : modelBindings) {
            modelBinding.vboIds().forEach(GL30C::glDeleteBuffers);
            glDeleteVertexArrays(modelBinding.vaoId());
        }
        modelBindings.clear();
    }
}
