package communications.frame;

public class PingAckFrame extends Frame {
	
	private static final long serialVersionUID = -5514130143056617042L;
	
	public PingAckFrame() {
		super(FrameType.PING_ACK);
	}
	
	@Override
	public void setPayload(String payload) {
		// pierde la información del payload para hacer
		// al pingAck muy ligero
	}
	
}
