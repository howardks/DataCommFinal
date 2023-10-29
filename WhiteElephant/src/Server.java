import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
			// Read input data from the socket
			InputStream is = socket.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = br.readLine();

			// All of our specific stuff will go here

			// We will need to parse the JSON with Gson

			// Output the temperature string through the same socket
			OutputStream os = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(os);
			pw.println(""); // This will be what we are sending back to the server

			// Cleanup
			pw.flush();
			pw.close();
			br.close();
			socket.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
