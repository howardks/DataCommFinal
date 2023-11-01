package rewrite;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
	public static void main(String[] args) throws Exception {
		Socket socket = new Socket("localhost", 5332);

		// Structures for receiving input from the server
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		// Structures for sending output to the server
		BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter out = new PrintWriter(socket.getOutputStream());

		// Perform login tasks
		String serverText = in.readLine();
		System.out.println(serverText);
		String name = userInput.readLine().trim();
		out.println(name);
		out.flush();
		serverText = in.readLine();
		System.out.println(serverText);
		String gift = userInput.readLine().trim();
		out.println(gift);
		out.flush();

		// GameLoop
		while (!Game.gameOver) {
			serverText = in.readLine();
			System.out.println(serverText);

			// Accepting input is not working here
			if (Game.wantsInput) {
				String responseText = userInput.readLine().trim();
				out.println(responseText);
				out.flush();

			}
		}

		// Cleanup
		in.close();
		userInput.close();
		out.close();
		socket.close();
	}
}
