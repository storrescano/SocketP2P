package communications.frame;

public class PingFrame extends Frame {

	private static final long serialVersionUID = 2388421454502439090L;

	public PingFrame() {
		super(FrameType.PING);
	}
	
	@Override
	public void setPayload(String payload) {
		// pierde la información del payload para hacer
		// al ping muy ligero
	}
	
}
