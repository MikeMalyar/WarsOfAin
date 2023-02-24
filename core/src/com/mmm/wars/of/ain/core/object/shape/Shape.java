package com.mmm.wars.of.ain.core.object.shape;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.collision.Ray;

public interface Shape {

    /**
     * Checks if shape is visible by provided Camera.
     *
     * @param cam - Camera object to check if sees the object.
     *
     * @return true if object is visible and false otherwise.
     *
     */
    boolean isVisible(Matrix4 transform, Camera cam);

    /**
     * Checks if shape intersects with a Ray.
     *
     * @param ray - the Ray object to check the intersection.
     *
     * @return -1 on no intersection, or when there is an intersection: the squared distance between the center of this
     * shape and the point on the ray closest to this shape when there is intersection.
     *
     * */
    float intersects(Matrix4 transform, Ray ray);
}
