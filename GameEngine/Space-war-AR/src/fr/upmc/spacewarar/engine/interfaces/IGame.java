package fr.upmc.spacewarar.engine.interfaces;

/**
 * 
 * @author Mohamed T. Kassar & Hakima Bouguetof
 *
 */
public interface IGame {

	IEventTrigger getEventTrigger();

	IGameController getGameController();

}