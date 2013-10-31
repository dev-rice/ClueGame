package player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

import card.Card;

import board.BoardCell;
import board.RoomCell;

public class ComputerPlayer extends Player{
	
	private char lastRoomVisited;

	public ComputerPlayer(String name) {
		super(name);
		// TODO Auto-generated constructor stub
		lastRoomVisited = ' ';
	}
	
	public void setLastRoomVisited(char room){
		lastRoomVisited = room;
	}
	
	public void pickLocation(Set<BoardCell> targets){
		// Given a Set of potential targets, pick one randomly
		// UNLESS there's a door, pick it, UNLESS we JUST visited the door.
		// Also, know when you're in a room, instead of moving, make a suggestion.

		// Iterates through targets looking for the first doorway it can find
		BoardCell removeMe = null;
		for(BoardCell cell : targets){
			if(cell.isDoorWay()){
				//System.out.println("It's a doorway!");
				if( ((RoomCell)cell).getCellData() != lastRoomVisited ){
					// If the cell is a doorway, that we haven't visited yet
					lastRoomVisited = ((RoomCell)cell).getCellData();
					// then move there
					moveToCell(cell);
					// Then make a guess
					createSuggestion(((RoomCell)cell).getRoom());
					// Then we're done!
					return;
				} else {
					removeMe = cell;
				}
			}
		}
		if (removeMe != null && removeMe instanceof RoomCell && removeMe.isDoorWay()){
			targets.remove(removeMe);
		}

		// If no doors are found, randomize the places to pick from
		Random rand = new Random();
		//System.out.println(targets.size());
		int loc = rand.nextInt(targets.size());
		int i = 0;
		for(BoardCell cell : targets){
			if(i == loc){
				moveToCell(cell);
				break;
			}
			i++;
		}
		return;

	}

	public void moveToCell(BoardCell target){
		currentCell = target;
		return;
	}

	public char getLastRoomVisited() {
		return lastRoomVisited;
	}

	public Suggestion createSuggestion(String room){
		// Initially makes a room card based on what room it is in
		Card room_suggestion = new Card(room, Card.CardType.ROOM);
		
		// Initialized ArrayLists for possible suggestion cards.
		ArrayList<Card> weapons = new ArrayList<Card>();
		ArrayList<Card> people = new ArrayList<Card>();
		
		// Fills those ArrayLists with weapons and people, respectively. It also
		//	doesn't add any cards it has seen to the ArrayLists
		for (Card card : deck){
			if (card.getType() == Card.CardType.WEAPON && !cards_seen.contains(card)){
				weapons.add(card);
			} else if (card.getType() == Card.CardType.PERSON && !cards_seen.contains(card)) {
				people.add(card);
			}
		}
		
		// Chooses a random card from each pile
		Random rand = new Random();
		int weapons_index = rand.nextInt(weapons.size());
		int people_index = rand.nextInt(people.size());
		
		Card weapon_suggestion = weapons.get(weapons_index);
		Card person_suggestion = people.get(people_index);

		// Creates a new suggestion with the cards it chose.
		return new Suggestion(person_suggestion, weapon_suggestion, room_suggestion);
	}

	public void updateSeen(Card seen){
		cards_seen.add(seen);
	}

	@Override
	public String toString() {
		return "Computer: " + name;
	}


}
