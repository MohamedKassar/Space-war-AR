package fr.upmc.spacewarar.engine.interfaces;

/**
 * 
 * @author Mohamed T. Kassar & Hakima Bouguetof
 *
 */
public interface IGameController {
	void stop();

	void pause();

	void start();

	void startMovingRight();

	void startMovingLeft();

	void stopMoving();

	void shoot();
}
