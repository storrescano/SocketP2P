package game.view;

import game.controller.ControllerP2P;
import game.model.PelotaModel;

import javax.swing.*;
import java.awt.*;

public class PelotaView extends JPanel {

    private PelotaModel pelotaModel;



    public PelotaView(PelotaModel pelotaModel) {
        this.pelotaModel = pelotaModel;


    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Dibuja la pelota en la posicion actual
        g.fillOval(pelotaModel.getPositionActual().x, pelotaModel.getPositionActual().y, 20, 20);
    }


}
