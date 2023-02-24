package com.mmm.wars.of.ain.core.object;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.mmm.wars.of.ain.core.object.shape.Shape;

public abstract class AbstractGameObject extends ModelInstance implements GameObject {

    private Shape shape;

    public AbstractGameObject(Model model, String nodeId) {
        super(model, nodeId, true);

        BoundingBox boundingBox = new BoundingBox();
        this.calculateBoundingBox(boundingBox);
        shape = getShape(boundingBox);
    }

    public abstract Shape getShape(BoundingBox boundingBox);

    public boolean isVisibleByCamera(Camera camera) {
        return shape != null && shape.isVisible(transform, camera);
    }

    public float intersectsWithRay(Ray ray) {
        return shape == null ? -1f : shape.intersects(transform, ray);
    }
}
