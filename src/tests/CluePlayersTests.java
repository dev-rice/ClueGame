package tests;

import static org.junit.Assert.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Set;
import java.util.Stack;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import board.BoardCell;
import board.WalkwayCell;
import card.Card;
import player.ComputerPlayer;
import player.HumanPlayer;
import player.Player;
import player.Suggestion;
import ClueGame.ClueGame;

public class CluePlayersTests {
	private static ClueGame game;

	@Before
	public void init(){
		game = new ClueGame("deck.txt"); 
	}

	@Test
	public void testLoadPeople() {
		// Tests the loading of the people from the deck.txt file
		ArrayDeque<Player> players = game.getPlayers();
		ArrayList playersArr = new ArrayList();
		int i = 0;
		for(Player player: players){
			playersArr.add(player.getName());
		}
		Assert.assertTrue(playersArr.contains("Mrs. White"));
		Assert.assertTrue(playersArr.contains("Professor Plum"));
		Assert.assertTrue(playersArr.contains("Miss Scarlet"));
		Assert.assertTrue(playersArr.contains("Mrs. Peacock"));
		Assert.assertTrue(playersArr.contains("Mister Green"));
		Assert.assertTrue(playersArr.contains("Colonel Mustard"));
	}

	@Test
	public void testCardDeal() {
		// Tests various attributes to assure the deck was 
		// dealt correctly.
		ArrayDeque<Player> players = game.getPlayers();

		Assert.assertTrue((players.getFirst().getCards().size() - players.getLast().getCards().size()) <= 1);

		ArrayList<String> cards = new ArrayList<String>();

		for (Player player : players){
			for (Card card : player.getCards()){
				assertFalse(cards.contains(card.name));
				cards.add(card.name);
			}
		}
		assertTrue(cards.size() == 20);
	}

	@Test
	public void testAccusationForcedCorrect() {
		// Tests the accusation to be true, provided
		// the Correct guess is made
		String person = game.getSoln().getPerson();
		String weapon = game.getSoln().getWeapon();
		String room = game.getSoln().getRoom();

		Assert.assertTrue(game.getSoln().guessIsCorrect(person, weapon, room));
	}

	@Test
	public void testAccusationWrongRoom() {
		// Tests the accusation to be false, provided
		// an incorrect guess is made
		String person = game.getSoln().getPerson();
		String weapon = game.getSoln().getWeapon();
		String room = "Not even a real card";

		Assert.assertFalse(game.getSoln().guessIsCorrect(person, weapon, room));
	}

	@Test
	public void testAccusationWrongPerson() {
		// Tests the accusation to be false, provided
		// an incorrect guess is made
		String person = "Lead Pipe";
		String weapon = game.getSoln().getWeapon();
		String room = game.getSoln().getRoom();

		Assert.assertFalse(game.getSoln().guessIsCorrect(person, weapon, room));
	}

	@Test
	public void testAccusationWrongWeapon() {
		// Tests the accusation to be false, provided
		// an incorrect guess is made
		String person = game.getSoln().getPerson();
		String weapon = "phone";
		String room = game.getSoln().getRoom();

		Assert.assertFalse(game.getSoln().guessIsCorrect(person, weapon, room));
	}

	@Test
	public void testSuggestionOneCard() {
		// Tests a random suggestion: has two outcomes
		// 1) Suggestion is disproved with a card
		// 2) Suggestion is True (unlikely, but possible)
		// Either outcome is acceptable, but the outcome must strictly match one of these! 
		Player chris = new Player("Chris");

		chris.addCard(new Card("Mister Green", Card.CardType.PERSON));
		chris.addCard(new Card("Lead Pipe", Card.CardType.WEAPON));
		chris.addCard(new Card("Library", Card.CardType.ROOM));

		Card disprove_card = chris.disproveSuggestion("Mister Green", "Gun", "Study");

		Assert.assertTrue(disprove_card.equals(new Card("Mister Green", Card.CardType.PERSON)));

	}

	@Test
	public void testSuggestionTwoCards() {
		// Tests a random suggestion: has two outcomes
		// 1) Suggestion is disproved with a card
		// 2) Suggestion is True (unlikely, but possible)
		// Either outcome is acceptable, but the outcome must strictly match one of these! 
		Player chris = new Player("Chris");

		chris.addCard(new Card("Mrs. White", Card.CardType.PERSON));
		chris.addCard(new Card("Gun", Card.CardType.WEAPON));
		chris.addCard(new Card("Study", Card.CardType.ROOM));

		Card disprove_card = chris.disproveSuggestion("Mister Green", "Gun", "Study");

		Assert.assertTrue(disprove_card.equals(new Card("Gun", Card.CardType.WEAPON)) ||
				disprove_card.equals(new Card("Study", Card.CardType.ROOM)));

	}

	@Test
	public void testSuggestionQuery() {
		// All of these are case specific players with their tailored
		//	hands, this makes it easier to test.
		ComputerPlayer player1 = new ComputerPlayer("Mrs. Peacock");
		player1.addCard(new Card("Gun", Card.CardType.WEAPON));
		player1.addCard(new Card("Lead Pipe", Card.CardType.WEAPON));
		player1.addCard(new Card("Rope", Card.CardType.WEAPON));

		ComputerPlayer player2 = new ComputerPlayer("Miss Scarlet");
		player2.addCard(new Card("Knife", Card.CardType.WEAPON));
		player2.addCard(new Card("Vogon Poetry", Card.CardType.WEAPON));
		player2.addCard(new Card("Dining room", Card.CardType.ROOM));

		ComputerPlayer player3 = new ComputerPlayer("Professor Plum");
		player3.addCard(new Card("Mrs. Peacock", Card.CardType.PERSON));
		player3.addCard(new Card("Miss Scarlet", Card.CardType.PERSON));
		player3.addCard(new Card("Professor Plum", Card.CardType.PERSON));
		player3.addCard(new Card("Lounge", Card.CardType.ROOM));

		ComputerPlayer player4 = new ComputerPlayer("Mrs. White");
		player4.addCard(new Card("Colonel Mustard", Card.CardType.PERSON));
		player4.addCard(new Card("Mister Green", Card.CardType.PERSON));
		player4.addCard(new Card("Hall", Card.CardType.ROOM));

		ComputerPlayer player5 = new ComputerPlayer("Colonel Mustard");
		player5.addCard(new Card("Conservatory", Card.CardType.ROOM));
		player5.addCard(new Card("Kitchen", Card.CardType.ROOM));
		player5.addCard(new Card("Ballroom", Card.CardType.ROOM));
		player5.addCard(new Card("Closet", Card.CardType.ROOM));

		HumanPlayer player6 = new HumanPlayer("Mister Green");
		player6.addCard(new Card("Library", Card.CardType.ROOM));
		player6.addCard(new Card("Study", Card.CardType.ROOM));
		player6.addCard(new Card("Walkway", Card.CardType.ROOM));

		ArrayDeque<Player> players = new ArrayDeque<Player>();
		players.add(player1);
		players.add(player2);
		players.add(player3);
		players.add(player4);
		players.add(player5);
		players.add(player6);
		
		// Set the game's players to those we just created, also sets the solution
		//	(which no players have).
		game.setPlayers(players);
		game.setSolution("Mrs. White", "Candlestick", "Billiard room");

		// Test that someone suggests the solution
		Assert.assertEquals(game.handleSuggestion("Mrs. White", "Candlestick", "Billiard room", player1), null);

		// Test that a computer player returns the right card
		Assert.assertEquals(game.handleSuggestion("Mrs. White", "Gun", "Billiard room" , player2),
				new Card("Gun", Card.CardType.WEAPON));

		// Test that if the accusing player has the card of interest, null is returned
		Assert.assertEquals(game.handleSuggestion("Mrs. White", "Gun", "Billiard room" , player1), null);

		// Test that the players are queued in order by testing a suggestion that two players could
		//	deny and making sure the first one queued returns the card.
		Assert.assertEquals(game.handleSuggestion("Mrs. Peacock", "Candlestick", "Library" , player2), 
				new Card("Mrs. Peacock", Card.CardType.PERSON));
	}

	@Test
	public void testTargetWithRoom() {
		// Tests that the computer player, when given the option, chooses a door
		ComputerPlayer tester = (ComputerPlayer) game.getPlayers().peekLast();
		// sets the player up with a walkway next to a door
		tester.setCurrentCell(new WalkwayCell(18,13));
		// Finds the adjacencyLists for the Board
		game.getBoard().loadConfigFiles();
		game.getBoard().startTargets(18,13,3);
		// Asks the tester to pick from them
		tester.pickLocation(game.getBoard().getTargets());
		// It should pick the door
		int index = game.getBoard().calcIndex(21,13);
		BoardCell doorToPick = game.getBoard().getCellAt(index);
		Assert.assertTrue(tester.getCurrentCell().equals(doorToPick));	
	}

	@Test
	public void testTargetNoRoom() {
		// A random selection from a set of targets that don't include a room, 
		ComputerPlayer tester = (ComputerPlayer) game.getPlayers().peekLast();
		tester.setCurrentCell(new WalkwayCell(12,8));
		game.getBoard().loadConfigFiles();
		game.getBoard().startTargets(12,8,1);
		Set<BoardCell> targets = game.getBoard().getTargets();
		tester.pickLocation(game.getBoard().getTargets());
		Assert.assertTrue(targets.contains(tester.getCurrentCell()));

	}

	@Test
	public void testTargetLastVisitedRoom() {
		// considers the last visited room
		ComputerPlayer tester = (ComputerPlayer) game.getPlayers().peekLast();
		tester.setCurrentCell(new WalkwayCell(20,13));
		tester.setLastRoomVisited(' ');
		game.getBoard().loadConfigFiles();
		game.getBoard().startTargets(20,13,1);
		//System.out.println(game.getBoard().getTargets());
		tester.pickLocation(game.getBoard().getTargets());
		int index = game.getBoard().calcIndex(21,13);
		BoardCell doorToPick = game.getBoard().getCellAt(index);
		//System.out.println(tester.getCurrentCell());
		//System.out.println(doorToPick);
		Assert.assertTrue(game.getBoard().getTargets().contains(doorToPick));	
		// He's in the room.
		game.getBoard().startTargets(21,13,1);
		tester.pickLocation(game.getBoard().getTargets());
		int index2 = game.getBoard().calcIndex(20,13);
		BoardCell doorToPick2 = game.getBoard().getCellAt(index2);
		Assert.assertTrue(tester.getCurrentCell().equals(doorToPick2));
		// He's outside the room. His last visited room should be T, for bathroom.
		Assert.assertEquals('T', tester.getLastRoomVisited());
		// Make sure he didn't go back into the room
		game.getBoard().startTargets(20,13,1);
		//System.out.println(tester.getCurrentCell());
		//System.out.println(doorToPick2);
		tester.pickLocation(game.getBoard().getTargets());
		int index3 = game.getBoard().calcIndex(21,13);
		BoardCell doorToPick3 = game.getBoard().getCellAt(index3);
		//System.out.println(tester.getCurrentCell());
		//System.out.println(doorToPick3);
		Assert.assertFalse(tester.getCurrentCell().equals(doorToPick3));
	}

	@Test 
	public void ComputerSuggestionOnlyOne() {
		// Only one possible suggestion.

		ComputerPlayer player1 = new ComputerPlayer("Chris");
		player1.setDeck(game.getPlayers().getFirst().getDeck());
		
		// Creates a suggestion that will be the only possible suggestion the player can make
		Card person = new Card("Mrs. White", Card.CardType.PERSON);
		Card weapon = new Card("Gun", Card.CardType.WEAPON);
		Card room = new Card("Library", Card.CardType.ROOM);

		Suggestion expected_suggestion = new Suggestion(person, weapon, room);

		// Creates the cards_seen ArrayList to contain every card
		//	except for those in the expected suggestion.
		ArrayList<Card> cards_seen = new ArrayList<Card>();
		for (Card card : player1.getDeck()){
			if (!card.equals(person) && !card.equals(weapon) && !card.equals(room)){
				cards_seen.add(card);
			}
		}
		
		// Sets the ComputerPlayers cards_seen ArrayList to
		//	the one we just made.
		player1.setCardsSeen(cards_seen);
		
		// Ensures that the suggestion created is the expected suggestion.
		Assert.assertEquals(player1.createSuggestion("Library"), expected_suggestion);

	}

	@Test
	public void ComputerSuggestionRandomChoice() {
		// one with some random possibilities (e.g., the player 
		//	has not seen Miss Scarlet or Professor Plum... some suggestions should include Miss Scarlet, some should include Professor Plum).

		ComputerPlayer player1 = new ComputerPlayer("Chris");
		player1.setDeck(game.getPlayers().getFirst().getDeck());
		
		Card person = new Card("Miss Scarlet", Card.CardType.PERSON);
		Card weapon = new Card("Gun", Card.CardType.WEAPON);
		Card room = new Card("Library", Card.CardType.ROOM);
		
		Card person2 = new Card("Professor Plum", Card.CardType.PERSON);

		// Creates two possible suggestions, one with person and one with person2
		Suggestion expected_suggestion1 = new Suggestion(person, weapon, room);
		Suggestion expected_suggestion2 = new Suggestion(person2, weapon, room);

		
		// Makes an ArrayList to contain all of the cards except the four made above.
		//	(any card in either of our two suggestions).
		
		ArrayList<Card> cards_seen = new ArrayList<Card>();
		for (Card card : player1.getDeck()){
			if (!card.equals(person) && !card.equals(weapon) && !card.equals(room) && !card.equals(person2)) {
				cards_seen.add(card);
			}
		}

		// Sets the ComputerPlayers cards_seen to the one we just made.
		player1.setCardsSeen(cards_seen);
		
		int suggestion1_count = 0;
		int suggestion2_count = 0;
		int neither_count = 0;
		
		// Keeps track of how many times each suggestion was chosen out of 100 tries.
		//	Also counts any unexpected suggestions.
		for (int i = 0; i < 100; ++i){
			Suggestion real_suggestion = player1.createSuggestion("Library");
			if (real_suggestion.equals(expected_suggestion1)){
				++suggestion1_count;
			} else if (real_suggestion.equals(expected_suggestion2)) {
				++suggestion2_count;
			} else {
				++neither_count;
			}
		}
		
		// Makes sure that we have at least one of each suggestion and zero
		//	unexpected suggestions.
		Assert.assertTrue(suggestion1_count >= 1);
		Assert.assertTrue(suggestion2_count >= 1);
		Assert.assertEquals(neither_count, 0);
	}


}
