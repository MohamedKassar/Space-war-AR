package fr.upmc.spacewarar.engin;

import fr.upmc.spacewarar.engin.interfaces.events.IEventTrigger;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;

/**
 * 
 * @author Mohamed T. Kassar & Hakima Bouguetof
 *
 */
public class Game {
	private static Game CURRENT_GAME = new Game();

	private final EventTrigger eventTrigger = new EventTrigger();
	private final SimpleIntegerProperty scoreProperty = new SimpleIntegerProperty(0);
	private final SimpleIntegerProperty lifeProperty = new SimpleIntegerProperty(100);
	private final SimpleBooleanProperty gameOverProperty = new SimpleBooleanProperty();

	protected Game() {
		init();
	}

	private void init() {
		// init preperties
		gameOverProperty.bind(lifeProperty.isEqualTo(0));

		// init triggers
		scoreProperty.addListener((ObservableValue<? extends Number> observable, Number oldValue,
				Number newValue) -> eventTrigger.scoreChangedTrigger(newValue.intValue()));

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

	protected void setScore(int score) {
		scoreProperty.set(score);
	}

	public IEventTrigger getEventTrigger() {
		return eventTrigger;
	}

	public static Game getCurrentGame() {
		return CURRENT_GAME;
	}

	public static void main(String[] args) {
		CURRENT_GAME.getEventTrigger().setOnScoreChanged(System.out::println);
		CURRENT_GAME.setScore(50);
		CURRENT_GAME.setScore(30);
	}
}
