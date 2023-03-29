package game.controller;

import communications.controller.ClientP2P;
import communications.controller.IpUtilities;
import communications.controller.MyP2P;
import game.model.AnimationModel;
import game.view.GameView;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ControllerP2P implements ClientP2P {

    private static final String CONNECTED = "CONNECTED";
    private static final String DISCONNECTED = "DISCONNECTED";

    private GameView gameView;

    private MyP2P myP2Pcontroller;


    public ControllerP2P(MyP2P myP2Pcontroller) {
        this.myP2Pcontroller = myP2Pcontroller;
        Properties gameProperties = new Properties();
        try {
            // Afegir ip de peers a la llista
            gameProperties.load(new FileInputStream("game.properties"));

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        AnimationModel model = new AnimationModel();
        AnimationController inicio = new AnimationController(model);

        GameView game = new GameView();
        game.setController(inicio);
        game.setRefreshMilis(20);


        new Thread(model).start();
        new Thread(game).start();


    }

    @Override
    public void addConnection(String ip) {
        if (ip != null && IpUtilities.isValidIp(ip)) {
            //System.out.println(myP2Pcontroller.getConnectionStatus(ip));
        }
    }

    @Override
    public void pushMessage(String ip, String message) {

    }
}
