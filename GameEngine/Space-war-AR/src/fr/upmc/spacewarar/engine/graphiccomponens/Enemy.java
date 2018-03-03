package fr.upmc.spacewarar.engine.graphiccomponens;

import java.io.File;

import fr.upmc.spacewarar.engine.Engine;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Point2D;

/**
 * 
 * @author Mohamed T. Kassar & Hakima Bouguetof
 *
 */
public class Enemy extends GraphicComponent {

	private final SimpleIntegerProperty lifeProperty = new SimpleIntegerProperty(50);

	public Enemy(Point2D point) {
		setLayoutX(point.getX() - 50);
		setLayoutY(point.getY() - 100);

		setPrefSize(150, 90);
		setStyle("-fx-background-image: url('file:///"
				+ new File("resources/enemy01.gif").getAbsolutePath().replace("\\", "/")
				+ "');-fx-background-size: stretch;");
	}

	private int lastMoveSwitch = Engine.FPS;
	private boolean dir = false;

	public void decreaseLifeBy(int p) {
		lifeProperty.set(lifeProperty.get() - p);
	}

	public boolean isDead() {
		return lifeProperty.get() <= 0;
	}
	
	

	@Override
	public void step() {
		if (lastMoveSwitch >= Engine.FPS * 2) {
			dir = !dir;
			lastMoveSwitch = 0;
		}
		setLayoutX(getLayoutX() + 2 * (dir ? 1 : -1));
		lastMoveSwitch++;
	}

}
