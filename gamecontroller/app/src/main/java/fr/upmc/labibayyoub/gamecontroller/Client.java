package fr.upmc.labibayyoub.gamecontroller;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

/**
 * Created by labib on 25/02/2018.
 */

public class Client extends Socket implements Serializable {


    public Client(String host, int port) throws IOException {
        super(host, port);
    }


}
