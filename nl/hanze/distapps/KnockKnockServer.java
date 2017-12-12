package nl.hanze.distapps;

import java.net.*;
import java.io.*;

public class KnockKnockServer implements Runnable {
	ServerSocket serverSocket = null;
	Socket clientSocket = null;
	KnockKnockProtocol kkp = null;
	
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
		serverSocket.close();
	}
	
	private void makeListener() {
        try {
            System.out.println("Server waiting connections on port " +Settings.PORT_NUM);
            this.serverSocket = new ServerSocket(Settings.PORT_NUM);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " +Settings.PORT_NUM);
            System.exit(1);
        }
	}
	
	public void run() {
		System.out.println("Client connected in thread: " + Thread.currentThread().getName());
		kkp = new KnockKnockProtocol();
	
        try {
	       handleRequests();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void accept() {
        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }
	}
	
	public void start () {
		KnockKnockServer server = new KnockKnockServer();
		server.makeListener();
			while(true) {
				server.accept();
				Thread thread = new Thread(server);
				thread.start();
			}
	   }
	
    public static void main(String[] args) throws IOException {
    	new KnockKnockServer().start();
    }
}
