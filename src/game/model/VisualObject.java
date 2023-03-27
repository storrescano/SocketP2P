package game.model;

import game.controller.ObjectTypes;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class VisualObject {
    private Coordinates position;
    private static Image[] objectImages;
    private ObjectTypes objectType;

    public static void loadObjectImages() {

    }

    public synchronized void drawObject(Graphics graphics) {
        Coordinates coord = getPosition();
        graphics.drawImage(objectImages[objectType.ordinal()], coord.x, coord.y, null);
    }

    public synchronized Coordinates getPosition() {
        return position;
    }

    protected synchronized void setPosition(Coordinates position) {
        this.position = position;
    }

    public ObjectTypes getObjectType() {
        return objectType;
    }

    protected void setObjectType(ObjectTypes type) {
        objectType = type;
    }
}
