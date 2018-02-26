package fr.upmc.labibayyoub.gamecontroller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.IOException;
import java.net.Socket;

public class ControllerActivity extends AppCompatActivity {

    private Socket sc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);
        Intent i = getIntent();
        sc = (Socket)i.getSerializableExtra("socket");

    }

    public void disconnect(View v) throws IOException {
       if(sc != null ){
           sc.close();
       }
        finish();
    }

    public void turnRight(){

    }

    public void  turnLeft(){

    }
}
