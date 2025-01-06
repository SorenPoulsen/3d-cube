package org.example.model;

import org.example.texture.TextureSource;

/**
 * @author Søren Thalbitzer Poulsen
 */
public record Model(float[] vertices, float[] textureCoords, int[] indices, TextureSource textureSource) {}
