package ClueGame;

public class Solution {

	private String person;
	private String weapon;
	private String room;
	
	public Solution(String person, String weapon, String room) {
		this.person = person;
		this.weapon = weapon;
		this.room = room;
	}

	public String getPerson() {
		return person;
	}

	public String getWeapon() {
		return weapon;
	}

	public String getRoom() {
		return room;
	}

	@Override
	public String toString() {
		return "Solution [person=" + person + ", weapon=" + weapon + ", room="
				+ room + "]";
	}

	public boolean guessIsCorrect(String person, String weapon, String room){
		return ( this.person.equals(person) && this.weapon.equals(weapon) && this.room.equals(room) );
	}

}
