package communications.connections;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;

public class ClientConnector {
	
	private ConnectionController controller;
	private int remoteServerPort;
	private boolean runState;
	
	ClientConnector(ConnectionController connectionController, int remoteServerPort) {
		this.controller = connectionController;
		this.remoteServerPort = remoteServerPort;
		runState = true;
		new Thread(this::run).start();
	}
	
	@SuppressWarnings("unused")
	private void log(String message) {
		System.out.println(this.getClass().getSimpleName() + ": " + message);
	}
	
	@SuppressWarnings("unused")
	private void logError(String message) {
		System.err.println(this.getClass().getSimpleName() + ": " + message);
	}
	
	private void run() {
		logError("Activado.");
		while(runState) {
			// Recibe la lista de los antiguos peers que se han desconectado
			Map<String, Connection> connectionList = controller.getConnections();
			// Intenta conectar a cada peer de la lista
			connectionList.forEach((connectionIp, connection) -> {
				if(!connection.isOk()) {
					try {
						logError("Intento de reconexión con " + connectionIp);
						Socket socket = new Socket(connectionIp, remoteServerPort);
						logError("Éxito en la reconexión con " + connectionIp);
						controller.addConnection(socket);
					} catch (IOException e) {
						logError("Falló la reconexión con " + connectionIp);
					}
				}
			});
			
			// Espera para la siguente conexion
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {}
		}
		logError("Detenido.");
	}
	
	void stopRun() {
		logError("Deteniendo...");
		runState = false;
	}
	
}
