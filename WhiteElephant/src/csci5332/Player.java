package csci5332;

/*
 * This class allows each user's name and current gift
 * to be tracked. 
 */

public class Player {
	private String name;
	private String currentGift;

	public Player(String name) {
		setName(name.toLowerCase());
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
