package game.controller;

import communications.controller.ClientP2P;
import communications.controller.MyP2P;
import game.model.PelotaModel;
import game.view.PelotaView;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

public class ControllerP2P implements ClientP2P, Runnable {

    private static final String CONNECTED = "CONNECTED";
    private static final String DISCONNECTED = "DISCONNECTED";

    private boolean isPelota = false;

    private String[] walls;

    private JFrame frame;

    private PelotaModel pelotaModel;

    private PelotaView pelotaView;

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

        pelotaModel = new PelotaModel(this);
        pelotaView = new PelotaView(pelotaModel);


        frame = new JFrame("Pelota rebotando");
        frame.add(pelotaView);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        frame.pack();
        frame.setVisible(true);


    }

    public void setPelota(boolean isPelota) {
        this.isPelota = isPelota;
    }

    private boolean getPelota(){
        return isPelota;
    }


    public JFrame getFrame() {
        return frame;
    }


    public boolean isPelotaIn() {
        return isPelota;
    }

    public void setWalls(String[] walls) {
        this.walls = walls;
    }

    public String[] getWalls() {
        return walls;
    }

    public void sendMessage(String direction, String type) {
        String dir = null;
        switch (direction) {
            case "l" -> dir = myP2Pcontroller.getProperties().getProperty("ml");
            case "r" -> dir = myP2Pcontroller.getProperties().getProperty("mr");
            case "d" -> dir = myP2Pcontroller.getProperties().getProperty("dc");
            case "u" -> dir = myP2Pcontroller.getProperties().getProperty("uc");
        }

        String [] data = {type, String.valueOf(pelotaModel.getPositionActual().x),
                String.valueOf(pelotaModel.getPositionActual().y),
                String.valueOf(pelotaModel.getVelocidad().x),
                String.valueOf(pelotaModel.getVelocidad().x)};

        myP2Pcontroller.sendMessage(dir, Arrays.toString(data));
    }

    @Override
    public void run() {

        while (true) {
            if (isPelota) {
                try {
                    pelotaModel.moverPelota();
                    pelotaView.repaint();
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                    // handle exception
                }
            }
        }
    }


    @Override
    public void addConnection(String ip) {

    }

    @Override
    public void pushMessage(String ip, String message) {

    }
}
