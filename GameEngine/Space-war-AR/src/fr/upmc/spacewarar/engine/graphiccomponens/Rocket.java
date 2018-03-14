package fr.upmc.spacewarar.engine.graphiccomponens;

import java.io.File;


/**
 * 
 * @author Mohamed T. Kassar & Hakima Bouguetof
 *
 */
public class Rocket extends GraphicComponent {

	public enum Direction {
		UP, DOWN
	}

	private final Direction direction;
	private final int power;

	public Rocket(double x, double y, Direction direction, int power) {
		this.direction = direction;
		this.power = power;
		setLayoutX(x);
		setLayoutY(y);
		
		setPrefSize(30, 60);
//		setStyle("-fx-background-radius: 25; -fx-background-color: black;");
		setStyle("-fx-background-image: url('file:///"
				+ new File("resources/rocketb"+direction+".gif").getAbsolutePath().replace("\\", "/")
				+ "');-fx-background-size: stretch;");
	}

	@Override
	public void step() {
		switch (direction) {
		case UP:
			setLayoutY(getLayoutY() - 10);
			break;
		case DOWN:
			setLayoutY(getLayoutY() + 10);
			break;
		}
	}

	public boolean collidesWith(GraphicComponent graphicComponent) {
		return graphicComponent.getBoundsInParent().intersects(getBoundsInParent());
	}

	public boolean isEnemyRocket() {
		return direction == Direction.DOWN;
	}

	public int getPower() {
		return this.power;
	}

}
