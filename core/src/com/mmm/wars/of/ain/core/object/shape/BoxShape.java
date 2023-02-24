package com.mmm.wars.of.ain.core.object.shape;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;

public class BoxShape extends BaseShape {

    public BoxShape(BoundingBox bounds) {
        super(bounds);
    }

    @Override
    public boolean isVisible(Matrix4 transform, Camera cam) {
        return cam.frustum.boundsInFrustum(transform.getTranslation(position).add(getCenter()), getDimensions());
    }

    @Override
    public float intersects(Matrix4 transform, Ray ray) {
        transform.getTranslation(position).add(getCenter());
        if (Intersector.intersectRayBoundsFast(ray, position, getDimensions())) {
            final float len = ray.direction
                    .dot(position.x - ray.origin.x, position.y - ray.origin.y, position.z - ray.origin.z);
            return position.dst2(ray.origin.x + ray.direction.x * len, ray.origin.y + ray.direction.y * len,
                    ray.origin.z + ray.direction.z * len);
        }
        return -1f;
    }
}
