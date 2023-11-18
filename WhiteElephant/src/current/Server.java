package current;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
	public static void main(String[] args) {
		try (ServerSocket server = new ServerSocket(5332)) {
			System.out.println("White Elephant server is running on port 5332...");
			
			while (true) {
				
			}
			
		} catch (IOException ex) {
			System.out.println("There was an input/output error");
		} catch (Exception ex) {
			System.out.println("There was an error");
		}
	}
}
