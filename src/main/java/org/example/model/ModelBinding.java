package org.example.model;

import java.util.List;

/**
 * @author Søren Thalbitzer Poulsen
 */
public record ModelBinding(Model model, int numVertices, int vaoId, List<Integer> vboIds, int textureId) {
}
