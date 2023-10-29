import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

	public static void main(String[] args) throws Exception {
		Socket socket = new Socket("localhost", 5332);

		// Structures for receiving input from the server
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		// Structures for sending output to the server
		BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter out = new PrintWriter(socket.getOutputStream());

		// Read and display input from server
		String line = in.readLine();
		System.out.println(line);

		// Accept and send output to server
		String name = userInput.readLine().trim();
		out.println(name);
		out.flush();

		// Read and display input from server
		line = in.readLine();
		System.out.println(line);

		// Cleanup
		in.close();
		userInput.close();
		out.close();
		socket.close();
	}

}
