package csci5332;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Game {
	static ArrayList<Player> players = new ArrayList<>();
	static ArrayList<String> playerNames = new ArrayList<>();
	static ArrayList<String> gifts = new ArrayList<>();
	static boolean gameOver = false;
	static int currentPlayerNum = 0;
	static int turn = 0;
	static int numSteals = 0;
	static String lastStolen;

	public static Player login(PrintWriter out, BufferedReader in) throws Exception {
		// Send output to client
		out.println("Welcome to White Elephant!");
		out.println("Enter name: ");

		// Read and display input data from client
		String name = in.readLine().toLowerCase();
		while (playerNames.contains(name)) {
			out.println("Name already in use, choose a different name");
			out.println("Enter name: ");
			name = in.readLine().toLowerCase();
		}
		playerNames.add(name);
		Player player = new Player(name);
		Game.players.add(player);

		// Send output to client
		out.println("Enter gift: ");

		// Read and display input data from client
		String gift = in.readLine();
		Game.gifts.add(gift);

		return player;
	}

	public static synchronized void takeTurn(Player player, PrintWriter out, BufferedReader in) throws Exception {
		// Only run while game is in progress
		if (!checkGameOver(out)) {
			// Only run for the player whose turn it is
			if (currentPlayerNum == players.indexOf(player)) {
				// Only run if this player does not have a gift
				if (player.getCurrentGift().equals("")) {
					Server.sendMessage(String.format("\nTurn %d: Player %s", turn, player.getName()));

					if (Game.turn == 0 || Game.numSteals == 3 || Game.checkStealableGifts(player) == 0) {
						// Player must choose a gift if it is the first turn, if numSteals is maxed out, 
						// or if there are no players a gift can be stolen from
						Game.chooseGift(player, out, in);
					} else {
						String choice = "";

						// Let player choose or steal a gift
						while (!choice.equals("choose") && !choice.equals("steal")) {
							out.println(
									"Would you like to choose a gift or steal a gift (input \"choose\" or \"steal\")?");
							choice = in.readLine().trim().toLowerCase();

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
				} else {
					// Move to next player if conditions are not met
					Game.currentPlayerNum = (Game.currentPlayerNum == Game.players.size() - 1) ? 0
							: Game.currentPlayerNum + 1;
				}
			}
		}
	}

	public static void chooseGift(Player player, PrintWriter out, BufferedReader in) throws Exception {
		// Let player choose a gift or switch to steal if possible
		String choiceStr = "Choose a gift from the gift pile (input an integer between 0 and " + (gifts.size() - 1)
				+ ((turn == 0 || Game.numSteals == 3 || Game.checkStealableGifts(player) == 0) ? "): "
						: " or \"steal\" to steal a gift instead): ");
		out.println(choiceStr);

		String giftStr = in.readLine().toLowerCase().trim();

		if (turn != 0 && Game.numSteals < 3 && Game.checkStealableGifts(player) > 0 && giftStr.equals("steal")) {
			// Let player switch to steal if conditions are met
			Game.stealGift(player, out, in);
		} else {
			// Allow player to choose their gift by index
			int giftNum = -1;
			while ((giftNum < 0 || giftNum > gifts.size() - 1)) {
				if (giftStr.matches("-?\\d+")) {
					giftNum = Integer.parseInt(giftStr);

					if (giftNum < 0 || giftNum > gifts.size() - 1) {
						out.println("Input an integer between 0 and " + (gifts.size() - 1));
						out.println(choiceStr);
						giftStr = in.readLine().toLowerCase().trim();
						Game.lastStolen = "";
					}
				} else if (turn != 0 && Game.numSteals < 3 && Game.checkStealableGifts(player) > 0
						&& giftStr.equals("steal")) {
					Game.stealGift(player, out, in);
				} else {
					out.println("Input an integer between 0 and " + (gifts.size() - 1));
					out.println(choiceStr);
					giftStr = in.readLine().toLowerCase().trim();
				}
			}
			player.setCurrentGift(gifts.get(giftNum));
			Game.gifts.remove(giftNum);

			// Send message output to all clients and move to next player
			Server.sendMessage(
					String.format("Player %s opened a gift containing %s", player.getName(), player.getCurrentGift()));
			Game.numSteals = 0;
			Game.currentPlayerNum = (Game.currentPlayerNum == Game.players.size() - 1) ? 0 : Game.currentPlayerNum + 1;
		}
	}

	public static void stealGift(Player player, PrintWriter out, BufferedReader in) throws Exception {
		ArrayList<String> stealablePlayerNames = printStealableGifts(player, out);

		// Let player steal a gift or switch to choose
		out.println(
				"Who would you like to steal from? (input the player's name or \"choose\" to choose a gift instead): ");
		String victimName = in.readLine().trim().toLowerCase();

		// Do not let player steal from invalid other player
		while ((victimName.equals(lastStolen) || !stealablePlayerNames.contains(victimName))
				&& !victimName.equals("choose")) {
			out.println("You cannot steal from " + victimName);
			out.println(
					"Who would you like to steal from? (input the player's name or \"choose\" to choose a gift instead): ");
			victimName = in.readLine().trim().toLowerCase();
		}

		if (victimName.equals("choose")) {
			// Let player switch to choose
			Game.chooseGift(player, out, in);
		} else {
			// Allow player to steal a gift by other player's name
			int index = -1;

			while (index == -1) {
				for (Player victim : players) {
					if (victim.getName().toLowerCase().equals(victimName)) {
						index = players.indexOf(victim);
					}
				}

				if (index != -1) {
					player.setCurrentGift(Game.players.get(index).getCurrentGift());
					Game.players.get(index).setCurrentGift("");
					Server.sendMessage(String.format("Player %s stole Player %s's %s!", player.getName(), victimName,
							player.getCurrentGift()));
					Game.lastStolen = player.getName();
					Game.currentPlayerNum = index;
				} else {
					out.println("Please input the player's name: ");
				}
			}

			Game.numSteals++;
		}
	}

	public static boolean checkGameOver(PrintWriter out) {
		// Check if all players have a gift
		// If so, the game ends
		for (Player player : Game.players) {
			if (player.getCurrentGift().equals("")) {
				return false;
			}
		}

		out.println();
		out.println("Results: ");
		printGifts(out);
		out.println("Thank you for playing White Elephant!");
		Game.gameOver = true;
		return true;
	}

	public static int checkStealableGifts(Player player) {
		// Check if there are any players that the current player
		// can steal from
		int count = 0;

		for (Player victim : Game.players) {
			if (!victim.getCurrentGift().equals("") && !victim.getName().equals(Game.lastStolen)
					&& !player.getName().equals(victim.getName())) {
				count++;
			}
		}

		return count;
	}

	public static ArrayList<String> printStealableGifts(Player player, PrintWriter out) {
		// Print other players and their gifts that can be stolen
		ArrayList<String> stealablePlayerNames = new ArrayList<>();

		for (Player victim : Game.players) {
			if (!victim.getCurrentGift().equals("") && !victim.getName().equals(Game.lastStolen)
					&& !player.getName().equals(victim.getName())) {
				out.println(String.format("Player %s's gift: %s", victim.getName(), victim.getCurrentGift()));
				stealablePlayerNames.add(victim.getName());
			}
		}

		return stealablePlayerNames;
	}

	public static void printGifts(PrintWriter out) {
		// Print all players and their gifts
		for (Player player : Game.players) {
			if (!player.getCurrentGift().equals("")) {
				out.println(String.format("Player %s's gift: %s", player.getName(), player.getCurrentGift()));
			}
		}
	}
}
