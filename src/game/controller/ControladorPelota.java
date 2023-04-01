package game.controller;

import game.model.Coordinates;
import game.model.PelotaModel;
import game.view.PelotaView;

import java.util.Random;

public class ControladorPelota implements Runnable {
    private PelotaModel pelotaModel;
    private PelotaView pelotaView;

    public ControladorPelota(PelotaModel pelotaModel, PelotaView pelotaView, ControllerP2P controllerP2P) {
        this.pelotaModel = pelotaModel;
        this.pelotaView = pelotaView;
    }


    @Override
    public void run() {
        while (true) {
            try {
                pelotaModel.moverPelota();
                pelotaView.repaint();
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                // handle exception
            }
        }
    }
}
