package communications.frame;

import java.io.Serializable;

/* TODO:
 * afegir comanda: disparar, girar, acelerar, frenar
 * missatge feedback: nau destruida
 * missatge contador
 * id nau
 * comanda inici de partida
 * background selection
 * tipo de joc
 * tipus de nau
 * identificador de equip
 * 
 */

public abstract class Frame implements Serializable {

	private static final long serialVersionUID = 8929688478778125237L;
	
	private FrameType frameType;
	private Long id;
	private Integer timeToLive;
	private String sourceIp;
	private String targetIp;
	
	private String payload;
	
	protected Frame(FrameType frameType) {
		this.frameType = frameType;
	}
	
	/**
	 * Establece los datos de cabecera
	 * @param timeToLive Saltos disponibles antes de expirar
	 * @param sourceIp IP de origen
	 * @param targetIp IP de destino
	 */
	public void setHeader(Long id, Integer timeToLive, String sourceIp, String targetIp) {
		this.id = id;
		this.timeToLive = timeToLive;
		this.sourceIp = sourceIp;
		this.targetIp = targetIp;
	}
	
	/**
	 * Decrementa el tiempo de vida del communications.frame
	 * @return Devuelve 'true' si ha expirado, false en caso contrario
	 */
	public final boolean decrementTTL() {
		if(timeToLive > 0) {
			--timeToLive;
			return true;
		}
		return false;
	}
	
	/**
	 * Devuelve el id del mensaje
	 * @return Entero largo que representa el id del mensaje
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * Indica de qué tipo es el Frame
	 * @return FrameType con el tipo de communications.frame
	 */
	public FrameType getFrameType() {
		return this.frameType;
	}
	
	/**
	 * Obtiene los datos de la trama
	 * @return Retorna una cadena con los datos de interés
	 */
	public String getPayload() {
		return payload;
	}
	
	/**
	 * Devuelve la IP de destino del communications.frame
	 * @return String con la ip de destion
	 */
	public String getSourceIP() {
		return sourceIp;
	}
	
	/**
	 * Devuelve la IP de origen del communications.frame
	 * @return String con la ip de origen
	 */
	public String getTargetIP() {
		return targetIp;
	}
	
	/**
	 * Devuelve el Time To Live
	 * @return Entero que representa el tiempo de vida
	 */
	public Integer getTTL() {
		return timeToLive;
	}
	
	/**
	 * Establece los datos de la carga
	 * @param payload Carga de la trama
	 */
	public void setPayload(String payload) {
		this.payload = payload;
	}
}
