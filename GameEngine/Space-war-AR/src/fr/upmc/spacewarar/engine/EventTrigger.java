package fr.upmc.spacewarar.engine;

import fr.upmc.spacewarar.engine.interfaces.IEventTrigger;
import fr.upmc.spacewarar.engine.interfaces.events.ScoreEvent;

/**
 * 
 * @author Mohamed T. Kassar & Hakima Bouguetof
 *
 */
public class EventTrigger implements IEventTrigger {
	private ScoreEvent changedScoreEvent;
	private ScoreEvent gameWinningEvent;
	private Runnable leftCollisionEvent;
	private Runnable rightCollisionEvent;
	private Runnable gameOverEvent;

	protected EventTrigger() {
	}

	@Override
	public void setOnScoreChanged(ScoreEvent event) {
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

	@Override
	public void setOnGameWinning(ScoreEvent event) {
		gameWinningEvent = event;
	}

	public void scoreChangedTrigger(int score) {
		if (changedScoreEvent == null)
			throw new RuntimeException("changedScoreEvent is not set !");
		changedScoreEvent.run(score);
	}

	public void leftCollisionTrigger() {
		if (leftCollisionEvent == null)
			throw new RuntimeException("leftCollisionEvent is not set !");
		leftCollisionEvent.run();
	}

	public void rightCollisionTrigger() {
		if (rightCollisionEvent == null)
			throw new RuntimeException("rightCollisionEvent is not set !");
		rightCollisionEvent.run();
	}

	public void gameOverTrigger() {
		if (gameOverEvent == null)
			throw new RuntimeException("gameOverEvent is not set !");
		gameOverEvent.run();
	}

	public void gameWinningEventTrigger(int score) {
		if (gameWinningEvent == null)
			throw new RuntimeException("gameWinningEvent is not set !");
		gameWinningEvent.run(score);
	}
	
}
