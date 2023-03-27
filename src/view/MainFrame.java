package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import communications.controller.ClientP2P;
import communications.controller.IpUtilities;
import communications.controller.MyP2P;

public class MainFrame extends JFrame implements WindowListener, ClientP2P {

    private static final long serialVersionUID = 8456560429229699542L;

    private static final String CONNECTED = "CONNECTED";
    private static final String DISCONNECTED = "DISCONNECTED";

    private JTable connectionsTable;
    private JTextArea areaChat;
    private JTextField areaMensaje;
    private JButton btnSendMessage;
    private MyP2P controller;

    private boolean runState;

    public MainFrame() {
        setTitle("Chat P2P");
        setPreferredSize(new Dimension(800, 600));
        setLayout(new BorderLayout());
        addWindowListener(this);
        runState = true;
        connectionsTable = new JTable(new DefaultTableModel(new String[]{"IP", "STATUS"}, 0));
        areaChat = new JTextArea();
        areaMensaje = new JTextField();
        initializeComponents();
        pack();
        setVisible(true);
    }

    @Override
    public void addConnection(String ip) {
        DefaultTableModel model = (DefaultTableModel) connectionsTable.getModel();
        if (ip != null && IpUtilities.isValidIp(ip)) {
            Object[] rowData = {ip, controller.getConnectionStatus(ip) ? CONNECTED : DISCONNECTED};
            model.addRow(rowData);
        }
    }

    public void setController(MyP2P controller) {
        this.controller = controller;
        new Thread(new Runnable() {
            public void run() {
                pollConnection();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
        }).start();
    }

    private void initializeComponents() {
        initializeAreaReceivedMessages();
        initializeAreaPeers();
        initializeAreaSendMessages();
    }

    private void initializeAreaSendMessages() {
        JPanel area = new JPanel();
        area.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;

        areaMensaje.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) { /* not used */ }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnSendMessage.doClick();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) { /* not used */ }
        });
        area.add(areaMensaje, c);

        btnSendMessage = new JButton("Enviar");
        btnSendMessage.addActionListener((ev) -> {
            String mensaje = areaMensaje.getText();
            areaMensaje.setText("");
            areaMensaje.requestFocus();
            sendMessage(mensaje);
        });
        c.weightx = 0;
        area.add(btnSendMessage, c);

        add(area, BorderLayout.SOUTH);
    }

    private void initializeAreaPeers() {
        JPanel area = new JPanel();
        area.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridy = 0;
        area.add(new JLabel("Peers"), c);
        c.gridy++;
        area.add(new JScrollPane(connectionsTable), c);
        add(area, BorderLayout.EAST);

    }

    private void initializeAreaReceivedMessages() {
        areaChat.setLineWrap(true);
        JScrollPane scrollChat = new JScrollPane(areaChat);
        scrollChat.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        areaChat.setEditable(false);
        this.add(scrollChat, BorderLayout.CENTER);
    }

    private void pollConnection() {
        DefaultTableModel tm = (DefaultTableModel) connectionsTable.getModel();
        while (runState) {
            boolean validConnections = false;

            for (int i = 0; i < tm.getRowCount(); ++i) {
                String ip = (String) tm.getValueAt(i, 0);
                if (IpUtilities.isValidIp(ip)) {
                    if (controller.getConnectionStatus(ip)) {
                        validConnections = true;
                        tm.setValueAt("CONNECTED", i, 1);
                    } else {
                        tm.setValueAt("DISCONNECTED", i, 1);
                    }
                } else {
                    tm.setValueAt("", i, 1);
                }
            }
            if (validConnections) {
                btnSendMessage.setEnabled(true);
                areaMensaje.setEnabled(true);
            } else {
                btnSendMessage.setEnabled(false);
                areaMensaje.setEnabled(false);
            }
        }
    }

    private void sendMessage(String message) {
        areaChat.append("SEND: " + message + "\n");
        controller.sendMessage(null, message);
    }

    @Override
    public void pushMessage(String ip, String message) {
        areaChat.append(ip + ": " + message + "\n");
    }

    // ------------------------------ WindowEvent Handler ------------------------------------//
    @Override
    public void windowOpened(WindowEvent e) { /* Not used */ }

    @Override
    public void windowClosing(WindowEvent e) {
        runState = false;
        setVisible(false);
        controller.stopAndQuit();
    }

    @Override
    public void windowClosed(WindowEvent e) { /* Not used */ }

    @Override
    public void windowIconified(WindowEvent e) { /* Not used */ }

    @Override
    public void windowDeiconified(WindowEvent e) { /* Not used */ }

    @Override
    public void windowActivated(WindowEvent e) { /* Not used */ }

    @Override
    public void windowDeactivated(WindowEvent e) { /* Not used */ }

}
