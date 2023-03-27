package communications.connections;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import communications.controller.IpUtilities;
import communications.frame.Frame;
import communications.frame.FrameType;
import communications.frame.MessageFrame;
import communications.frame.PingAckFrame;
import communications.frame.PingFrame;

public class Connection {
	
	private ConnectionController controller;
	
	private String targetIp;
	private Socket socket;
	private HealthCareConnection hcc;
	
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	private long lastTimeReceivedMessage;
	private int timeToLive;
	private int connectionTimeout;
	
	private Thread thread;
	
	private boolean runState;
	
	Connection(ConnectionController controller, String targetIp, int timeToLive, int connectionTimeout) {
		this.controller = controller;
		this.targetIp = targetIp;
		this.timeToLive = timeToLive;
		this.connectionTimeout = connectionTimeout;
	}
	
	/**
	 * Lanza un ping al destino de esta conexión
	 */
	void doPing() {
		if(isOk()) {
			Frame ping = new PingFrame();
			ping.setHeader(0L, 1, socket.getLocalAddress().getHostAddress(), targetIp);
			lastTimeReceivedMessage = System.currentTimeMillis();
			send(ping);
		}
	}
	
	/**
	 * Informa de la targetIp del peer
	 * @return Una cadena con la targetIp del peer, null en caso de no haber conectado
	 */
	public String getTargetIp() {
		return targetIp;
	}
	
	/**
	 * Devuelve el tiempo en milisegundos de la última conexión
	 * @return Un dato tipo long con el tiempo de la última conexión
	 */
	synchronized long getTimeReceivedMessage() {
		return lastTimeReceivedMessage;
	}
	
	private void handleFrame(Frame frame) {
		String myIp = socket.getLocalAddress().getHostAddress();
		switch(frame.getFrameType()) {
		case MESSAGE:
			// El paquete es nuestro. Lo matamos
			if(frame.getSourceIP().equals(myIp)) return;
			// El paquete va para nosotros o es un flood. Enviar el payload y la ip de origen al controlador para tratarlo.
			if(frame.getTargetIP().equals(myIp) || frame.getTargetIP().equals("*")) {
				controller.pushMessage(frame.getSourceIP(), frame.getPayload());
			}
			// Reenviar si no es para nosotros y su ttl no es 0
			if(!frame.getTargetIP().equals(myIp) && frame.decrementTTL()) {
				controller.resend(targetIp, frame);
			}
			break;
		case PING:
			// Consideramos viene directamente de este peer
			Frame response = new PingAckFrame();
			response.setHeader(0L, 1, myIp, targetIp);
			log("Enviando PingAck a " + targetIp);
			send(response);
			break;
		case PING_ACK:
			log("Recibido PingAck: " + socket.getInetAddress().getHostAddress());
			break;
		}
	}
	
	/**
	 * Informa si se tiene un socket abierto
	 * @return True si tiene socket, False en caso contrario
	 */
	boolean isOk() {
		return socket!=null && !socket.isClosed();
	}
	
	/**
	 * Elimina un socket. Borra el puerto adscrito. No cambia la targetIp
	 * para que este objeto esté vinculado a ella.
	 */
	void killSocket() {
		try {
			runState = false;
			if(hcc!=null)
				hcc.stopRun();
			if(socket != null && !socket.isClosed())
				socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			in = null;
			out = null;
			socket = null;
			logError("Matando el socket de " + targetIp);
		}
	}
	
	@SuppressWarnings("unused")
	private void log(String message) {
		System.out.println(this.getClass().getSimpleName() + ": " + message);
	}
	
	@SuppressWarnings("unused")
	private void logError(String message) {
		System.err.println(this.getClass().getSimpleName() + ": " + message);
	}
	
	private synchronized void receive() {
		if(isOk()) {
			try {
				Frame frame = (Frame)in.readObject();
				// Una vez recibido un mensaje actualiza la marca de tiempo de última recepción
				updateTimeReceivedMessage();
				// Si es un PING o un PING_ACK, tratarlo
				if(frame.getFrameType() == FrameType.PING || frame.getFrameType() == FrameType.PING_ACK) {
					handleFrame(frame);
				} else { // Si es otro tipo de mensaje comprobar si no es antiguo
					Long messageId = controller.getMessageControl(frame.getSourceIP());
					if(messageId == null || frame.getId() > messageId) {
						controller.setMessageControl(frame.getSourceIP(), frame.getId());
						handleFrame(frame);
					}
				}
			} catch (Exception e) {
				log("Error en receive");
				e.printStackTrace();
				killSocket();
			}
		}
	}
	
	private void run() {
		while(runState) {
			// Sólo ejecuta si hay conexión
			if(isOk()) {
				receive();
			} else {
				killSocket();
			}
		}
	}
	
	/**
	 * Envia un Frame por el socket
	 * @param mensaje Texto a enviar
	 */
	void send(Frame frame) {
		if(isOk()) {
			try {
				out.writeObject(frame);
			} catch (IOException e) {
				e.printStackTrace();
				killSocket();
			}
		}
	}
	
	void send(long messageId, String payload) {
		if(isOk()) {
			send(messageId, targetIp, payload);
		}
	}
	
	void send(long messageId, String destinationIp, String payload) {
		try {
			// Si destinationIp no es una ip valida, salir
			if(!IpUtilities.isValidIp(destinationIp))
				return;
		} catch (NullPointerException e) {
			// La ip vino vacía. Marcarla como flood
			destinationIp = "*";
		}
		if(isOk()) {
			Frame frame = new MessageFrame();
			frame.setHeader(messageId, timeToLive, socket.getLocalAddress().getHostAddress(), destinationIp);
			frame.setPayload(payload);
			send(frame);
		}
	}
	
	/**
	 * Añade un socket a la conexión
	 * @param socket Socket por el que hará la conexión
	 */
	void setSocket(Socket socket) {
		if(!isOk() && targetIp.equals(socket.getInetAddress().getHostAddress())) {
			this.socket = socket;
			// Configura los objetos para mandar y recibir información
			try {
				out = new ObjectOutputStream(socket.getOutputStream());
				in = new ObjectInputStream(socket.getInputStream());
				updateTimeReceivedMessage();
				runState = true;
				if(thread == null || !thread.isAlive()) { 
					thread = new Thread(this::run);
					thread.start();
				}
				hcc = new HealthCareConnection(this, connectionTimeout);
			} catch (Exception e) {
				killSocket();
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Detiene el hilo secundario
	 */
	void stopRun() {
		if(runState) {
			logError("Detenido");
			killSocket();
		}
	}
	
	synchronized void updateTimeReceivedMessage() {
		lastTimeReceivedMessage = System.currentTimeMillis();
	}
	
}
