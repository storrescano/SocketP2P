package communications.controller;

import java.net.Socket;
import java.util.regex.Pattern;

public final class IpUtilities {
	
	/**
	 * Devuelve una cadena con la información de la dirección del socket.
	 * @param socket Socket activo del que se desea extraer la información
	 * @return String con el formato ip:puerto
	 */
	public static String getSocketRemoteInfo(Socket socket) {
		return socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
	}
	
	/**
	 * Establece si una IP dada por una cadena es válida.
	 * @param ip String con una ip.
	 */
	public static boolean isValidIp(String ip) throws NullPointerException {
		if(ip == null) {
			throw new NullPointerException("Argument ip cannot be null.");
		} else {
			String IPV4_PATTERN =
	            "^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$";
			Pattern pattern = Pattern.compile(IPV4_PATTERN);
			return pattern.matcher(ip).matches();
		}
	}
}
