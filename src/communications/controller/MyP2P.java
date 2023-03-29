package communications.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import communications.connections.ConnectionController;

public class MyP2P {
	private ClientP2P client;
	private ConnectionController connectionController;
	
	public MyP2P() {
		Properties properties = new Properties();
		try {
			// Afegir ip de peers a la llista
			properties.load(new FileInputStream(new File("connections.properties")));
			int serverPort = Integer.parseInt(properties.getProperty("server_port"));
			int timeToLive = Integer.parseInt(properties.getProperty("ttl"));
			int timeout = Integer.parseInt(properties.getProperty("timeout"));
			connectionController = new ConnectionController(this, timeToLive, timeout, serverPort);
			for(String peer: new String[]{
					"ip_ul", "ip_uc", "ip_ur", "ip_ml",
					"ip_mr", "ip_dl", "ip_dc", "ip_dr"}) {
				String ip = properties.getProperty(peer);
				if(ip != null && IpUtilities.isValidIp(ip)) {
					connectionController.addEmptyConnection(ip);
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	/**
	 * Guarda en la lista de conexiones el socket de una nueva conexión.
	 * @param socket Socket establecido con la nueva conexión.
	 */
	public void informNewConnection(String ip) {
		if(client != null) {
			client.addConnection(ip);
		}
	}

	/**
	 * Indica el estado con la conexión ip.
	 * @param ip IP de la conexión que se quiere comprobar.
	 */
	public boolean getConnectionStatus(String ip) {
		return connectionController.getConnectionStatus(ip);
	}

	/**
	 * Envia el mensaje recibido a la vista.
	 * @param message Mensaje recibido a través del socket.
	 */
	public void pushMessage(String ip, String message) {
		if(client != null) {
			client.pushMessage(ip, message);
		}
	}

	/**
	 * Envía un mensaje a la ip designada. Si la ip es de un peer la enviará directamente a este.
	 * Si la ip es desconocida hará un flood poniendo como destino esa Ip.
	 * Si la ip es null se entenderá como un broadcast
	 * @param ip IP de la conexión a utilizar para mandar un mensaje.
	 * @param message Mensaje que se desea enviar.
	 */
	public void sendMessage(String ip, String message) {
		connectionController.sendMessage(ip, message);
	}
	
	/**
	 * Añade una vista al controlador
	 * @param client Vista a añadir
	 */
	public void setClient(ClientP2P client) {
		this.client = client;
		connectionController.getConnections().forEach((connectionIp, connection) -> this.client.addConnection(connectionIp));
	}
	
	/**
	 * Detiene el servidor, el servicio de reconexión, cierra todas las conexiones establecidas y detiene el programa.
	 */
	public void stopAndQuit() {
		connectionController.stopAndQuit();
	}
	
}
