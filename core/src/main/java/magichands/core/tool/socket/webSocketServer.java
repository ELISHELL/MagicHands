package magichands.core.tool.socket;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class webSocketServer extends WebSocketServer {
    private WebSocketListener listener;

    public interface WebSocketListener {
        void onConnected(WebSocket conn);
        void onMessageReceived(WebSocket conn, String message);
        void onDisconnected(WebSocket conn, int code, String reason, boolean remote);
        void onError(WebSocket conn, Exception ex);
    }

    public webSocketServer(int port, WebSocketListener listener) {
        super(new InetSocketAddress(port));
        this.listener = listener;
    }

    @Override
    public void onStart() {
        System.out.println("WebSocket Server started");
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        if (listener != null) {
            listener.onConnected(conn);
        }
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        if (listener != null) {
            listener.onDisconnected(conn, code, reason, remote);
        }
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        if (listener != null) {
            listener.onMessageReceived(conn, message);
            // 在这里可以向客户端发送回应消息
            // conn.send("Server response: " + message);
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        if (listener != null) {
            listener.onError(conn, ex);
        }
    }

}