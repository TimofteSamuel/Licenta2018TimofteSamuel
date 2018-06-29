package com.example.uidk9044.mobilecontrol.Controler;

/**
 * Created by uidk9044 on 24-May-17.
 */
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;


public class Connect2 extends WebSocketClient {
    private String err = "Conected";
    private static Connect2 connection;
    private boolean conected = false;
    public static Connect2 getInstance(){
        if (connection != null){
            return connection;
        }
        else {
            //connection = new Connect2();
            return connection;
        }
    }
    public Connect2(URI serverUri, Draft draft) {
            super(serverUri, draft);
        }

        public Connect2(URI serverURI) {
            super(serverURI);
        }

        @Override
        public void onOpen(ServerHandshake handshakedata) {
            System.out.println("new connection opened");
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            System.out.println("closed with exit code " + code + " additional info: " + reason);
        }

        @Override
        public void onMessage(String message) {
            System.out.println("received message: " + message);
        }

        @Override
        public void onError(Exception ex) {
            System.err.println("an error occurred:" + ex);
        }
}
