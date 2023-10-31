package rewrite;

public class User {
	private String name;
	private String currentGift;

	public User(String name) {
		setName(name);
		setCurrentGift("");
	}

	@Override
	public String toString() {
		return getName();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCurrentGift() {
		return currentGift;
	}

	public void setCurrentGift(String currentGift) {
		this.currentGift = currentGift;
	}
}
