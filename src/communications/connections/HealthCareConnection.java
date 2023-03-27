package communications.connections;

public class HealthCareConnection {
	
	private Connection connection;
	private ConnectionStatus status;
	
	private int healthTimeOut;
	
	private boolean runState;
	
	HealthCareConnection(Connection connection, int healthTimeOut) {
		this.connection = connection;
		this.healthTimeOut = healthTimeOut;
		status = ConnectionStatus.OK;
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
		logError("Activado");
		long currentTime;
		while(runState) {
			try {
				Thread.sleep(healthTimeOut);
			} catch (InterruptedException e) {}
			if(connection.isOk()) {
				currentTime = System.currentTimeMillis();
				try {
					Thread.sleep(healthTimeOut);
				} catch (InterruptedException e) { }
				long lastTimeConnection = connection.getTimeReceivedMessage();
				if(currentTime - lastTimeConnection > healthTimeOut) {
					if(status == ConnectionStatus.OK) {
						status = ConnectionStatus.AWAITING_ACK;
							
						log("<OK> timeout = " + (currentTime - lastTimeConnection) + " (max. " + healthTimeOut + "ms)");
						log("<OK> sending ping");
							
						connection.doPing();
					} else {  // status == ConnectionStatus.AWAITING_OK
						log("<AWAITING_OK> timeout = " + (currentTime - lastTimeConnection) + " (max. " + healthTimeOut + "ms)");
							
						connection.killSocket();
						stopRun();
					}
				} else {
					status = ConnectionStatus.OK;
				}
			}
		}
		logError("Detenido.");
	}
	
	void stopRun() {
		if(runState) {
			logError("Deteniendo...");
			runState = false;
		}
	}
	
}
