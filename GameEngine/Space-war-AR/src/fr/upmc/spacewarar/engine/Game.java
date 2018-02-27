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
	private final GameController gameController = new GameController();
	private final Engine engine = new Engine();
	
	/*
	 * TODO right/left collision
	 */
	private final SimpleIntegerProperty scoreProperty = new SimpleIntegerProperty(0);
	private final SimpleIntegerProperty lifeProperty = new SimpleIntegerProperty(100);
	private final SimpleBooleanProperty gameOverProperty = new SimpleBooleanProperty(false);
	private final SimpleBooleanProperty gameWonProperty = new SimpleBooleanProperty(false);
	

	

	protected Game() {
		// init preperties
		gameOverProperty.bind(lifeProperty.lessThanOrEqualTo(0));

		// init triggers
		scoreProperty.addListener((ObservableValue<? extends Number> observable, Number oldValue,
				Number newValue) -> eventTrigger.scoreChangedTrigger(newValue.intValue()));// TODO : maybe put this code
																							// in setScore

		gameOverProperty
				.addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
					if (newValue) {
						gameOver();
					}
				});
	}

	private void gameOver() {
		// TODO :
		eventTrigger.gameOverTrigger();
	}


	protected void setScore(int score) { // TODO : increaseScoreBy !!
		scoreProperty.set(score);
	}

	protected void setLife(int life) { // TODO: change it by decreaseLifeBy !
		lifeProperty.set(life);
	}

	protected void setGameWon() {
		gameWonProperty.set(true);
		eventTrigger.gameWinningEventTrigger(getScore());
	}
	
	
	protected boolean isGameOver() {
		return gameOverProperty.get();
	}

	protected boolean isGameWon() {
		return gameWonProperty.get();
	}

	protected int getScore() {
		return scoreProperty.get();
	}

	protected Engine getEngine() {
		return engine;
	}
	
	@Override
	public IEventTrigger getEventTrigger() {
		return eventTrigger;
	}

	@Override
	public IGameController getGameController() {
		return gameController;
	}
	
	public static IGame getCurrentGame() {
		return CURRENT_GAME;
	}

	public static void main(String[] args) {
		Game c = CURRENT_GAME;
		System.out.println(c.isGameOver());
		c.setLife(5);
		System.out.println(c.isGameOver());
		c.setLife(0);
		System.out.println(c.isGameOver());
	}
}
