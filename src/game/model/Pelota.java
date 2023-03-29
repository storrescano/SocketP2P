package game.model;

import game.controller.ObjectTypes;

import java.util.Random;

public class Pelota extends AnimatedObject{
	private int velocidadX;
	private int velocidadY;

	private final Boolean destroyOnContact = false;
	private Coordinates positionActual;
	private Coordinates positionUpdated;

	private Coordinates posicionAbsoluta;

	
	private Random Random;
	
	public Pelota(AnimationModel animationModel, ObjectTypes type, Coordinates position) {
		super(animationModel, type, position);
		positionUpdated = new Coordinates();
		positionActual = new Coordinates();
		
		Random = new Random();
		while((velocidadX = Random.nextInt(-3, 4)) == 0);
		while((velocidadY = Random.nextInt(-3, 4)) == 0);
	}

	@Override
	public void update() {
		positionActual = getPosition();
		if(positionActual.x >= 1125-100) {
			velocidadX = Random.nextInt(-3, 0);
		} else if(positionActual.x <= 0) {
			velocidadX = Random.nextInt(1, 4);
		}
		if(positionActual.y >= 750-100) {
			velocidadY = Random.nextInt(-3, 0);
		} else if(positionActual.y <= 0) {
			velocidadY = Random.nextInt(1, 4);
		}
		
		positionUpdated.x = positionActual.x+ velocidadX;
		positionUpdated.y = positionActual.y+ velocidadY;
		setPosition(positionUpdated);
	}

	public Coordinates getPositionActual() {
		return positionActual;
	}

	public Coordinates getPositionUpdated() {
		return positionUpdated;
	}


	public Coordinates getPosicionAbsoluta() {
		return posicionAbsoluta;
	}

	public void setPosicionAbsoluta(Coordinates posicionAbsoluta) {
		this.posicionAbsoluta = posicionAbsoluta;
	}

}
