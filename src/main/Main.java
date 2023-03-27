package main;

import communications.controller.MyP2P;
import game.controller.Controller;

public class Main {

	public static void main(String[] args) {
		MyP2P controller = new MyP2P();
		Controller juego = new Controller();
		juego.setController(controller);
		controller.setClient(juego);
	}
}
