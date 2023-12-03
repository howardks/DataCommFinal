package csci5332;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	static ArrayList<Handler> clients = new ArrayList<>();
	static int totalPlayers = 4; // Change this variable to change the number of players

	public static void main(String[] args) {
		// Run the server
		try (ServerSocket server = new ServerSocket(5332)) {
			System.out.println("White Elephant server is running on port 5332...");

			while (clients.size() < Server.totalPlayers) {
				Socket socket = server.accept();
				Handler handler = new Handler(socket);
				clients.add(handler);
				System.out.println(String.format("Client %d connected.", handler.myId));
				handler.start();
			}

		} catch (IOException ex) {
			System.out.println("There was an input/output error");
		} catch (Exception ex) {
			System.out.println("There was an error");
		}
	}

	public static void sendMessage(String message) {
		// Send message to all clients
		for (Handler client : clients) {
			client.sendMessage(message);
		}
	}
}
