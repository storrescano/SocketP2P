package communications.connections;

import java.net.Socket;
import java.util.Hashtable;
import java.util.Map;

import communications.controller.IpUtilities;
import communications.controller.MyP2P;
import communications.frame.Frame;

public class ConnectionController {
	
	private long messageId;
	private int defaultTimeToLive;
	private int connectionTimeout;
	
	private Map<String, Connection> connectionList;
	private Map<String, Long> messageControl;
	private ServerConnector serverConnector;
	private ClientConnector clientConnector;
	
	private MyP2P controller;
	
	public ConnectionController(MyP2P controller, int defaultTimeToLive, int connectionTimeout, int serverPort) {
		this.controller = controller;
		
		this.defaultTimeToLive = defaultTimeToLive;
		this.connectionTimeout = connectionTimeout;
		
		connectionList = new Hashtable<>();
		messageControl = new Hashtable<>();
		
		serverConnector = new ServerConnector(this, serverPort);
		clientConnector = new ClientConnector(this, serverPort);
	}
	
	/**
	 * Guarda en la lista de conexiones el socket de una nueva conexión.
	 * @param socket Socket establecido con la nueva conexión.
	 */
	synchronized void addConnection(Socket socket) {
		// obtener el índice en la lista de conexiones que tuviera esa ip
		String ip = socket.getInetAddress().getHostAddress();
		addEmptyConnection(ip);
		Connection connection = connectionList.get(ip);
		if(!connection.isOk()) {
			connection.setSocket(socket);
		}
	}
	
	/**
	 * Crea una nueva conexión sin socket para una ip dada. Si ya existe devuelve el índice
	 * para dicha conexión.
	 * @param ip IP de la conexión que se desea establecer.
	 * @return el índice en la lista de conexiones de la conexión con la ip dada.
	 */
	public void addEmptyConnection(String ip) {
		if(!connectionList.containsKey(ip)) {
			connectionList.put(ip, new Connection(this, ip, defaultTimeToLive, connectionTimeout));
			controller.informNewConnection(ip);
		}
	}
	
	/**
	 * Obtiene el índice en la lista de conexiones cuya ip es la indicada.
	 * @param ip String con la ip de la conexión.
	 * @return Retorna el índice de la conexión el la lista. -1 Si dicha ip no existe.
	 */
	private Connection getConnectionFromIp(String ip) throws NullPointerException, IllegalArgumentException {
		if(IpUtilities.isValidIp(ip)) {
			return connectionList.get(ip);
		} else {
			throw new IllegalArgumentException("Argument ip is not conformant to [0,255].[0,255].[0,255].[0,255]");
		}
	}

	/**
	 * Indica el estado con la conexión ip.
	 * @param ip IP de la conexión que se quiere comprobar.
	 */
	public boolean getConnectionStatus(String ip) {
		boolean retValue = false;
		Connection connection = getConnectionFromIp(ip);
		if(connection != null) {
			retValue = connection.isOk();
		}
		return retValue;
	}

	/**
	 * Envia el mensaje recibido a la vista.
	 * @param message Mensaje recibido a través del socket.
	 */
	void pushMessage(String ip, String message) {
		controller.pushMessage(ip, message);
	}
	
	synchronized void setMessageControl(String ip, long messageId) {
		messageControl.put(ip, messageId);
	}
	
	synchronized Long getMessageControl(String ip) {
		return messageControl.get(ip);
	}

	public Map<String, Connection> getConnections() {
		return connectionList;
	}
	
	@SuppressWarnings("unused")
	private void log(String message) {
		System.out.println(this.getClass().getSimpleName() + ": " + message);
	}
	
	@SuppressWarnings("unused")
	private void logError(String message) {
		System.err.println(this.getClass().getSimpleName() + ": " + message);
	}
	
	/**
	 * Reenvia un paquete. Si el destino del paquete, que está dentro del communications.frame,
	 * es uno de nuestros peers, enviarlo solo a él, en caso contrario hacer un flood
	 * para todas las ips conocidas menos de peer quien nos lo envió.
	 * @param bannedIp Ip a la que no se debe de retransmitir
	 */
	void resend(String bannedIp, Frame frame) {
		try {
			Connection connection = getConnectionFromIp(frame.getTargetIP());
			if(connection != null) {
				connection.send(frame);
			}
		} catch(IllegalArgumentException e) {
			// La ip tiene una forma correcta. Hay que mirar si tiene un *, símbolo de que
			// se quiere hacer un flood.
			// En caso de no ser así, terminar el método.
			if(frame.getTargetIP().equals("*")) {
				connectionList.forEach((connectionIp, connection) -> {
					if(!connectionIp.equals(bannedIp)) {
						connection.send(frame);
					}
				});
			}
		} catch(NullPointerException e) {
			// Las cosas han ido terriblemente mal y se ha colado un null en donde no debería llegar nunca.
			// Desechamos el reenvio regresando de la función.
		}
	}

	/**
	 * Envía un mensaje a la ip designada. Si la ip es de un peer la enviará directamente a este.
	 * Si la ip es desconocida hará un flood poniendo como destino esa Ip.
	 * Si la ip es null se entenderá como un broadcast
	 * @param ip IP de la conexión a utilizar para mandar un mensaje.
	 * @param payload Mensaje que se desea enviar.
	 */
	public void sendMessage(String ip, String payload) {
		try {
			Connection connection = getConnectionFromIp(ip);
			if(connection != null) {
				connection.send(messageId, payload);
				messageId++;
			}
		} catch (NullPointerException e) {
			// La ip es null, por lo tanto enviar un flood
			connectionList.forEach((connectionIp, connection) -> connection.send(messageId, null, payload));
			messageId++;
		} catch (IllegalArgumentException e) {
			// El argumento pasado no representa una ip válida. No enviar nada
		}
	}
	
	/**
	 * Detiene el servidor, el servicio de reconexión, cierra todas las conexiones establecidas y detiene el programa.
	 */
	public void stopAndQuit() {
		// Detiene el servidor
		serverConnector.stopRun();
		
		// Detiene el resucitador
		clientConnector.stopRun();
		
		connectionList.forEach((connectionIp, connection) -> connection.stopRun());
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {}
		
		System.exit(0);
	}
	
}
