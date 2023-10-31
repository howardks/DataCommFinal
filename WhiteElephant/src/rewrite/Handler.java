package rewrite;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Handler extends Thread {
	Socket socket;
	
	public Handler(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			// Structures for sending output to the client
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

			// Structures for receiving input from the server
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			

			// Cleanup
			out.flush();
			out.close();
			in.close();
			socket.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
