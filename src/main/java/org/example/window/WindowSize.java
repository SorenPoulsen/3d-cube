package org.example.window;

/**
 * @author Søren Thalbitzer Poulsen
 */
public class WindowSize {

    private int width, height;

    public WindowSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
