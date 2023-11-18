package old;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	static ArrayList<Handler> clients = new ArrayList<>();

	public static void main(String[] args) {
		System.out.println("Server is running on port 5332");
		try (ServerSocket server = new ServerSocket(5332)) {
			while (true) {
				Socket socket = server.accept();
				Handler handler = new Handler(socket);
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
}
