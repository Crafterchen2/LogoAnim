package com.github.crafterchen2.logoanim.remote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class ServerConnection {

    private final ServerSocket serverSocket;
    private final ArrayList<MessageListener> messageListeners = new ArrayList<>();
    private final ArrayList<RefuseListener> refuseListeners = new ArrayList<>();
    private final Thread thread = new Thread(this::mainLoop);

    public ServerConnection(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(1);
    }

    public void close() throws IOException {
        if (thread.isAlive()) {
            thread.interrupt();
        }
        while (thread.isAlive()) {
        }
        serverSocket.close();
    }

    public void addMessageListener(MessageListener listener) {
        messageListeners.add(listener);
    }

    public void addRefuseListener(RefuseListener listener) {
        refuseListeners.add(listener);
    }

    private void mainLoop() {
        try {
            while (!thread.isInterrupted()) {
                try {
                    Socket client = serverSocket.accept();
                    if (ConnectionManager.isInWhitelist(client.getInetAddress().toString())) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                        int version = Integer.parseInt(in.readLine());
                        String alias = in.readLine();
                        String message = in.readLine();
                        if (version == ConnectionManager.VERSION) {
                            for (MessageListener listener : messageListeners) {
                                listener.handleMessage(new SocketInfo(client, alias), message);
                            }
                        }else {
                            for (RefuseListener listener : refuseListeners) {
                                listener.handleRefuse(new SocketInfo(client, alias), RefuseReason.INCORRECT_VERSION);
                            }
                        }
                        client.close();
                    } else {
                        for (RefuseListener listener : refuseListeners) {
                            listener.handleRefuse(new SocketInfo(client,null), RefuseReason.NOT_WHITELISTED);
                        }
                    }
                } catch (SocketTimeoutException timeoutException) {
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() throws RuntimeException {
        if (thread.isAlive()) {
            throw new RuntimeException("Thread already running");
        } else {
            thread.start();
        }
    }

    public record SocketInfo(String ip, int port, String alias) {

        public SocketInfo(Socket socket, String alias) {
            this(socket.getInetAddress().toString(), socket.getPort(), alias);
        }

    }

    public interface MessageListener {
        void handleMessage(SocketInfo socket, String msg);
    }

    public interface RefuseListener {
        void handleRefuse(SocketInfo socket, RefuseReason reason);
    }

    public enum RefuseReason {
        INCORRECT_VERSION,
        NOT_WHITELISTED
    }

}
