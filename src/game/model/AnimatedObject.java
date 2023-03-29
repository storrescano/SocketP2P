package game.model;


import game.controller.AnimatedObjectStatus;
import game.controller.ObjectTypes;

public abstract class AnimatedObject extends VisualObject implements Runnable {
	private final int FRAME_LAPSE = 40;
	
	private AnimationModel animationModel;
	private AnimatedObjectStatus status;
	
	public AnimatedObject(AnimationModel animationModel, ObjectTypes type, Coordinates position) {
		this.animationModel = animationModel;
		setPosition(position);
		setObjectType(type);
		setStatus(AnimatedObjectStatus.running);
	}
	
	public synchronized AnimatedObjectStatus getStatus() {
		return status;
	}
	
	public synchronized void setStatus(AnimatedObjectStatus status) {
		this.status = status;
	}
	
	protected abstract void update();
	
	@Override
	public void run() {
		while(getStatus() != AnimatedObjectStatus.dead) {
			update();
			try {
				Thread.sleep(FRAME_LAPSE);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
