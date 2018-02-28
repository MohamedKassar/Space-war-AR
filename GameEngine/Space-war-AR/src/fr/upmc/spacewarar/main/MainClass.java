package fr.upmc.spacewarar.main;

import fr.upmc.spacewarar.engine.Game;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainClass extends Application {
	public static void main(String[] args) {
		
		Game.getCurrentGame().getEventTrigger().setOnScoreChanged(score -> {
			System.err.println(score);
		});
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
