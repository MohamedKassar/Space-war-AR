package fr.upmc.spacewarar.engine;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import fr.upmc.spacewarar.engine.interfaces.IGameController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * 
 * @author Mohamed T. Kassar & Hakima Bouguetof
 *
 */
class Engine implements IGameController {
	/*
	 * TODO graphic components here
	 * 
	 * connect GameController with engine
	 */

	private static final int FPS = 60;
	private final Timeline scheduler;

	private boolean started = false;
	private boolean paused = false;

	protected Engine() {
		scheduler = new Timeline(new KeyFrame(Duration.millis(1000 / FPS), e -> step()));
	}

	protected void step() {
		// TODO : execute step method of each component
	}

	@Override
	public void stop() {
		paused = false;
		started = false;
		scheduler.stop();
	}

	@Override
	public void pause() {
		scheduler.pause();
		paused = true;
	}

	@Override
	public void start() {
		if (!started)
			started = true;
		if (paused)
			paused = false;
		
		scheduler.play();
	}

	@Override
	public void shoot() {
		// TODO
	}
}
