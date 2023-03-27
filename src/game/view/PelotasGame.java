package game.view;

//import game.model.VisualObject;

import java.awt.*;
import java.util.ArrayList;

public class PelotasGame extends Canvas implements Runnable {


    //private ArrayList<VisualObject> animatedObjectList;
    private Image canvas;
    private Graphics canvasGraphics;

    public PelotasGame() {
        setBackground(Color.black);
    }

    public void initializeComponents(){

    }

    @Override
    public void run() {

    }

    public void refresh(){

    }


    /*public void paintObjects(ArrayList<VisualObject> animatedObjectList) {
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
    }*/
}
