package csci5332;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class Handler extends Thread {
	static int id;
	Socket socket;
	PrintWriter out;
	BufferedReader in;
	Player player;
	int myId;

	public Handler(Socket socket) throws IOException {
		this.socket = socket;
		myId = id;
		id++;
		// Structures for sending output to the client
		out = new PrintWriter(socket.getOutputStream(), true);

		// Structures for receiving input from the server
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		// Send Client its id
		out.println("ID: " + myId);
	}

	@Override
	public void run() {
		try {

			// Perform login tasks
			player = Game.login(out, in);

			while (Game.gifts.size() < Server.totalPlayers) {
				Server.sendMessage(String.format("Waiting on %d more players to join...", Server.totalPlayers - Game.players.size()));
				TimeUnit.SECONDS.sleep(5);
			}

			sendMessage("Starting game!");
			TimeUnit.SECONDS.sleep(1);

			// GameLoop
			while (!Game.gameOver) {
				Game.takeTurn(player, out, in);
			}

		} catch (Exception ex) {

		} finally {
			try {
				out.close();
				in.close();
			} catch (IOException e) {
			}
		}
	}

	public void sendMessage(String message) {
		out.println(message);
	}
}
