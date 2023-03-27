package communications.controller;

/**
 * Interfaz del cliente con el controlador
 * @author mique
 *
 */
public interface ClientP2P {
	/**
	 * Añade una nueva conexión descubierta por el controlador
	 * @param ip String con la ip de la nueva conexión
	 */
	public void addConnection(String ip);
	
	/**
	 * Envía el mensaje recibido desde la ip dada
	 * @param ip String con la ip que recepciona el mensaje
	 * @param message String con el mensaje recibido
	 */
	public void pushMessage(String ip, String message);
}
