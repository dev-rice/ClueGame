package player;

public class HumanPlayer extends Player{

	public HumanPlayer(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString() {
		return "Human: " + name;
	}

}
