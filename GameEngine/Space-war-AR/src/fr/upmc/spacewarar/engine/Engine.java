package fr.upmc.spacewarar.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.upmc.spacewarar.engine.graphiccomponens.Enemy;
import fr.upmc.spacewarar.engine.graphiccomponens.Robot;
import fr.upmc.spacewarar.engine.graphiccomponens.Robot.Command;
import fr.upmc.spacewarar.engine.graphiccomponens.Rocket;
import fr.upmc.spacewarar.engine.interfaces.IGameController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

/**
 * 
 * @author Mohamed T. Kassar & Hakima Bouguetof
 *
 */
public class Engine implements IGameController {
	/*
	 * TODO graphic components here
	 * 
	 * connect GameController with engine
	 */
	private final Game game;
	private Robot robot;
	private static final AnchorPane canvas = new AnchorPane();
	private final List<Enemy> enemies = new ArrayList<>();
	private final List<Rocket> rockets = new ArrayList<>();

	static {
		canvas.setPrefHeight(1080);
		canvas.setPrefWidth(1920);
		canvas.setStyle("-fx-background-image: url('file:///"
				+ new File("resources/bcg01.jpg").getAbsolutePath().replace("\\", "/")
				+ "');-fx-background-size: stretch;");
	}

	private static final int FPS = 3;
	private final Timeline scheduler;

	private boolean started = false;
	private boolean paused = false;

	protected Engine(Game game) {
		scheduler = new Timeline(new KeyFrame(Duration.millis(1000 / FPS), e -> step()));
		scheduler.setCycleCount(Timeline.INDEFINITE);
		this.game = game;
	}

	protected void step() {
		enemies.forEach(enemy -> enemy.step());
		robot.step();
	}

	@Override
	public void stop() {
		paused = false;
		started = false;
		scheduler.stop();
	}

	@Override
	public void pause() {
		scheduler.pause();
		paused = true;
	}

	@Override
	public void start() {
		if (!started) {
			robot = new Robot(this);
			enemies.clear();
			// TODO create enemies

			rockets.clear();
			canvas.getChildren().clear();
			canvas.getChildren().add(robot);
			canvas.getChildren().addAll(enemies);
			System.out.println(game);
			game.reinitGame();
			started = true;
		} else if (paused) {
			paused = false;
		}
		scheduler.play();
	}

	public void addRocket(Rocket rocket) {
		rockets.add(rocket);
	}

	public void removeRocket(Rocket rocket) {
		rockets.remove(rocket);
	}

	@Override
	public void shoot() {
		robot.sendCommand(Command.SHOOT);
	}

	public static AnchorPane getCanvas() {
		return canvas;
	}

	public Robot getRobot() {
		return robot;
	}
}
