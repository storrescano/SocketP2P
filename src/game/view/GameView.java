package game.view;

import communications.controller.ClientP2P;
import game.controller.AnimatedObjectStatus;
import game.controller.AnimationController;
import game.controller.ControllerP2P;
import game.model.AnimatedObject;
import game.model.VisualObject;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GameView extends JFrame implements ClientP2P, Runnable {

	private static final long serialVersionUID = 8456560429229699542L;

	private AnimationController controller;
	public int refreshMilis;

	private PelotasGame pelotasGame;

	private boolean runState;

	public GameView(){
		setTitle("Pelotas rebotando");
		setLayout(new BorderLayout());
		setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setUndecorated(true);

		new Thread(() -> {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			log("Altura: " + getHeight() + ", Ancho: " + getWidth());

		}).start();


		pelotasGame = new PelotasGame();
		runState = true;
		pack();
		setVisible(true);

	}

	public void setController(AnimationController controller){
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
			for(AnimatedObject o: controller.getObjects()) {
				if(o.getStatus() != AnimatedObjectStatus.dead)
					list.add(o);
			}
			refresh(list);
			try {
				Thread.sleep(refreshMilis);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void setRefreshMilis(int refreshMilis) {
		this.refreshMilis = refreshMilis;
	}
}
