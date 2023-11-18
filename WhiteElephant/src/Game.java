

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Game {
	static ArrayList<User> users = new ArrayList<>();
	static ArrayList<String> gifts = new ArrayList<>();
	static int turn = 0;
	static boolean gameOver = false;
	static String action;
	static int numSteals = 0;
	
	public static User login(PrintWriter out, BufferedReader in) throws Exception {
		// Send output to client
		out.println("Enter name: ");

		// Read and display input data from client
		String name = in.readLine();
		User user = new User(name);
		Game.users.add(user);
		
		// Send output to client
		out.println("Enter gift: ");

		// Read and display input data from client
		String gift = in.readLine();
		Game.gifts.add(gift);

		return user;
	}
	
	public static synchronized void takeTurn(User user, PrintWriter out, BufferedReader in) throws Exception {
		if (Game.turn == 0 || Game.numSteals == 3) {
			Game.chooseGift(user, out, in);	
			Game.numSteals = 0;
		} else {
			Game.action = "actionChoice";
			String choice = "";
			
			while (!choice.equals("choose") || !choice.equals("steal")) {
				out.println("Would you like to choose a gift or steal a gift (input \"choose\" or \"steal\")?");
				choice = in.readLine().trim().toLowerCase();
				
				switch (choice) {
				case "choose":
					Game.chooseGift(user, out, in);
					break;
				case "steal":
					Game.stealGift(user, out, in);
					break;
				default: 
					out.println("Please input \"choose\" or \"steal\"");
				}
			}
		}
		
		turn++;
	}

	public static void chooseGift(User user, PrintWriter out, BufferedReader in) throws Exception {
		Game.action = "giftChoice";
		out.println("Choose a gift from the gift pile (input an integer between 0 and " + (gifts.size() - 1) + "): ");
		
		// TODO: Accepting input is not working here
		int giftNum = Integer.parseInt(in.readLine().trim());
		user.setCurrentGift(gifts.get(giftNum));
		Game.gifts.remove(giftNum);
		
		// Broadcast gift to all clients
		Server.broadcastMessage(user.getName() + " opened " + user.getCurrentGift());
	}
	
	public static void stealGift(User user, PrintWriter out, BufferedReader in) throws Exception {
		out.println("You chose steal!");
		Game.numSteals++;
	}
}
