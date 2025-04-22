package com.github.crafterchen2.logoanim.remote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ServerConnection {

    private final ServerSocket serverSocket;
    private final RequestHandler requestHandler;
    private final Thread thread = new Thread(this::mainLoop);

    public ServerConnection(int port, RequestHandler requestHandler) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.requestHandler = requestHandler;
        serverSocket.setSoTimeout(1000);
    }

    public ServerConnection(RequestHandler requestHandler) throws IOException {
        this(NetworkingDetails.PORT, requestHandler);
    }

    public void close() throws IOException {
        if (thread.isAlive()) {
            thread.interrupt();
        }
        while (thread.isAlive()); // Wait for the thread to exit
        serverSocket.close();
    }

    private void mainLoop() {
        try {
            while (!thread.isInterrupted()) {
                try {
                    Socket client = serverSocket.accept();
                    if (NetworkingDetails.isInWhitelist(client.getInetAddress().toString())) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                        int version = Integer.parseInt(in.readLine());
                        if (version == NetworkingDetails.VERSION) {
                            String alias = in.readLine();
                            String message = in.readLine();
                            String response = requestHandler.handleMessage(new SocketInfo(client, alias), message);
                            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                            out.println(response);
                            out.close();
                        } else {
                            requestHandler.handleRefuse(new SocketInfo(client, null), RefuseReason.INCORRECT_VERSION);
                        }
                        in.close();
                        client.close();
                    } else {
                        requestHandler.handleRefuse(new SocketInfo(client, null), RefuseReason.NOT_WHITELISTED);
                    }
                } catch (SocketTimeoutException _) {
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
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

    public interface RequestHandler {

        String handleMessage(SocketInfo socket, String msg);
        void handleRefuse(SocketInfo socket, RefuseReason reason);

    }

    public enum RefuseReason {
        INCORRECT_VERSION, NOT_WHITELISTED
    }

}
