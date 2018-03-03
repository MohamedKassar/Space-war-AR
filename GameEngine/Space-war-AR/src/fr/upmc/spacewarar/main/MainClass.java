package fr.upmc.spacewarar.main;

import fr.upmc.spacewarar.engine.Engine;
import fr.upmc.spacewarar.engine.Game;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * 
 * @author Mohamed T. Kassar & Hakima Bouguetof
 *
 */
public class MainClass extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Space war AR");
		// primaryStage.getIcons()
		Scene scene = new Scene(Engine.getCanvas(), 1920, 1080);
		primaryStage.setScene(scene);
		Engine.getCanvas().getChildren().add(new Label());
		primaryStage.setResizable(false);
		primaryStage.show();
		primaryStage.setFullScreen(true);

		scene.setOnKeyTyped((KeyEvent event) -> {
			if (event.getCharacter().equals("f")) {
				primaryStage.setFullScreen(true);
			} else if (event.getCharacter().equals(" ")) {
				Game.getCurrentGame().getGameController().shoot();
			} else if (event.getCharacter().equals("w")) {
				Game.getCurrentGame().getGameController().start();
			} else if (event.getCharacter().equals("x")) {
				Game.getCurrentGame().getGameController().pause();
			} else if (event.getCharacter().equals("c")) {
				Game.getCurrentGame().getGameController().stop();
			}
		});

		Game.getCurrentGame().getEventTrigger().setOnGameOver(() -> {
		});
		Game.getCurrentGame().getEventTrigger().setOnGameWinning(s -> {
			System.out.println(s);
		});
		Game.getCurrentGame().getEventTrigger().setOnLeftCollision(() -> {
		});
		Game.getCurrentGame().getEventTrigger().setOnRightCollision(() -> {
		});
		Game.getCurrentGame().getEventTrigger().setOnScoreChanged(s -> {
			System.out.println(s);
		});
		// Game.getCurrentGame().getGameController().start();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}
