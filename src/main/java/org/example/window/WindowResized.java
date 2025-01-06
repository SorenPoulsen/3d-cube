package org.example.window;

/**
 * @author Søren Thalbitzer Poulsen
 */
@FunctionalInterface
public interface WindowResized {
    void resized(int width, int height);
}
