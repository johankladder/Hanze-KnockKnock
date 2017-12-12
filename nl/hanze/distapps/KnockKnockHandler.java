package nl.hanze.distapps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class KnockKnockHandler implements Runnable {

    private Socket clientSocket = null;
    private KnockKnockProtocol kkp = null;

    public KnockKnockHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    private void handleRequests() throws IOException {
        String inputLine;
        String outputLine;
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        clientSocket.getInputStream()));


        outputLine = kkp.processInput(null);
        out.println(outputLine);

        while ((inputLine = in.readLine()) != null) {
            outputLine = kkp.processInput(inputLine);
            out.println(outputLine);
            if (outputLine.equals("Bye."))
                break;
        }
        out.close();
        in.close();
        clientSocket.close();
    }

    @Override
    public void run() {
        System.out.println("Client connected in thread: " + Thread.currentThread().getName());
        kkp = new KnockKnockProtocol();
        try {
            handleRequests();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
