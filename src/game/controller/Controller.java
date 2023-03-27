package game.controller;

import communications.connections.ConnectionController;
import communications.controller.ClientP2P;
import communications.controller.IpUtilities;
import communications.controller.MyP2P;
import game.view.GameFrame;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Controller implements ClientP2P {

    private static final String CONNECTED = "CONNECTED";
    private static final String DISCONNECTED = "DISCONNECTED";

    private MyP2P myP2Pcontroller;


    public Controller(){
        Properties game = new Properties();
        try {
            // Afegir ip de peers a la llista
            game.load(new FileInputStream("game.properties"));
            boolean isPelota = Boolean.parseBoolean(game.getProperty("pelota"));

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        GameFrame gameFrame = new GameFrame();
        gameFrame.setController(this);

        new Thread(gameFrame).start();
    }

    public void setController(MyP2P controller){

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
