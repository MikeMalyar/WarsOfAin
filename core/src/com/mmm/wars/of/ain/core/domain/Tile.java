package com.mmm.wars.of.ain.core.domain;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.mmm.wars.of.ain.core.configuration.annotation.Injected;
import com.mmm.wars.of.ain.core.configuration.annotation.ModelObject;
import com.mmm.wars.of.ain.core.object.AbstractGameObject;
import com.mmm.wars.of.ain.core.object.shape.BoxShape;
import com.mmm.wars.of.ain.core.object.shape.Shape;

@ModelObject(modelPath = "tile")
public class Tile extends AbstractGameObject {

    @Injected
    public Tile(Model model, String nodeId) {
        super(model, nodeId);
    }

    @Override
    public Shape getShape(BoundingBox boundingBox) {
        return new BoxShape(boundingBox);
    }
}
