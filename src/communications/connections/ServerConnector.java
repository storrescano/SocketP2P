package communications.connections;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import communications.controller.IpUtilities;

public class ServerConnector {

	private int localPort;
	private boolean runState;
	private ConnectionController controller;
	
	ServerConnector(ConnectionController connectionController, int localPort) {
		this.controller = connectionController;
		this.localPort = localPort;
		this.runState = true;
		
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
		try(ServerSocket serverSocket = new ServerSocket(localPort)){
			logError("Activando para puerto " + localPort);
			while(runState) {
				if(!serverSocket.isClosed()) {
					try {
						Socket socket = serverSocket.accept();
						logError("Conexión establecida con " + IpUtilities.getSocketRemoteInfo(socket));
						controller.addConnection(socket);
					} catch(IOException e) {
						logError("Servidor desconectado o error al crear el socket de cliente.");
					}
				}
			}
			logError("Detenido.");
		} catch (IOException e) {
			logError("Error al arrancar el servidor.");
			e.printStackTrace();
			log("Deteniendo la aplicación.");
			runState = false;
			controller.stopAndQuit();
		}
	}
	
	void stopRun() {
		logError("Deteniendo");
		runState = false;
	}

}
