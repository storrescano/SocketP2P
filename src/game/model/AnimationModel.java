package game.model;

import game.controller.AnimatedObjectStatus;
import game.controller.ObjectTypes;

import java.util.ArrayList;
import java.util.Random;

public class AnimationModel implements Runnable {
    private ArrayList<AnimatedObject> animatedObjects;


    public AnimationModel() {
        this.animatedObjects = new ArrayList<>();

        start();
    }

    public synchronized AnimatedObject[] getObjects() {
        return animatedObjects.toArray(new AnimatedObject[0]);
    }

    private void start() {
        AnimatedObject.loadObjectImages();
    }

    public void run() {
        ObjectTypes tipo;
        Random random = new Random();
        Coordinates posicionInicial;
        AnimatedObject animatedObjectTipus;


        while (true) {
            int end = animatedObjects.size();
            for (int i = 0; i < end; ++i) {
                AnimatedObject animatedObject = animatedObjects.get(i);
                if (animatedObject.getStatus() == AnimatedObjectStatus.dead) {
                    animatedObjects.remove(animatedObject);
                    end--;
                }
            }

            // Crear objectos nuevos
            posicionInicial = new Coordinates();
            posicionInicial.x = random.nextInt(1125 - 100);
            posicionInicial.y = random.nextInt(750 - 100);
            //Cambiar para que sea entre diferentes objetos.
            switch (ObjectTypes.PELOTA) {
                //Insertar sus respectivos objetos, pelota, nave, disparo...
                default -> {
                    animatedObjectTipus = new Pelota(this, ObjectTypes.PELOTA, posicionInicial);
                }
            }

            animatedObjects.add(animatedObjectTipus);
            new Thread(animatedObjectTipus).start();
        }

    }
}
