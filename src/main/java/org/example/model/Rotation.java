package org.example.model;

/**
 * @author Søren Thalbitzer Poulsen
 */
public class Rotation {

    private float angleRadians, rotXAxis, rotYAxis, rotZAxis;

    public Rotation(float angleRadians, float rotXAxis, float rotYAxis, float rotZAxis) {
        this.angleRadians = angleRadians;
        this.rotXAxis = rotXAxis;
        this.rotYAxis = rotYAxis;
        this.rotZAxis = rotZAxis;
    }

    public float getAngleRadians() {
        return angleRadians;
    }

    public void setAngleRadians(float angleRadians) {
        this.angleRadians = angleRadians;
    }

    public float getRotXAxis() {
        return rotXAxis;
    }

    public void setRotXAxis(float rotXAxis) {
        this.rotXAxis = rotXAxis;
    }

    public float getRotYAxis() {
        return rotYAxis;
    }

    public void setRotYAxis(float rotYAxis) {
        this.rotYAxis = rotYAxis;
    }

    public float getRotZAxis() {
        return rotZAxis;
    }

    public void setRotZAxis(float rotZAxis) {
        this.rotZAxis = rotZAxis;
    }
}