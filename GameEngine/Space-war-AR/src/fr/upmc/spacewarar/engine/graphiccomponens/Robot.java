package fr.upmc.spacewarar.engine.graphiccomponens;

import java.io.File;
import java.util.concurrent.ThreadLocalRandom;

import fr.upmc.spacewarar.engine.Engine;
import fr.upmc.spacewarar.engine.EventTrigger;
import fr.upmc.spacewarar.engine.Game;
import fr.upmc.spacewarar.ocv.interfaces.IObjectTracker;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;

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

		collidesWhithLeftBorder.bind(layoutXProperty().lessThanOrEqualTo(0));
		collidesWhithRightBorder.bind(layoutXProperty().greaterThanOrEqualTo(1920 - getPrefWidth()));

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
	}

	public void sendCommand(Command command) {
		if (command == Command.NONE) {
			this.command = command;
		}
	}

	public SimpleIntegerProperty lifeProperty() {
		return lifeProperty;
	}

	protected void setLife(int life) { // TODO: change it by decreaseLifeBy !
		lifeProperty.set(life);
	}

	@Override
	public void step() {
		switch (command) {
		case SHOOT:
			engine.addRocket(null);// TODO
			break;
		case NONE:
			break;
		}
		System.out.println("allo");
		updatePosition();
		command = Command.NONE;
	}

	private void updatePosition() {
		IObjectTracker objectTracker = () -> ThreadLocalRandom.current().nextInt(100);
		setLayoutX(objectTracker.getPosition() * 19.2);
	}
}
