package game.view;

//import game.model.VisualObject;

import game.model.VisualObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PelotasGame extends Canvas {


    private ArrayList<VisualObject> animatedObjectList;
    private Image canvas;
    private Graphics canvasGraphics;
    private Image background;

    public PelotasGame() {
        try {
            background = ImageIO.read(new File("Imagenes/Fondo.jpeg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        animatedObjectList = new ArrayList<>();
        canvas = new BufferedImage(1024, 800, BufferedImage.TYPE_3BYTE_BGR);
        canvasGraphics = canvas.getGraphics();

    }



    public void paintObjects(ArrayList<VisualObject> animatedObjectList) {
        this.animatedObjectList = animatedObjectList;
        repaint();
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }

    @Override
    public void paint(Graphics g) {
        canvasGraphics.clearRect(0, 0, canvas.getWidth(null), canvas.getHeight(null));
        canvasGraphics.drawImage(background, 0, 0, null);

        for(VisualObject object: animatedObjectList) {
            object.drawObject(canvasGraphics);
        }
        g.drawImage(canvas, 0, 0, null);
    }
}
