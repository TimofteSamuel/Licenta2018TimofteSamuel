package com.example.uidk9044.mobilecontrol.Activitys;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.uidk9044.mobilecontrol.Controler.Connect;
import com.example.uidk9044.mobilecontrol.Controler.Functionality;
import com.example.uidk9044.mobilecontrol.R;

import java.util.Timer;
import java.util.TimerTask;



public class StaticMenuActivity extends AppCompatActivity {
    public Connect conn =  Connect.getInstance();
    public TextView text;
    public TextView connect_text_id;
    public static Timer timer = new Timer();
    public  TimerTask functie = new MyTimerTask();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.static_menu);
        //try to connect
        if (conn.IsConnected() == false){
            conn.connectWebSocket();
        }
//        conn.mWebSocketClient.close();
        //if error tot connect
        connect_text_id = (TextView) findViewById(R.id.connect_text_id);
        if (conn.getErr().equals("Conected")){
            //connect_text_id.setText("Connected");
            conn.Conecteaza();
            Log.i("cenect","conectat");
        }
        else {
            connectionError();
            Log.i("cenect","neconect");
        }
        TrimiteMesajCiclic();
    }

    public void connectionError()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(StaticMenuActivity.this,R.style.MyDialogTheme);
        builder.setMessage("You have to try to reconnect")
                .setTitle("Error in connection");
        builder.setPositiveButton("Reconnect", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                reconnect();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public  void TrimiteMesajCiclic(){
        Functionality.setMesage();
        timer.scheduleAtFixedRate(functie, 100, 100);
    }
    public  void OpresteTrimiterea(){
        timer.cancel();
    }


    class MyTimerTask extends TimerTask {
        public void run() {
            StaticMenuActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    conn.sendMessage(Functionality.mesage1);
                    conn.sendMessage(Functionality.mesage2);
                }
            });

        }
    }

    public void reconnect(){
        conn.connectWebSocket();
        connect_text_id = (TextView) findViewById(R.id.connect_text_id);
        if (conn.getErr().equals("Conneced")){
            connect_text_id.setText("Connected");
        }
        else {
            connect_text_id.setText("Error in conection");
            AlertDialog.Builder builder = new AlertDialog.Builder(StaticMenuActivity.this,R.style.MyDialogTheme);
            builder.setMessage("Problem at the server")
                    .setTitle("Error in reconnection");
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        conn.close(conn.mWebSocketClient);
        conn.mWebSocketClient.close();
    }


    @Override
    public void onPause() {
        super.onPause();
        conn.close(conn.mWebSocketClient);
        conn.mWebSocketClient.close();
    }

    @Override
    public void onStop() {
        super.onStop();
        conn.close(conn.mWebSocketClient);
        conn.mWebSocketClient.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.static_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.connect_text_id:
                return true;
            case R.id.verify_conn_id:
                verifyConn();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void verifyConn(){
        connect_text_id = (TextView) findViewById(R.id.connect_text_id);
        if (conn.getErr().equals("Conected")){
            connect_text_id.setText("Connected");
            //afiseaza conectat
        }
        else {
            connect_text_id.setText("Error in conection");
            connectionError();
        }
    }

    public void tryConnection(){
        //try to connect
        conn.connectWebSocket();
        connect_text_id = (TextView) findViewById(R.id.connect_text_id);
        if (conn.getErr().equals("Conneced")){
            connect_text_id.setText("Connected");
            connect_text_id.setText("Conneced");
        }
        else {
            connect_text_id.setText("Error in conection");
        }
    }
}

