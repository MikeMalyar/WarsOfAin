package com.mmm.wars.of.ain.core.domain.model;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.mmm.wars.of.ain.core.configuration.annotation.model.ModelObjectConstructor;
import com.mmm.wars.of.ain.core.configuration.annotation.model.ModelObject;
import com.mmm.wars.of.ain.core.configuration.annotation.model.NodeId;
import com.mmm.wars.of.ain.core.domain.model.type.ModelType;
import com.mmm.wars.of.ain.core.object.AbstractGameObject;
import com.mmm.wars.of.ain.core.object.shape.BoxShape;
import com.mmm.wars.of.ain.core.object.shape.Shape;

@ModelObject(modelType = ModelType.TILE, modelPath = "model/tile/tile.g3dj")
public class Tile extends AbstractGameObject {

    @ModelObjectConstructor
    public Tile(Model model, @NodeId("Tile") String nodeId) {
        super(model, nodeId);
    }

    @Override
    public Shape getShape(BoundingBox boundingBox) {
        return new BoxShape(boundingBox);
    }
}
