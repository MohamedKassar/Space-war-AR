package fr.upmc.spacewarar.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import fr.upmc.spacewarar.engine.graphiccomponens.Enemy;
import fr.upmc.spacewarar.engine.graphiccomponens.Robot;
import fr.upmc.spacewarar.engine.graphiccomponens.Robot.Command;
import fr.upmc.spacewarar.engine.graphiccomponens.Rocket;
import fr.upmc.spacewarar.engine.graphiccomponens.Rocket.Direction;
import fr.upmc.spacewarar.engine.interfaces.IGameController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import javafx.util.Pair;

/**
 * 
 * @author Mohamed T. Kassar & Hakima Bouguetof
 *
 */
public class Engine implements IGameController {

	private final Game game;
	private Robot robot;
	private static final AnchorPane canvas = new AnchorPane();

	private static final AnchorPane gameOver = new AnchorPane();
	private static final AnchorPane gameWon = new AnchorPane();
	private static final AnchorPane gamePaused = new AnchorPane();
	private static final AnchorPane gameStart = new AnchorPane();

	protected static final Label scoreLabel = new Label("Score : 000");
	protected static final Label lifeLabel = new Label("Life : 100");

	private final ObservableList<Enemy> enemies = FXCollections.observableArrayList();
	private final ObservableList<Rocket> rockets = FXCollections.observableArrayList();

	private final SimpleBooleanProperty wonProprety = new SimpleBooleanProperty(false);

	private static final List<Point2D> enemiesPositions = Arrays.asList(new Point2D(900, 200), new Point2D(1100, 200),
			new Point2D(700, 300), new Point2D(1000, 300), new Point2D(1300, 300), new Point2D(600, 400),
			new Point2D(800, 400), new Point2D(1200, 400), new Point2D(1400, 400), new Point2D(700, 500),
			new Point2D(900, 500), new Point2D(1100, 500), new Point2D(1300, 500), new Point2D(800, 600),
			new Point2D(1000, 600), new Point2D(1200, 600), new Point2D(500, 300), new Point2D(1500, 300),
			new Point2D(400, 400), new Point2D(1600, 400), new Point2D(500, 500), new Point2D(1500, 500));

	static {
		canvas.setPrefHeight(1080);
		canvas.setPrefWidth(1920);
		canvas.setStyle("-fx-background-image: url('file:///"
				+ new File("resources/bcg03.gif").getAbsolutePath().replace("\\", "/")
				+ "');-fx-background-size: stretch;");
		final String style = " -fx-font-size: 160pt;" + "-fx-font-family: 'Game Over';" + "    -fx-text-fill: white;"
				+ "-fx-opacity: 0.95;";

		List<Pair<AnchorPane, String>> panes = Arrays.asList(new Pair<>(gameOver, "GAME OVER"),
				new Pair<>(gameStart, "Space war AR"), new Pair<>(gameWon, "GAME WON"),
				new Pair<>(gamePaused, "Game in pause"));

		panes.forEach(pair -> {
			Label label = new Label(pair.getValue());
			label.setStyle(style);
			label.setLayoutX(1920 / 2 - 320);
			label.setLayoutY(1080 / 2 - 100);

			pair.getKey().setStyle("-fx-background-color:rgba(0, 0, 0, 0.5);");
			AnchorPane.setBottomAnchor(pair.getKey(), 0d);
			AnchorPane.setTopAnchor(pair.getKey(), 0d);
			AnchorPane.setRightAnchor(pair.getKey(), 0d);
			AnchorPane.setLeftAnchor(pair.getKey(), 0d);
			pair.getKey().getChildren().add(label);
		});

		scoreLabel.setLayoutX(1500);
		scoreLabel.setLayoutY(10);
		String style_ = "-fx-font-size: 40pt; -fx-font-family: 'Super Mario Bros.';-fx-text-fill: white;-fx-opacity: 0.95;";
		scoreLabel.setStyle(style_);
		lifeLabel.setStyle(style_);
		lifeLabel.setLayoutX(60);
		lifeLabel.setLayoutY(10);

		canvas.getChildren().add(gameStart);
		canvas.getChildren().add(scoreLabel);
		canvas.getChildren().add(lifeLabel);
	}

	public static final int FPS = 60;
	private final Timeline scheduler;

	private boolean started = false;
	private boolean paused = false;

	protected Engine(Game game) {
		scheduler = new Timeline(new KeyFrame(Duration.millis(1000 / FPS), e -> step()));
		scheduler.setCycleCount(Animation.INDEFINITE);
		this.game = game;
		enemies.addListener((Change<? extends Enemy> c) -> {
			wonProprety.set(enemies.size() == 0);
		});
	}

	protected void step() {
		rockets.forEach(rocket -> rocket.step());
		enemies.forEach(enemy -> enemy.step());
		robot.step();
		enemies.stream().filter(x -> ThreadLocalRandom.current().nextInt(600) / ((1 - (enemies.size() / 22)) + 1) < 1)
				.forEach(enemy -> {
					addRocket(new Rocket(enemy.getLayoutX() + enemy.getPrefWidth() / 2,
							enemy.getLayoutY() + enemy.getPrefHeight() + 2, Direction.DOWN, 10));
				});
		List<Rocket> rocketsToRemove = new ArrayList<>();
		rockets.forEach(rocket -> {
			if (rocket.isEnemyRocket()) {
				if (rocket.collidesWith(robot)) {
					robot.decreaseLifeBy(rocket.getPower());
					game.decreaseScoreBy(10);
					rocketsToRemove.add(rocket);
				}
			} else {
				enemies.stream().filter(enemy -> rocket.collidesWith(enemy)).forEach(enemy -> {
					enemy.decreaseLifeBy(rocket.getPower());
					game.increaseScoreBy(10);
					rocketsToRemove.add(rocket);
				});
			}
			rockets.stream()
					.filter(r -> !r.equals(rocket) && !rocketsToRemove.contains(rocket) && rocket.collidesWith(r))
					.findFirst().ifPresent(r -> {
						rocketsToRemove.add(r);
						rocketsToRemove.add(rocket);
						game.increaseScoreBy(15);
					});
		});
		rockets.stream().filter(rocket -> rocket.getLayoutY() < -30 || rocket.getLayoutY() > 1920)
				.forEach(rocketsToRemove::add);
		rocketsToRemove.forEach(this::removeRocket);
		enemies.stream().filter(enemy -> enemy.isDead()).findFirst().ifPresent(this::removeEnemy);
	}

	@Override
	public void stop() {
		Platform.runLater(() -> {
			if (started) {
				paused = false;
				started = false;
				scheduler.stop();
				canvas.getChildren().remove(gamePaused);
				if (game.isGameOver()) {
					canvas.getChildren().add(gameOver);
				} else if (game.isGameWon()) {
					canvas.getChildren().add(gameWon);
				} else {
					canvas.getChildren().add(gameStart);
				}

			}
		});
	}

	@Override
	public void pause() {
		Platform.runLater(() -> {
			if (started) {
				scheduler.pause();
				paused = true;
				canvas.getChildren().add(gamePaused);
			}
		});

	}

	@Override
	public void start() {
		Platform.runLater(() -> {
			if (!started) {
				robot = new Robot(this);
				enemies.clear();
				enemiesPositions.stream().map(Enemy::new).forEach(enemies::add);
				rockets.clear();
				canvas.getChildren().clear();
				canvas.getChildren().add(robot);
				canvas.getChildren().add(scoreLabel);
				canvas.getChildren().addAll(enemies);
				canvas.getChildren().add(lifeLabel);
				scoreLabel.setText("Score : 000");
				lifeLabel.textProperty().bind(new SimpleStringProperty("Life : ").concat(robot.lifeProperty()));
				game.reinitGame();
				started = true;
			} else if (paused) {
				paused = false;
				canvas.getChildren().remove(gamePaused);
			}
			scheduler.play();
		});
	}

	protected BooleanProperty wonProperty() {
		return this.wonProprety;
	}

	public void addRocket(Rocket rocket) {
		canvas.getChildren().add(rocket);
		rockets.add(rocket);
	}

	public void removeRocket(Rocket rocket) {
		canvas.getChildren().remove(rocket);
		rockets.remove(rocket);
	}

	public void removeEnemy(Enemy enemy) {
		canvas.getChildren().remove(enemy);
		enemies.remove(enemy);
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
