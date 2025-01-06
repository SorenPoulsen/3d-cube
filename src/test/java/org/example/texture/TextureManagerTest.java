package org.example.texture;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Søren Thalbitzer Poulsen
 */
public class TextureManagerTest {

    @Test
    public void testReadAndDecode() {
        TextureManager.RBGAImage RBGAImage;
        try (TextureManager textureManager = new TextureManager()) {
            ByteBuffer read = textureManager.readImageResource("/image/cat.jpg", 100000);
            assertNotNull(read);
            RBGAImage = textureManager.decodeImageToRGBA(read);
        }
        assertNotNull(RBGAImage);
        assertTrue(RBGAImage.width() > 1);
    }
}
