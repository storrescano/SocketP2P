package main;

import communications.controller.MyP2P;
import game.controller.ControllerP2P;
import game.model.PelotaModel;
import game.view.PelotaView;

public class Main {

    public static void main(String[] args) {
        MyP2P controller = new MyP2P();
        ControllerP2P juego = new ControllerP2P(controller);
        controller.setClient(juego);
        new Thread(juego).start();


    }
}
