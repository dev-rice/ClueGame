package ClueGame;

import java.awt.Checkbox;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.JDialog;

import card.Card;
import card.Card.CardType;

public class Notes extends JDialog {
	public Notes(Stack<Card> cards) {
		// TODO Auto-generated constructor stub
		super();
		
		setSize(500,500);
		
		setLayout(new GridLayout(3,2));
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		ArrayList<Card> people_cards = new ArrayList<Card>();
		ArrayList<Card> weapon_cards = new ArrayList<Card>();
		ArrayList<Card> room_cards = new ArrayList<Card>();
		
		while(!cards.isEmpty()){
			Card card = cards.pop();
			
			if (card.getType() == Card.CardType.PERSON){
				people_cards.add(card);
			} else if (card.getType() == Card.CardType.WEAPON) {
				weapon_cards.add(card);
			} else if (card.getType() == Card.CardType.ROOM && !card.getName().equals("Walkway")) {
				room_cards.add(card);
			}
			
		}
		
		add(new Checkboxes("People", people_cards));
		add(new Dropdown("Person Guess", people_cards));
		add(new Checkboxes("Weapons", weapon_cards));
		add(new Dropdown("Weapon Guess", weapon_cards));
		add(new Checkboxes("Room", room_cards));
		add(new Dropdown("Room Guess", room_cards));
		
	}
	
}
