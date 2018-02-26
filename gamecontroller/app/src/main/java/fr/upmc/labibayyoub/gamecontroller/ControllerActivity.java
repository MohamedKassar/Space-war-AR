package fr.upmc.labibayyoub.gamecontroller;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ControllerActivity extends AppCompatActivity {

    private Socket sc;
    BufferedWriter out;
    BufferedReader in;
    public static final String START = "START";
    public static final String STOP = "STOP";
    public static final String PAUSE = "PAUSE";
    public static final String RIGHT = "R";
    public static final String LEFT = "L";
    public static final String S_STOP = "S STOP";
    public static final String SWITCH_G = "SWITCH G";
    public static final String SWITCH_D = "SWITCH D";
    public static final String GO = "GO";
    public static final String GG = "GG";
    public static final String SCORE = "SCORE";
    private  Button left;
    private Button right;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);
        try {
            if ((sc = Client.getSocket()) == null)
                finish();
            left = findViewById(R.id.left);
            right = findViewById(R.id.right);
            left.setOnTouchListener((v, e) -> {
                try {
                    return (onRelease(v, e));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                return true;
            });
            right.setOnTouchListener((v, e) -> {
                try {
                    return (onRelease(v, e));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                return true;
            });
            out = new BufferedWriter(new OutputStreamWriter(sc.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(sc.getInputStream()));
            sendCommands(START);
            receiveCommands();
        } catch (IOException e) {
            e.printStackTrace();
        }





    }

    private void receiveCommands() throws IOException {


        while(true){
            final String[] msg = new String[1];
            new Thread(() -> {
                try {
                    msg[0] = in.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
            switch (msg[0]){
                case SWITCH_D:
                    right.setEnabled(!right.isEnabled());
                case SWITCH_G:
                    left.setEnabled(!left.isEnabled());
                case GO :
                    showMessage("Game over");
            }
        }
    }

    public void showMessage(String msg) {
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
    }

    public boolean onRelease(View v, MotionEvent event) throws IOException {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            sendCommands(S_STOP);
        }
        return true;
    }


    public void disconnect(View v) throws IOException {
        sendCommands(STOP);
        if (sc != null) {
            sc.close();
        }
        finish();
    }

    public void sendCommands(String msg) throws IOException {
        new Thread(() -> {
            try {
                out.write(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        
    }

    @Override
    public void onBackPressed() {
        try {
            disconnect(new View(this.getBaseContext()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void turnRight() throws IOException {
        sendCommands(LEFT);
    }

    public void turnLeft() throws IOException {
        sendCommands(RIGHT);
    }
    
}
