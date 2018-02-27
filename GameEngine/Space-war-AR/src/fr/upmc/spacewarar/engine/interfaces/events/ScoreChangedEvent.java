package fr.upmc.spacewarar.engine.interfaces.events;

/**
 * 
 * @author Mohamed T. Kassar & Hakima Bouguetof
 *
 */
@FunctionalInterface
public interface ScoreChangedEvent {
	void run(int score);
}
