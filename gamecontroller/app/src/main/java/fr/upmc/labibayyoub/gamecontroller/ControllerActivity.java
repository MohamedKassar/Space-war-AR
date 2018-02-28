package fr.upmc.labibayyoub.gamecontroller;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import java.net.Socket;

public class ControllerActivity extends AppCompatActivity {


    private Socket sc;
    BufferedWriter out;
    BufferedReader in;
    public static final String INTERNAL_ERROR = "Internal error";
    public static final String START = "START";
    public static final String STOP = "STOP";
    public static final String PAUSE = "PAUSE";
    private static final String RESUME = "RESUME";
    public static final String RIGHT = "R";
    public static final String LEFT = "L";
    public static final String S_STOP = "S STOP";
    public static final String SWITCH_G = "SWITCH G";
    public static final String SWITCH_D = "SWITCH D";
    public static final String GO = "GO";
    public static final String GG = "GG";
    public static final String SCORE = "SCORE";
    private String SCORE_REGULAR_EXPRESSION = "SCORE +[0-9]+";
    private String GG_REGULAR_EXPRESSION = "GG +[0-9]+";
    private Button left;
    private Button right;
    private Button pause;
    private TextView score_tv;
    private boolean onPause = false;

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
        left.setOnTouchListener((v, e) -> (onRelease(v, e, true)));
        right.setOnTouchListener((v, e) -> (onRelease(v, e, false)));
        new Thread(() -> {
            try {
                out = new BufferedWriter(new OutputStreamWriter(sc.getOutputStream()));
                in = new BufferedReader(new InputStreamReader(sc.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            runOnUiThread(() -> sendCommands(START));
            receiveCommands();


        }).start();


    }


    private void receiveCommands() {

        while (!sc.isClosed()) {
            final String msg;
            try {
                if ((msg = in.readLine()) == null)
                    return;
                switch (msg) {
                    case SWITCH_D:
                        runOnUiThread(() -> right.setEnabled(!right.isEnabled()));
                        break;
                    case SWITCH_G:
                        runOnUiThread(() -> left.setEnabled(!left.isEnabled()));
                        break;
                    case GO:
                        runOnUiThread(() -> showMessage("Game over"));
                        break;

                    default:
                        if (MainActivity.MatchString(SCORE_REGULAR_EXPRESSION, msg)
                                || MainActivity.MatchString(GG_REGULAR_EXPRESSION, msg)) {
                            score_tv.setText(msg.split(" ")[1]);
                            if (msg.split(" ")[0].equalsIgnoreCase(GG))
                                runOnUiThread(() -> showMessage("You won !!!!!"));
                        }
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void showMessage(String msg) {
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public boolean onRelease(View v, MotionEvent event, boolean l) {
        try {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                sendCommands(S_STOP);
            } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (l)
                    sendCommands(LEFT);
                else
                    sendCommands(RIGHT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showMessage(INTERNAL_ERROR);
        }
        return true;
    }


    public void disconnect(View v) {
        sendCommands(STOP);
        new Thread(() -> {
            try {
                Thread.sleep(500);
                if (sc != null) {
                    sc.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            runOnUiThread(() -> finish());
        }).start();


    }

    @Override
    protected void onDestroy() {


        super.onDestroy();
    }

    public void sendCommands(String msg) {
        showMessage(msg);
        new Thread(() -> {
            try {
                out.write(msg + "\n");
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> showMessage(INTERNAL_ERROR));

            }
        }).start();

    }

    @Override
    public void onBackPressed() {
        disconnect(new View(this.getBaseContext()));
    }

    public void pause(View view) {

        left.setEnabled(!left.isEnabled());
        right.setEnabled(!right.isEnabled());
        if (onPause) {
            sendCommands(START);
            pause.setText(PAUSE);
        } else {
            sendCommands(STOP);
            pause.setText(RESUME);
        }

        onPause = !onPause;
    }


}
