package fr.upmc.spacewarar.engine.graphiccomponens;

import javafx.scene.layout.AnchorPane;

/**
 * 
 * @author Mohamed T. Kassar & Hakima Bouguetof
 *
 */
public abstract class GraphicComponent extends AnchorPane{
	/*
	 * TODO : maybe should extends a javaFX node !
	 */
	public abstract void step();
	
//	public abstract boolean collidesWith(GraphicComponent graphicComponent);
}
