package game.model;

import game.controller.ControllerP2P;

import java.util.Random;

public class PelotaModel {

    private int velocidadX;
    private int velocidadY;
    private Coordinates positionActual;
    private Coordinates positionUpdated;
    private Random random;
    private boolean upWall, leftWall, rightWall, downWall;

    private ControllerP2P controllerP2P;

    public PelotaModel(ControllerP2P controllerP2P) {
        this.controllerP2P =  controllerP2P;

        for(String wall : controllerP2P.getWalls()){
            switch (wall) {
                case "l" -> this.leftWall = true;
                case "r" -> this.rightWall = true;
                case "d" -> this.downWall = true;
                case "u" -> this.upWall = true;
            }
        }

        positionUpdated = new Coordinates();
        positionActual = new Coordinates();

        random = new Random();
        while ((velocidadX = random.nextInt(-3, 4)) == 0) ;
        while ((velocidadY = random.nextInt(-3, 4)) == 0) ;
    }

    public void moverPelota() {
        positionActual = getPositionActual();
        if (positionActual.x >= (controllerP2P.getFrame().getWidth() - 20) && rightWall) {
            velocidadX = random.nextInt(-3, 0);
        } else if (positionActual.x <= 0 && leftWall) {
            velocidadX = random.nextInt(1, 4);
        }
        if (positionActual.y >= (controllerP2P.getFrame().getHeight() - 20) && downWall) {
            velocidadY = random.nextInt(-3, 0);
        } else if (positionActual.y <= 0 && upWall) {
            velocidadY = random.nextInt(1, 4);
        }

        if ((positionActual.x <= -20) || positionActual.x >= (controllerP2P.getFrame().getWidth() + 20)){

        }

        positionUpdated.x = positionActual.x + velocidadX;
        positionUpdated.y = positionActual.y + velocidadY;
        setPosicionActual(positionUpdated);
    }


    public synchronized Coordinates getPositionActual() {
        return positionActual;
    }

    protected synchronized void setPosicionActual(Coordinates position) {
        this.positionActual = position;
    }
}
