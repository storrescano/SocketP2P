package game.controller;

import game.model.AnimatedObject;
import game.model.AnimationModel;

public class AnimationController {
    private final AnimationModel model;

    public AnimationController(AnimationModel model) {
        this.model = model;
    }

    public AnimatedObject[] getObjects() {
        return model.getObjects();
    }

}
