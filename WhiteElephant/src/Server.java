import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) {
		try (ServerSocket server = new ServerSocket(5332)) {
			while (true) {
				Socket socket = server.accept();
				Handler handler = new Handler(socket);
				handler.start();
			}
		} catch (IOException ex) {
			System.out.println("There was an input/output error");
		} catch (Exception ex) {
			System.out.println("There was an error");
		}
	}
}

class Handler extends Thread {
	Socket socket;

	public Handler(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			// Structures for sending output to the client
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

			// Structures for receiving input from the server
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			// Send output to client
			out.println("Enter name: ");

			// Read and display input data from client
			String name = in.readLine();
			System.out.println("Client sent name: " + name);

			// Send output to client
			out.println("You are " + name);

			// Cleanup
			out.flush();
			out.close();
			in.close();
			socket.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
