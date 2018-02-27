package fr.upmc.spacewarar.engin;

import fr.upmc.spacewarar.engin.interfaces.events.IEventTrigger;
import fr.upmc.spacewarar.engin.interfaces.events.ScoreChangedEvent;

/**
 * 
 * @author Mohamed T. Kassar & Hakima Bouguetof
 *
 */
class EventTrigger implements IEventTrigger {
	private ScoreChangedEvent changedScoreEvent;
	private Runnable leftCollisionEvent;
	private Runnable rightCollisionEvent;
	private Runnable gameOverEvent;

	@Override
	public void setOnScoreChanged(ScoreChangedEvent event) {
		changedScoreEvent = event;
	}

	@Override
	public void setOnLeftCollision(Runnable event) {
		leftCollisionEvent = event;
	}

	@Override
	public void setOnRightCollision(Runnable event) {
		rightCollisionEvent = event;
	}
	
	@Override
	public void setOnGameOver(Runnable event) {
		gameOverEvent = event;
	}

	protected void scoreChangedTrigger(int score) {
		if (changedScoreEvent == null)
			throw new RuntimeException("changedScoreEvent is not set !");
		changedScoreEvent.run(score);
	}

	protected void leftCollisionTrigger() {
		if (leftCollisionEvent == null)
			throw new RuntimeException("leftCollisionEvent is not set !");
		leftCollisionEvent.run();
	}

	protected void rightCollisionTrigger() {
		if (rightCollisionEvent == null)
			throw new RuntimeException("rightCollisionEvent is not set !");
		rightCollisionEvent.run();
	}
	
	protected void gameOverTrigger() {
		if (gameOverEvent == null)
			throw new RuntimeException("gameOverEvent is not set !");
		gameOverEvent.run();
	}
}
