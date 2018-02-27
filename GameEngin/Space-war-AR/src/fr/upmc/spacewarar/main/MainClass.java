package fr.upmc.spacewarar.main;

import fr.upmc.spacewarar.engin.Game;

public class MainClass {
	public static void main(String[] args) {
		
		Game.getCurrentGame().getEventTrigger().setOnScoreChanged(score -> {
			System.err.println(score);
		});
	}
}
