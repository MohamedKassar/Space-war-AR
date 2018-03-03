package fr.upmc.spacewarar.engine;

import fr.upmc.spacewarar.engine.interfaces.IEventTrigger;
import fr.upmc.spacewarar.engine.interfaces.IGame;
import fr.upmc.spacewarar.engine.interfaces.IGameController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;

/**
 * 
 * @author Mohamed T. Kassar & Hakima Bouguetof
 *
 */
public class Game implements IGame {
	private static Game CURRENT_GAME = new Game();

	private final EventTrigger eventTrigger = new EventTrigger();
	private final Engine engine = new Engine(this);

	private SimpleIntegerProperty scoreProperty;

	private SimpleBooleanProperty gameOverProperty;
	private SimpleBooleanProperty gameWonProperty;

	protected Game() {
		// reinitGame();
	}

	void reinitGame() {
		scoreProperty = new SimpleIntegerProperty(0);
		gameOverProperty = new SimpleBooleanProperty(false);
		gameWonProperty = new SimpleBooleanProperty(false);
		// init preperties
		gameOverProperty.bind(engine.getRobot().lifeProperty().lessThanOrEqualTo(0));
		gameWonProperty.bind(engine.wonProperty());
		// init triggers
		scoreProperty.addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
			Engine.scoreLabel.setText("Score : " + newValue);
			eventTrigger.scoreChangedTrigger(newValue.intValue());
		});
		gameOverProperty
				.addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
					if (newValue) {
						setGameOver();
					}
				});
		gameWonProperty
				.addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
					if (newValue) {
						setGameWon();
					}
				});

	}

	private void setGameOver() {
		engine.stop();
		eventTrigger.gameOverTrigger();
	}

	private void setGameWon() {
		engine.stop();
		eventTrigger.gameWinningEventTrigger(scoreProperty.get());
	}

	public void increaseScoreBy(int score) {
		scoreProperty.set(scoreProperty.get() + score);
	}

	public void decreaseScoreBy(int score) {
		scoreProperty.set(scoreProperty.get() - score);
	}

	protected boolean isGameOver() {
		return gameOverProperty.get();
	}

	protected boolean isGameWon() {
		return gameWonProperty.get();
	}

	@Override
	public IEventTrigger getEventTrigger() {
		return eventTrigger;
	}

	@Override
	public IGameController getGameController() {
		return engine;
	}

	public static IGame getCurrentGame() {
		return CURRENT_GAME;
	}

}
