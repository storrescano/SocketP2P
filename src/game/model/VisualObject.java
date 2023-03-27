package game.model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/*public class VisualObject {
    private Coordinates position;
    private static Image[] objectImages;
    private ObjectTypes objectType;

    public static void loadObjectImages() {
        if(objectImages == null) {
            objectImages = new Image[4];
            String[] uris = {"Imagenes/Zombie.png", "Imagenes/Alien.png", "Imagenes/Soldier.png", "Imagenes/Dog.png" };
            try {
                for(int i=0; i<4; ++i) {
                    objectImages[i] = ImageIO.read(new File(uris[i]));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
}*/
