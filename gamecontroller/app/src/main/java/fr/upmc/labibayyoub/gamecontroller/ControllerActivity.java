package fr.upmc.labibayyoub.gamecontroller;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ControllerActivity extends AppCompatActivity {

    private Socket sc;
    BufferedWriter out;
    BufferedReader in;
    public static final String INTERNAL_ERROR = "Internal error";
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
    private String SCORE_REGULAR_EXPRESSION = "SCORE +[0-9]+";
    private String GG_REGULAR_EXPRESSION = "GG +[0-9]+";
    private Button left;
    private Button right;
    private Button pause;
    private TextView score_tv;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

            if ((sc = Client.getSocket()) == null)
                finish();
            left = findViewById(R.id.left);
            right = findViewById(R.id.right);
            pause = findViewById(R.id.pause);
            score_tv = findViewById(R.id.score);
            score_tv.setText("0");
            left.setOnTouchListener((v, e) ->  (onRelease(v, e)));
            right.setOnTouchListener((v, e) -> (onRelease(v, e)));
            new Thread(()->{
                try {
                    out = new BufferedWriter(new OutputStreamWriter(sc.getOutputStream()));
                    in = new BufferedReader(new InputStreamReader(sc.getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                runOnUiThread(()->{
                    sendCommands(START);
                    receiveCommands();
                });

            }).start();



    }

    private void receiveCommands()  {
        while (true) {
            final String[] msg = new String[1];
            new Thread(() -> {
                try {
                    msg[0] = in.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
            switch (msg[0]) {
                case SWITCH_D:
                    right.setEnabled(!right.isEnabled());
                case SWITCH_G:
                    left.setEnabled(!left.isEnabled());
                case GO:
                    showMessage("Game over");
                default:
                    if(MainActivity.MatchString(SCORE_REGULAR_EXPRESSION,msg[0])
                            || MainActivity.MatchString(GG_REGULAR_EXPRESSION,msg[0])) {
                        score_tv.setText(msg[0].split(" ")[1]);
                        if(msg[0].split(" ")[0].equalsIgnoreCase(GG))
                            showMessage("You won !!!!!");
                    }
            }
        }
    }

    public void showMessage(String msg) {
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
    }

    public boolean onRelease(View v, MotionEvent event)  {
        try {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                sendCommands(S_STOP);
            }
        }catch (Exception e){
            e.printStackTrace();
            showMessage(INTERNAL_ERROR);
        }
        return true;
    }


    public void disconnect(View v) {
        try {
            sendCommands(STOP);
            if (sc != null) {
                sc.close();
            }
            finish();
        }catch (Exception e){
            e.printStackTrace();
            showMessage(INTERNAL_ERROR);
        }
    }

    public void sendCommands(String msg)  {
        new Thread(() -> {
            try {
                out.write(msg);
            } catch (IOException e) {
                e.printStackTrace();
                showMessage(INTERNAL_ERROR);
            }
        }).start();

    }

    @Override
    public void onBackPressed() {
            disconnect(new View(this.getBaseContext()));
    }

    public void turnRight(View view) {
        sendCommands(LEFT);
    }

    public void turnLeft(View view)  {
            sendCommands(RIGHT);
    }

    public void pause(View view)  {
            sendCommands(PAUSE);
            left.setEnabled(false);
            right.setEnabled(false);
            pause.setText("resume");
    }


}
