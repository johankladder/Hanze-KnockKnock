package nl.hanze.distapps;

import java.net.*;
import java.io.*;

public class KnockKnockServer {
	ServerSocket serverSocket = null;
	Socket clientSocket = null;
	KnockKnockProtocol kkp = null;
	
	public KnockKnockServer() {
		makeListener();
		kkp = new KnockKnockProtocol();

        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }
        
        try {
	       handleRequests();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		serverSocket.close();
	}
	
	private void makeListener() {
        try {
            serverSocket = new ServerSocket(Settings.PORT_NUM);
            System.out.println("Server waiting connections on port " +Settings.PORT_NUM);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " +Settings.PORT_NUM);
            System.exit(1);
        }
	}
	
	
    public static void main(String[] args) throws IOException {
    	new KnockKnockServer();
    }
}
