package fr.upmc.spacewarar.engine.interfaces;

import fr.upmc.spacewarar.engine.interfaces.events.ScoreEvent;

/**
 * 
 * @author Mohamed T. Kassar & Hakima Bouguetof
 *
 */
public interface IEventTrigger {

	void setOnScoreChanged(ScoreEvent event);

	void setOnLeftCollision(Runnable event);

	void setOnRightCollision(Runnable event);

	void setOnGameOver(Runnable event);

	void setOnGameWinning(ScoreEvent event);

}