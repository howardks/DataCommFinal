package rewrite;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Game {
	static ArrayList<User> users = new ArrayList<>();
	static ArrayList<String> gifts = new ArrayList<>();
	static int turn = 0;
	static boolean gameOver = false;
	
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
}
