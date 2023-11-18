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
	static int numSteals = 0;

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
			throws Exception {

		if (turn % players.size() == (players.indexOf(player))) {
			Server.sendMessage(String.format("Turn %d: player %s", turn - 3, player.getName()));
			
			if (Game.turn - 3 == 0 || Game.numSteals == 3) {
				Game.chooseGift(player, out, in);	
				Game.numSteals = 0;
			} else {
				String choice = "";
				
				while (!choice.equals("choose") && !choice.equals("steal")) {
					out.println("Would you like to choose a gift or steal a gift (input \"choose\" or \"steal\")?");
					choice = in.readLine().trim().toLowerCase();
					out.println(choice);
					
					switch (choice) {
					case "choose":
						Game.chooseGift(player, out, in);
						break;
					case "steal":
						Game.stealGift(player, out, in);
						break;
					default: 
						out.println("Please input \"choose\" or \"steal\"");
					}
				}
			}			
			
			turn++;
			TimeUnit.SECONDS.sleep(2);
		}

		if (turn == 10) {
			gameOver = true;
		}
	}

	public static void chooseGift(Player player, PrintWriter out, BufferedReader in) throws Exception {
		
		out.println("Choose a gift from the gift pile (input an integer between 0 and " + (gifts.size() - 1) + "): ");
		int giftNum = Integer.parseInt(in.readLine().trim());
		player.setCurrentGift(gifts.get(giftNum));
		Game.gifts.remove(giftNum);
		
		out.println(String.format("Player %s opened %s", player.getName(), player.getCurrentGift()));
	}
	
	public static void stealGift(Player player, PrintWriter out, BufferedReader in) throws Exception {
		out.println("You chose steal!");
		Game.numSteals++;
	}
}
