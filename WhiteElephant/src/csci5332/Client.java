package csci5332;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
	int id;

	public Client(int id) {
		this.id = id;
	}

	public static void main(String[] args) throws Exception {
		Socket socket = new Socket("localhost", 5332);

		// Structures for receiving input from the server
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		// Structures for sending output to the server
		BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter out = new PrintWriter(socket.getOutputStream());

		String serverText;

		// GameLoop
		while (!Game.gameOver) {
			while (in.ready()) {
				serverText = in.readLine();

				if (serverText.startsWith("ID: ")) {
					int id = Integer.parseInt(serverText.split(" ")[1]);
					Client self = new Client(id);
				} else {
					System.out.println(serverText);
				}

				if (serverText.startsWith("Choose") || serverText.startsWith("Would") || serverText.startsWith("Who")
						|| serverText.startsWith("Enter")) {
					String responseText = userInput.readLine().trim();
					out.println(responseText);
					out.flush();
				}
			}
		}

		// Cleanup
		in.close();
		userInput.close();
		out.close();
		socket.close();
	}
}
