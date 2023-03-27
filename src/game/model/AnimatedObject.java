package game.model;

import game.controller.ObjectTypes;

public abstract class AnimatedObject extends VisualObject implements Runnable {

	private static int[][] statistics = new int[4][4];

	private final int FRAME_LAPSE = 40;

	
	public AnimatedObject(ObjectTypes type, Coordinates position) {
		setPosition(position);
		setObjectType(type);
	}
	

}
