package com.example.sijili;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;

public class WebSocketClients extends WebSocketClient {

    public WebSocketClients(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("WebSocket connection opened");
        // You can send login event or any other initialization logic here
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Received message: " + message);
        // Handle received messages from the server
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("WebSocket connection closed");
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }
}