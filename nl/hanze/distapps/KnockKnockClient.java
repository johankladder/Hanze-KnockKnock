package nl.hanze.distapps;

import java.io.*;
import java.net.*;

public class KnockKnockClient {
	Socket kkSocket = null;
	PrintWriter out = null;
	BufferedReader in = null;
	
	public KnockKnockClient() {
		makeConnection();
		
		try {
			handleRequests();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void makeConnection() {
		try {
			kkSocket = new Socket(Settings.HOSTNAME, Settings.PORT_NUM);
			out = new PrintWriter(kkSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: " +Settings.HOSTNAME);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: "+Settings.HOSTNAME);
			System.exit(1);
		}
	}
	
	private void handleRequests() throws IOException {
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		String fromServer;
		String fromUser;

		while ((fromServer = in.readLine()) != null) {
			System.out.println("Server: " + fromServer);
			if (fromServer.equals("Bye."))
				break;

			fromUser = stdIn.readLine();
			if (fromUser != null) {
				System.out.println("Client: " + fromUser);
				out.println(fromUser);
			}
		}

		out.close();
		in.close();
		stdIn.close();
		kkSocket.close();
	}
	
	
	
	public static void main(String[] args) throws IOException {
		new KnockKnockClient();

	}
}
