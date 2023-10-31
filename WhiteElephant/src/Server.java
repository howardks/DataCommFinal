import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class Server {
	private static ArrayList<Handler> clients = new ArrayList<>();

	public static void main(String[] args) {
		Server s = new Server();
		
		try (ServerSocket server = new ServerSocket(5332)) {
			while (true) {
				Socket socket = server.accept();
				Handler handler = s.new Handler(socket);
				clients.add(handler);
				handler.start();
			}
		} catch (IOException ex) {
			System.out.println("There was an input/output error");
		} catch (Exception ex) {
			System.out.println("There was an error");
		}
	}
	
	public static void broadcastMessage(String message) {
        for (Handler client : clients) {
        	client.sendMessage(message);
        }
    }
	
	public static void broadcastMessageExclusive(String message, Handler self) {
        for (Handler client : clients) {
        	if (client != self) {        		
        		client.sendMessage(message);
        	}
        }
    }

	class Handler extends Thread {
		Socket socket;
		User self;
		PrintWriter out;

		public Handler(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			try {
				// Structures for sending output to the client
				out = new PrintWriter(socket.getOutputStream(), true);

				// Structures for receiving input from the server
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				// Send output to client
				out.println("Enter name: ");

				// Read and display input data from client
				String name = in.readLine();
				self = new User(name);
				System.out.println("Client sent name: " + name);
				Game.users.add(self);
				Server.broadcastMessageExclusive("User " + self.getName() + " joined", this);
				
				// Send output to client
				out.println("Enter gift: ");

				// Read and display input data from client
				String gift = in.readLine();
				System.out.println("Client " + self.getName() + " sent gift: " + gift);
				Game.gifts.add(gift);
				Server.broadcastMessageExclusive("User " + self.getName() + " added a gift to the gift pile", this);

				// Send output to client when other users join
				
				// Let client know we are waiting on more users to connect
				int numUsersRequired = 3;
				while (Game.gifts.size() < numUsersRequired) {
					Server.broadcastMessage("Current number of users: " + Game.gifts.size());
					Server.broadcastMessage("Waiting for " + (numUsersRequired - Game.gifts.size()) + " more users");
					TimeUnit.SECONDS.sleep(5);
				}
				
				// Beginning game when 3 users are connected
				out.println("Starting game!");
				TimeUnit.SECONDS.sleep(3);
				
				// Gameloop
				while (!Game.gameOver) {
//					// Currently junk data
//					if (Game.users.size() == 3) {
//						Server.broadcastMessage("" + Game.users.size());
//						TimeUnit.SECONDS.sleep(1);
//					}
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
}
