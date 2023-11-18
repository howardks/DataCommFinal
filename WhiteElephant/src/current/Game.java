package current;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Game {
	static ArrayList<Player> players = new ArrayList<>();
	static ArrayList<String> gifts = new ArrayList<>();
	static boolean gameOver = false;
	static int turn = 3;

	public static Player login(PrintWriter out, BufferedReader in) throws Exception {
		// Send output to client
		out.println("Enter name: ");

		// Read and display input data from client
		String name = in.readLine();
		Player user = new Player(name);
		Game.players.add(user);

		// Send output to client
		out.println("Enter gift: ");

		// Read and display input data from client
		String gift = in.readLine();
		Game.gifts.add(gift);

		return user;
	}

	public static synchronized void takeTurn(Player player, PrintWriter out, BufferedReader in)
			throws InterruptedException {
		//Server.sendMessage(""+turn);
		if (turn % players.size() == (players.indexOf(player))) {
			Server.sendMessage(String.format("Turn %d: player %s", turn, player.getName()));
			turn++;
			TimeUnit.SECONDS.sleep(2);
		}

		if (turn == 10) {
			gameOver = true;
		}
	}

}
