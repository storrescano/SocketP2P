package game.view;

import communications.controller.ClientP2P;
import game.controller.Controller;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame implements ClientP2P {

	private static final long serialVersionUID = 8456560429229699542L;

	private Controller controller;

	private PelotasGame pelotasGame;

	private boolean runState;

	public GameFrame(){
		setTitle("Pelotas rebotando");
		setLayout(new BorderLayout());
		setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);

		new Thread(() -> {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			log("Altura: " + getHeight() + ", Ancho: " + getWidth());
		}).start();
		setUndecorated(true);


		pelotasGame = new PelotasGame();
		runState = true;
		pack();
		setVisible(true);
	}

	public void setController(Controller controller){
		this.controller = controller;
	}



	@Override
	public void addConnection(String ip) {

	}

	@Override
	public void pushMessage(String ip, String message) {

	}

	@SuppressWarnings("unused")
	private void log(String message) {
		System.out.println(this.getClass().getSimpleName() + ": " + message);
	}

	@SuppressWarnings("unused")
	private void logError(String message) {
		System.err.println(this.getClass().getSimpleName() + ": " + message);
	}
}
