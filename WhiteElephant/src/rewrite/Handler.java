package rewrite;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Handler extends Thread {
	static int id;
	Socket socket;
	User user;
	PrintWriter out;
	int myId;
	
	
	public Handler(Socket socket) {
		this.socket = socket;
		myId = id;
		id++;
	}

	public void run() {
		try {
			// Structures for sending output to the client
			out = new PrintWriter(socket.getOutputStream(), true);

			// Structures for receiving input from the server
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			// Perform login tasks
			user = Game.login(out, in);
			
			// GameLoop
			while (!Game.gameOver) {
				if (myId == Game.turn) {
					Game.takeTurn(user, out, in);
				}
			}

			// Cleanup
			out.flush();
			out.close();
			in.close();
			socket.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void sendMessage(String message) {
		out.println(message);
	}
}
