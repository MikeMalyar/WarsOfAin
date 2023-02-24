package com.mmm.wars.of.ain.core.object.shape;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public abstract class BaseShape implements Shape {

    protected static final Vector3 position = new Vector3();
    private final Vector3 center = new Vector3();
    private final Vector3 dimensions = new Vector3();

    public BaseShape(BoundingBox bounds) {
        bounds.getCenter(center);
        bounds.getDimensions(dimensions);
    }

    public Vector3 getCenter() {
        return center;
    }

    public Vector3 getDimensions() {
        return dimensions;
    }
}
