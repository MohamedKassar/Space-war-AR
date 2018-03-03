package fr.upmc.labibayyoub.gamecontroller;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

/**
 * Created by labib on 25/02/2018.
 */

public  class Client {

    private static Socket socket;

    public static synchronized Socket getSocket(){
        return socket;
    }

    public static synchronized void setSocket(Socket socket){
        Client.socket = socket;
    }

}
