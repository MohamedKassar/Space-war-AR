package fr.upmc.spacewarar.engine.graphiccomponens;

import java.io.File;

import fr.upmc.spacewarar.engine.Engine;
import fr.upmc.spacewarar.engine.EventTrigger;
import fr.upmc.spacewarar.engine.Game;
import fr.upmc.spacewarar.engine.graphiccomponens.Rocket.Direction;
import fr.upmc.spacewarar.ocv.interfaces.IObjectTracker;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;

/**
 * 
 * @author Mohamed T. Kassar & Hakima Bouguetof
 *
 */
public class Robot extends GraphicComponent {
	public enum Command {
		SHOOT, NONE
	}

	private Command command = Command.NONE;

	private SimpleBooleanProperty collidesWhithLeftBorder = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty collidesWhithRightBorder = new SimpleBooleanProperty(false);
	private final SimpleIntegerProperty lifeProperty = new SimpleIntegerProperty(100);

	private final Engine engine;

	public Robot(Engine engine) {

		this.engine = engine;
		setPrefSize(250, 200);
		setLayoutY(1080 - 210);
		setLayoutX(950);
		setStyle("-fx-border-width: 2px; -fx-border-color: #2e8b57; -fx-background-image: url('file:///"
				+ new File("resources/crabe01.png").getAbsolutePath().replace("\\", "/")
				+ "');-fx-background-size: stretch;");

		collidesWhithLeftBorder.bind(layoutXProperty().lessThan(0));
		collidesWhithRightBorder.bind(layoutXProperty().greaterThan(1920 - getPrefWidth()));

		Game game = (Game) Game.getCurrentGame();
		EventTrigger eventTrigger = (EventTrigger) game.getEventTrigger();
		collidesWhithLeftBorder
				.addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
					eventTrigger.leftCollisionTrigger();
				});
		collidesWhithRightBorder
				.addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
					eventTrigger.rightCollisionTrigger();
				});
		
//		lifeProperty.addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
//			System.out.println(newValue);
//		});
	}

	private int lastShoot = Engine.FPS / 8 + 1;
	public void sendCommand(Command command) {
		if (this.command == Command.NONE && lastShoot > Engine.FPS / 5) {
			this.command = command;
			lastShoot = 0;
		}
	}

	public SimpleIntegerProperty lifeProperty() {
		return lifeProperty;
	}

	public void decreaseLifeBy(int p) {
		lifeProperty.set(lifeProperty.get() - p);
	}

	@Override
	public void step() {
		switch (command) {
		case SHOOT:
			engine.addRocket(new Rocket(getLayoutX() + getPrefWidth() / 2, getLayoutY() + 5, Direction.UP, 20));
			command = Command.NONE;
			break;
		case NONE:
			lastShoot = (lastShoot + 1) % Integer.MAX_VALUE;
			break;
		}
		updatePosition();
	}

	private boolean dir = true;

	private void updatePosition() {
		IObjectTracker objectTracker = () -> {
			if (getLayoutX() - 3 < 0) {
				dir = true;
			}
			if (getLayoutX() + 3 > 1920 - getPrefWidth()) {
				dir = false;
			}
			return (int) (dir ? getLayoutX() + 3 : getLayoutX() - 3);
		};
		setLayoutX(objectTracker.getPosition());
	}

}
