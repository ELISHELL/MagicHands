package magichands.core.tool.socket;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class webSocketClient extends WebSocketClient {

    private WebSocketListener listener;

    public interface WebSocketListener {
        void onConnected();
        void onMessageReceived(String message);
        void onDisconnected(int code, String reason, boolean remote);
        void onError(Exception ex);
    }

    public webSocketClient(URI serverURI, WebSocketListener listener) {
        super(serverURI);
        this.listener = listener;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        if (listener != null) {
            listener.onConnected();
        }
    }

    @Override
    public void onMessage(String message) {
        if (listener != null) {
            listener.onMessageReceived(message);
        }
    }


    public void sendMessageToServer(String message) {
        if (isOpen()) {
            send(message);
        }
    }
    @Override
    public void onClose(int code, String reason, boolean remote) {
        if (listener != null) {
            listener.onDisconnected(code, reason, remote);
        }
    }

    @Override
    public void onError(Exception ex) {
        if (listener != null) {
            listener.onError(ex);
        }
    }
}