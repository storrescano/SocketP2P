package game.controller;

import communications.controller.ClientP2P;
import communications.controller.IpUtilities;
import communications.controller.MyP2P;
import game.model.PelotaModel;
import game.view.PelotaView;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ControllerP2P implements ClientP2P {

    private static final String CONNECTED = "CONNECTED";
    private static final String DISCONNECTED = "DISCONNECTED";

    private boolean isPelota = false;

    private String[] walls;

    private JFrame frame;


    private MyP2P myP2Pcontroller;


    public ControllerP2P(MyP2P myP2Pcontroller) {
        this.myP2Pcontroller = myP2Pcontroller;
        Properties gameProperties = new Properties();

        try {
            // Afegir ip de peers a la llista
            gameProperties.load(new FileInputStream("game.properties"));
            setPelota(Boolean.parseBoolean(gameProperties.getProperty("isPelota")));
            setWalls(gameProperties.getProperty("walls").split(","));

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        PelotaModel pelotaModel = new PelotaModel(this);
        PelotaView pelotaView = new PelotaView(pelotaModel, this);
        ControladorPelota controladorPelota = new ControladorPelota(pelotaModel,pelotaView, this);

        frame = new JFrame("Pelota rebotando");
        frame.add(pelotaView);
        frame.setUndecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);

        frame.pack();
        frame.setVisible(true);

        new Thread(controladorPelota).start();


    }

    private void setPelota(boolean isPelota) {
        this.isPelota = isPelota;
    }

    @Override
    public void addConnection(String ip) {
        if (ip != null && IpUtilities.isValidIp(ip)) {
            //System.out.println(myP2Pcontroller.getConnectionStatus(ip));
        }
    }

    public JFrame getFrame(){
        return frame;
    }

    @Override
    public void pushMessage(String ip, String message) {
    }

    public boolean isPelotaIn() {
        return isPelota;
    }

    public void setWalls(String[] walls){
        this.walls = walls;
    }

    public String[] getWalls(){
        return walls;
    }
}
