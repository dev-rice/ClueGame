package player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Stack;

import board.BoardCell;
import board.WalkwayCell;

import card.Card;

public class Player {
	protected String name;
	protected BoardCell currentCell;
	protected ArrayList<Card> cards;
	protected ArrayList<Card> cards_seen;
	protected static Stack<Card> deck;
	protected Color player_color;
	
	protected Suggestion suggestion;
	
	public Player(String name) {
		super();
		this.name = name;
		cards_seen = new ArrayList<Card>();
		cards = new ArrayList<Card>();
		player_color = new Color(0,0,0);
		initializePlayer(name);
		// currentCell = Starting cell for each player
	}

	public Suggestion getSuggestion() {
		return suggestion;
	}
	
	private void initializePlayer(String name){
		if (name.equals("Mrs. Peacock")) {
			player_color = Color.BLUE;
			currentCell = new WalkwayCell(5,0);
		} else if (name.equals("Miss Scarlet")) { 
			player_color = Color.RED;
			currentCell = new WalkwayCell(0,6);
		} else if (name.equals("Professor Plum")) {
			player_color = Color.MAGENTA;
			currentCell = new WalkwayCell(0,16);
		} else if (name.equals("Mrs. White")) {
			player_color = Color.WHITE;
			currentCell = new WalkwayCell(5,25);
		} else if (name.equals("Colonel Mustard")) {
			player_color = Color.YELLOW;
			currentCell = new WalkwayCell(25,18);
		} else if (name.equals("Mister Green")) {
			player_color = Color.GREEN;
			currentCell = new WalkwayCell(25,9);
		} 
	}
	
	public void draw(Graphics g){
		int size = BoardCell.TILE_SIZE;
		g.setColor(player_color);
		g.fillOval(currentCell.getColumn()*size, currentCell.getRow()*size, size, size);
		g.setColor(Color.WHITE);
		g.drawOval(currentCell.getColumn()*size, currentCell.getRow()*size, size, size);
	}

	public Card disproveSuggestion(String person, String weapon, String room){
		// Makes a hand for cards that disprove the suggestion
		ArrayList<Card> disprove_cards = new ArrayList<Card>();
		
		for (Card card : cards){
			String name = card.getName();
			// If anything disproves the suggestion add it to the hand
			if (name.equals(person) || name.equals(room) || name.equals(weapon)){
				disprove_cards.add(card);
			}	
		}
		if (disprove_cards.size() != 0) {
			// If we have cards we will shuffle them and then pick the first
			//  one and show it (same as randomizing).
			Collections.shuffle(disprove_cards);
			return disprove_cards.get(0);
		} else {
			return null;
		}
	}
	
	public BoardCell getCurrentCell(){
		return currentCell;
	}
	
	public void setCurrentCell(WalkwayCell target){
		// Temporary, only for testing
		currentCell = target;
	}
	
	public void addCard(Card card){
		cards.add(card);
	}
	
	public void setCardsSeen(ArrayList<Card> seen){
		cards_seen = seen;
	}
	
	public ArrayList<Card> getCardsSeen() {
		return cards_seen;
	}
	
	public void setDeck(Stack<Card> deck) {
		this.deck = deck;
	}
	
	public Stack<Card> getDeck() {
		return deck;
	}
	
	public String getName() {
		return name;
	}

	public ArrayList<Card> getCards() {
		return cards;
	}

	@Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        Player player = (Player) obj;

        return name.equals(player.getName());                
    }
	
}
