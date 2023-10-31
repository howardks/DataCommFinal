package rewrite;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	private static ArrayList<Handler> clients = new ArrayList<>();

	public static void main(String[] args) {
		Server s = new Server();
		
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
}
