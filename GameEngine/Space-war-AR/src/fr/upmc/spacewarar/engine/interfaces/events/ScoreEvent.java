package fr.upmc.spacewarar.engine.interfaces.events;

/**
 * 
 * @author Mohamed T. Kassar & Hakima Bouguetof
 *
 */
@FunctionalInterface
public interface ScoreEvent {
	void run(int score);
}
