package fr.upmc.spacewarar.engine.interfaces.events;

/**
 * 
 * @author Mohamed T. Kassar & Hakima Bouguetof
 *
 */
public interface IEventTrigger {

	void setOnScoreChanged(ScoreChangedEvent event);

	void setOnLeftCollision(Runnable event);

	void setOnRightCollision(Runnable event);

	void setOnGameOver(Runnable event);

}