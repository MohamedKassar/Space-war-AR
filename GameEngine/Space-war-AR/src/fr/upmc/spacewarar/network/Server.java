/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package fr.upmc.spacewarar.network;

import fr.upmc.spacewarar.engine.Game;
import fr.upmc.spacewarar.engine.interfaces.IEventTrigger;
import fr.upmc.spacewarar.engine.interfaces.IGameController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
    private int serverPort;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private IEventTrigger trigger;
    private IGameController controller;

    public Server(int port) {
        this.serverPort = port;
        this.trigger = Game.getCurrentGame().getEventTrigger();
        this.controller = Game.getCurrentGame().getGameController();

    }

    @Override
    public void run() {
        getClient();

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintStream out = new PrintStream(clientSocket.getOutputStream());

            trigger.setOnGameOver(() -> out.println("GO"));
            trigger.setOnScoreChanged((score) -> out.println("SCORE " + score));
            trigger.setOnGameWinning(score -> out.println("GG " + score));
            trigger.setOnLeftCollision(() -> out.println("SWITCH G"));
            trigger.setOnRightCollision(() -> out.println("SWITCH D"));

            String line;

            while ((line = in.readLine()) != null) {
                switch (line) {
                    case "START":
                        controller.start();
                        break;
                    case "STOP":
                        controller.stop();
                        break;
                    case "PAUSE":
                        controller.pause();
                        break;
                    case "SHOOT":
                        controller.shoot();
                        break;
                    case "D":
                        break;
                    case "G":
                        break;
                    case "S":
                        break;

                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error opening stream", e);
        }
    }

    private void getClient() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + serverPort, e);
        }
        try {
            this.clientSocket = this.serverSocket.accept();
        } catch (IOException e) {
            throw new RuntimeException("Error accepting client connection", e);
        }
        System.out.println("New client, address " + clientSocket.getInetAddress() + " on " + clientSocket.getPort() + ".");
    }

}
