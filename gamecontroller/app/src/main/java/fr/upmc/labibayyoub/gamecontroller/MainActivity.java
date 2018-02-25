package fr.upmc.labibayyoub.gamecontroller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private Client sc;
    private String hostName;
    private int port;
    private Boolean con = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
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
        else if (!isIpAddress(hostName))
            showMessage("Ip address is invalid");
        else {
            port = Integer.parseInt(et2.getText().toString());
            Thread serverThread = new Thread(new ConnexionThread());
            serverThread.start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    synchronized (con) {
                        try {
                            con.wait();
                            if (!con)
                                return;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(MainActivity.this, ControllerActivity.class);
                            i.putExtra("client", sc);
                            startActivity(i);
                        }
                    });
                }

            }).start();
        }
    }

    class ConnexionThread implements Runnable {
        public void run() {
            try {

                sc = new Client(hostName, port);
                synchronized (con) {
                    con = true;
                }

            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showMessage("Couldn't establish connexion");
                        synchronized (con) {
                            con.notifyAll();
                        }
                    }
                });

            }
        }
    }

    public void showMessage(String msg) {
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
    }

    public static boolean isIpAddress(String text) {
        Pattern p = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
        Matcher m = p.matcher(text);
        return m.find();
    }
}
