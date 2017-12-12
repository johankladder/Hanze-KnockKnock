package nl.hanze.distapps;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class KnockKnockServer {
    private ServerSocket serverSocket = null;

    private void makeListener() {
        try {
            System.out.println("Server waiting connections on port " + Settings.PORT_NUM);
            this.serverSocket = new ServerSocket(Settings.PORT_NUM);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + Settings.PORT_NUM);
            System.exit(1);
        }
    }

    public Socket waitForClient() {
        try {
            return serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }
        return null;
    }

    public void start() {
        KnockKnockServer server = new KnockKnockServer();
        server.makeListener();
        while (true) {
            Socket clientSocket = server.waitForClient();
            Thread thread = new Thread(new KnockKnockHandler(clientSocket));
            thread.start();
        }
    }

    public static void main(String[] args) throws IOException {
        new KnockKnockServer().start();
    }
}
