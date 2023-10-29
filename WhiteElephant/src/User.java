import java.util.ArrayList;

public class User {
	private String username;
	private String password;
	private ArrayList<String> inventory;
	
	public User(String username, String password) {
		setUsername(username);
		setPassword(password);
		setInventory(new ArrayList<>());
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ArrayList<String> getInventory() {
		return inventory;
	}

	public void setInventory(ArrayList<String> inventory) {
		this.inventory = inventory;
	}
}
