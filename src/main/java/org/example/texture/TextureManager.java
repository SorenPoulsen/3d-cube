package org.example.texture;

import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL30C.*;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;
import static org.lwjgl.system.MemoryUtil.memSlice;

/**
 * @author Søren Thalbitzer Poulsen
 */
public class TextureManager implements AutoCloseable {

    private final List<Integer> textureIds = new ArrayList<>();

    /**
     * Create OpenGL texture from raw RGBA image buffer.
     *
     * @param RBGAImage RGBA image buffer
     * @return  Texture ID.
     */
    public int createTexture(RBGAImage RBGAImage) {
        int textureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureId);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        // upload texture
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, RBGAImage.width(), RBGAImage.height(), 0,
                GL_RGBA, GL_UNSIGNED_BYTE, RBGAImage.imageRGBABuffer());
        glGenerateMipmap(GL_TEXTURE_2D);
        textureIds.add(textureId);
        return textureId;
    }

    @Override
    public void close() {
        for (Integer textureId : textureIds) {
            glDeleteTextures(textureId);
        }
    }

    /**
     * Decode image into raw RGBA buffer. Supports jpg, png etc.
     *
     * @param buffer    NIO direct ByteBuffer with encoded image.
     * @return  Decoded RGBA image.
     */
    public RBGAImage decodeImageToRGBA(ByteBuffer buffer) {
        RBGAImage RBGAImage;
        int desiredChannels = 4;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);
            RBGAImage = new RBGAImage(stbi_load_from_memory(buffer, w, h, channels, desiredChannels),
                    w.get(0), h.get(0), channels.get(0), desiredChannels);
        }
        if (RBGAImage.imageRGBABuffer() == null) {
            throw new RuntimeException("Failed to decode image");
        }
        return RBGAImage;
    }

    /**
     * Read image from resource path into NIO direct ByteBuffer.
     *
     * @param resourcePath  Resource path of jpg or png image.
     * @param initialCapacity   Initial capacity of NIO direct ByteBuffer.
     * @return  NIO direct ByteBuffer.
     */
    public ByteBuffer readImageResource(String resourcePath, int initialCapacity) {
        ByteBuffer buffer;
        try (InputStream stream = getClass().getResourceAsStream(resourcePath)) {
            if (stream == null) {
                throw new RuntimeException("Image not found at resource path " + resourcePath);
            }
            ReadableByteChannel channel = Channels.newChannel(stream);
            buffer = BufferUtils.createByteBuffer(initialCapacity); // allocate direct bytebuffer in native byte order.
            while (channel.read(buffer) != -1) {
                if (buffer.remaining() == 0) {
                    buffer = resizeBuffer(buffer, buffer.capacity() * 3 / 2);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load resource " + resourcePath, e);
        }
        buffer.flip(); // Set limit to current position and position to 0.
        return memSlice(buffer); // Create new bytebuffer with shared subsequence from position of buffer and its remaining bytes.
    }

    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
        buffer.flip();  // Set limit to current position and set position to 0.
        newBuffer.put(buffer); // Bulk transfer remaining content of old buffer to new buffer. Set position af new buffer to
        // the end of the transferred data.
        return newBuffer;
    }

    public record RBGAImage(ByteBuffer imageRGBABuffer, int width, int height, int srcChannels, int bufferChannels) {
    }
}
