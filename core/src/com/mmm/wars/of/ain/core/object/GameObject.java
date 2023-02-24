package com.mmm.wars.of.ain.core.object;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.collision.Ray;

public interface GameObject {

    /**
     * Checks if object is visible by provided Camera.
     *
     * @param camera - Camera object to check if sees the object.
     *
     * @return true if object is visible and false otherwise.
     *
     */
    boolean isVisibleByCamera(Camera camera);

    /**
     * Checks if object intersects with a Ray.
     *
     * @param ray - the Ray object to check the intersection.
     *
     * @return -1 on no intersection, or when there is an intersection: the squared distance between the center of this
     * object and the point on the ray closest to this object when there is intersection.
     *
     * */
    float intersectsWithRay(Ray ray);
}
