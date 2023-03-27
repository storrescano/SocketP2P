package main;

import communications.controller.MyP2P;
import game.controller.Controller;

public class Main {

	public static void main(String[] args) {
		Controller juego = new Controller();
		MyP2P controller = new MyP2P();
		juego.setController(controller);
		controller.setClient(juego);
	}
}
