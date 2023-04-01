package game.view;

import game.controller.ControllerP2P;
import game.model.PelotaModel;

import javax.swing.*;
import java.awt.*;

public class PelotaView extends JPanel {

    private PelotaModel pelotaModel;

    private boolean isPelota;

    private ControllerP2P controllerP2P;



    public PelotaView(PelotaModel pelotaModel, ControllerP2P controllerP2P) {
        this.controllerP2P = controllerP2P;
        this.pelotaModel = pelotaModel;


    }

    public void setPelotaModel(PelotaModel pelotaModel) {
        this.pelotaModel = pelotaModel;
    }

    public void setIsPelota(boolean isPelota) {
        this.isPelota = isPelota;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the ball at the current position
        g.fillOval(pelotaModel.getPositionActual().x, pelotaModel.getPositionActual().y, 20, 20);
    }


}
