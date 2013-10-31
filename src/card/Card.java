package card;

public class Card {
	
	public String name;
	public CardType type;
	
	public enum CardType {
		ROOM, WEAPON, PERSON;
	}
	
	public Card(String name, CardType type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public CardType getType() {
		return type;
	}

	@Override
	public String toString() {
		return "Card [name=" + name + ", type=" + type + "]";
	}

	@Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        Card card = (Card) obj;
        return name.equals(card.name) && type == card.getType();
                
    }

}
