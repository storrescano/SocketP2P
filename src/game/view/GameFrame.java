package game.view;

import communications.controller.ClientP2P;
import game.controller.Controller;
import game.model.VisualObject;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GameFrame extends JFrame implements ClientP2P, Runnable {

	private static final long serialVersionUID = 8456560429229699542L;

	private Controller controller;
	public int refreshMilis;

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

	public void refresh(ArrayList<VisualObject> animatedObjectsList) {
		pelotasGame.paintObjects(animatedObjectsList);
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

	@Override
	public void run() {
		while(true) {
			ArrayList<VisualObject> list = new ArrayList<>();
			refresh(list);
			try {
				Thread.sleep(refreshMilis);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
