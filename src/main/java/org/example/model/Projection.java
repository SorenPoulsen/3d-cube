package org.example.model;

/**
 * @author Søren Thalbitzer Poulsen
 */
public class Projection {

    private float fov, zNear, zFar;

    public Projection(float fov, float zNear, float zFar) {
        this.fov = fov;
        this.zNear = zNear;
        this.zFar = zFar;
    }

    public float getFov() {
        return fov;
    }

    public float getZNear() {
        return zNear;
    }

    public float getZFar() {
        return zFar;
    }
}
