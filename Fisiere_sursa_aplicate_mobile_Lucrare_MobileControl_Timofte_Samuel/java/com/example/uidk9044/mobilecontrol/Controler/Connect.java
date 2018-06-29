package com.example.uidk9044.mobilecontrol.Controler;

import android.os.Build;
import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by uidk9044 on 20-Sep-16.
 */
public class Connect {
    private String err = "Conected";
    private static Connect connection;
    private boolean conected = false;
    public static Connect getInstance(){
        if (connection != null){
            return connection;
        }
        else {
            connection = new Connect();
            return connection;
        }
    }

    public boolean IsConnected(){
        return conected;
    }

    public void Conecteaza(){
        conected = true;
    }

    public void Deconecteaza(){
        conected = false;
    }

    public WebSocketClient mWebSocketClient;

    public void connectWebSocket() {
        URI uri;
        try {
            uri = new URI("ws://192.168.42.1:8090");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri, new Draft_6455()) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("Websocket", "Opened");
                mWebSocketClient.send("Hello from " + Build.MANUFACTURER + " " + Build.MODEL);
                //MainActivity.afisText.setText("connected");
            }

            @Override
            public void onMessage(String s) {
                final String message = s;

            }

            @Override
            public void onClose(int i, String s, boolean b) {
                mWebSocketClient.close();
                Log.i("Websocket", "Closed " + s);
            }


            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
                err = e.getMessage();
                //afisText.setText("eroor");
            }
        };
        mWebSocketClient.connect();
    }
    public void close(WebSocketClient mWebSocketClient){
        mWebSocketClient.close();
    }
    public String getErr() {
        return err;
    }

    public void sendMessage(String s){
        mWebSocketClient.send(s);
    }

    public void setErr(String err) {
        this.err = err;
    }

//    public void sendMessage(View view) {
//        EditText editText = (EditText)findViewById(R.id.message);
//        mWebSocketClient.send(editText.getText().toString());
//        editText.setText("");
//    }
}
