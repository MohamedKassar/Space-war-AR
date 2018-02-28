package fr.upmc.labibayyoub.gamecontroller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private Socket sc = null;
    private String hostName;
    private int port;
    private String IP_REGULAR_EXPRESSION = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
    private Button connect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connect = findViewById(R.id.connect);
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            if (sc != null && sc.isConnected())
                sc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onStop();
    }

    public void onButtonClick(View v) {
        EditText et1 = findViewById(R.id.ipaddress);
        EditText et2 = findViewById(R.id.port);
        hostName = et1.getText().toString();
        if (hostName.isEmpty())
            showMessage("Ip address is empty");
        else if (et2.getText().toString().isEmpty())
            showMessage("Port is empty");
        else if (!MatchString(IP_REGULAR_EXPRESSION,hostName))
            showMessage("Ip address is invalid");
        else {
            port = Integer.parseInt(et2.getText().toString());
            connect.setEnabled(false);
            Thread serverThread = new Thread(new ConnexionThread());
            serverThread.start();
        }
    }

    class ConnexionThread implements Runnable {
        public void run() {
            try {

                sc = new Socket();
                 new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(() -> connect.setEnabled(true));
                    }
                }, 5000);
                sc.connect(new InetSocketAddress(hostName,port), 5000);
                Client.setSocket(sc);
                runOnUiThread(() -> {
                    Intent i = new Intent(MainActivity.this, ControllerActivity.class);
                    startActivity(i);
                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> showMessage("Couldn't establish connexion"));

            } finally {
                try {
                    if (sc != null && sc.isConnected())
                        sc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void showMessage(String msg) {
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
    }

    public static boolean MatchString(String regularExpression, String text) {
        Pattern p = Pattern.compile(regularExpression);
        Matcher m = p.matcher(text);
        return m.matches();
    }
}
