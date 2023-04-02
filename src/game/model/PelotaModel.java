package game.model;

import game.controller.ControllerP2P;

import java.util.Random;

public class PelotaModel {


    private Coordinates velocidad;
    //private int velocidadX;
    //private int velocidadY;
    private Coordinates positionActual;
    private Coordinates positionUpdated;
    private Random random;

    private ObjectTypes objectTypes = ObjectTypes.PELOTA;

    private boolean pelotaCruza = false;
    private boolean upWall, leftWall, rightWall, downWall;
    private ControllerP2P controllerP2P;

    public PelotaModel(ControllerP2P controllerP2P) {
        this.controllerP2P = controllerP2P;

        for (String wall : controllerP2P.getWalls()) {
            switch (wall) {
                case "l" -> this.leftWall = true;
                case "r" -> this.rightWall = true;
                case "d" -> this.downWall = true;
                case "u" -> this.upWall = true;
            }
        }

        velocidad = new Coordinates();
        positionUpdated = new Coordinates();
        positionActual = new Coordinates();

        random = new Random();
        //while ((velocidadX = random.nextInt(-3, 4)) == 0) ;
        //while ((velocidadY = random.nextInt(-3, 4)) == 0) ;

        while ((velocidad.x = random.nextInt(-3, 4)) == 0) ;
        while ((velocidad.y = random.nextInt(-3, 4)) == 0) ;
    }

    public void moverPelota() {
        positionActual = getPositionActual();
        if (positionActual.x >= (controllerP2P.getFrame().getWidth() - 20) && rightWall) {
            //velocidadX = random.nextInt(-3, 0);
            velocidad.x = random.nextInt(-3, 0);
        } else if (positionActual.x <= 0 && leftWall) {
            //velocidadX = random.nextInt(1, 4);
            velocidad.x = random.nextInt(1, 4);
        }
        if (positionActual.y >= (controllerP2P.getFrame().getHeight() - 20) && downWall) {
            //velocidadY = random.nextInt(-3, 0);
            velocidad.y = random.nextInt(-3, 0);
        } else if (positionActual.y <= 0 && upWall) {
            //velocidadY = random.nextInt(1, 4);
            velocidad.y = random.nextInt(1, 4);
        }

        if (!pelotaCruza) {
            checkPelota();
        } else {
            if (positionActual.x <= -20) {
                controllerP2P.setPelota(false);
                controllerP2P.sendMessage("l", objectTypes.name());
            } else if (positionActual.x >= (controllerP2P.getFrame().getWidth() + 20)) {
                controllerP2P.setPelota(false);
                controllerP2P.sendMessage("r", objectTypes.name());
            } else if (positionActual.y <= -20) {
                controllerP2P.setPelota(false);
                controllerP2P.sendMessage("u", objectTypes.name());
            } else if (positionActual.x >= (controllerP2P.getFrame().getHeight() + 20)) {
                controllerP2P.setPelota(false);
                controllerP2P.sendMessage("d", objectTypes.name());
            }
        }


        //positionUpdated.x = positionActual.x + velocidadX;
        //positionUpdated.y = positionActual.y + velocidadY;

        positionUpdated.x = positionActual.x + velocidad.x;
        positionUpdated.y = positionActual.y + velocidad.y;
        setPosicionActual(positionUpdated);
    }

    private void checkPelota() {
        if (positionActual.x <= 0 || positionActual.x >= controllerP2P.getFrame().getWidth() ||
                positionActual.y <= 0 || positionActual.y >= controllerP2P.getFrame().getHeight()) {
            // La pelota ha tocado el vorde de la pantalla, hacer algo aqui
            pelotaCruza = true;
        }
    }


    public synchronized Coordinates getPositionActual() {
        return positionActual;
    }

    public synchronized Coordinates getVelocidad(){
        return velocidad;
    }

    protected synchronized void setPosicionActual(Coordinates position) {
        this.positionActual = position;
    }

}
