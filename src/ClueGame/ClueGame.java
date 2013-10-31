package ClueGame;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import board.Board;
import player.ComputerPlayer;
import player.HumanPlayer;
import player.Player;
import card.Card;
import card.Card.CardType;

public class ClueGame extends JFrame {
	
	private static String DECK_FILE;
	
	private static final String HUMAN_PLAYER = "Professor Plum"; 

	private Solution soln;
	private ArrayDeque<Player> players;
	private Player current_player;
	private boolean game_running;
	private Board board;
	private Notes notes;
	
	public ClueGame(String deck) {
		// Should have the initialization of the game
		// automated, or should it be called by the creating
		// class?
		DECK_FILE = deck;
		board = new Board("Layout.csv", "Legend.csv");
		players = loadPeople();
		deal();
		
		players.getFirst().setDeck(loadDeck());
				
		board.updatePlayers(players);
		
		// All JFrame things

		add(board, BorderLayout.CENTER);
		setSize(896,896);
		setTitle("Clue Game");
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		menuBar.add(createFileMenu());
		
		//ControlPanel control_panel = new ControlPanel();
		//add(control_panel, BorderLayout.EAST);
		
		for (Player player : players) {
			if (player instanceof HumanPlayer) {
				add(new CardsPanel(player), BorderLayout.EAST);
			}
		}
		
	}
	
	private JMenu createFileMenu() {
	  JMenu menu = new JMenu("File");
	  menu.add(createFileOpenNotesItem());
	  menu.add(createFileExitItem());
	  return menu;
	}
	
	private JMenuItem createFileOpenNotesItem(){
		JMenuItem item = new JMenuItem("Open Notes");
		  class MenuItemListener implements ActionListener {
		    public void actionPerformed(ActionEvent e)
		    {
		    	if (notes == null){
		    		notes = new Notes(loadDeck());
		    	}
		    	notes.setVisible(true);
		    }
		  }
		  item.addActionListener(new MenuItemListener());
		  return item;
	}
	
	private JMenuItem createFileExitItem() {
	  JMenuItem item = new JMenuItem("Exit");
	  class MenuItemListener implements ActionListener {
	    public void actionPerformed(ActionEvent e)
	    {
	       System.exit(0);
	    }
	  }
	  item.addActionListener(new MenuItemListener());
	  return item;
	}
	
	public Solution getSoln() {
		return soln;
	}

	public ArrayDeque<Player> getPlayers() {
		return players;
	}
	
	public Card handleSuggestion(String person, String weapon, String room, Player accusingPlayer) {
		// Check all the players that are not the accusingPlayer.
		
		// If the suggestion is the solution, don't bother checking
		if (soln.guessIsCorrect(person, weapon, room)){
			return null;
		}
		
		//System.out.println("Players: " + players);
		
		// Iterate through the queue until you get to the accusing player.
		//	At the same time this puts the next players in order.
		Player temp = players.pop();
		
		while (!temp.equals(accusingPlayer)){
			players.add(temp);
			//System.out.println(players);
			temp = players.pop();
			//System.out.println(players);
		}

		//System.out.println("Players, sorted: " + players);
		
		// Once you have the players queued in the correct order, check their response
		//	to this suggestion. 
		for (Player player : players){
			//System.out.println(player);

			Card disproving_card = player.disproveSuggestion(person, weapon, room);
			
			if (disproving_card != null && !player.equals(accusingPlayer)){
				players.add(accusingPlayer);
				return disproving_card;
			}
		}

		return null;
	}

	// private void runGame(){
	// 	game_running = true;

	// 	while (game_running){
	// 		takeTurn();

	// 	}

	// }

	public void takeTurn(){
	 	// Pulling the player whose turn it is
	 	// off, using them to take a turn
		current_player = players.pop();

		if (current_player instanceof ComputerPlayer){
	 		// Do computer things.
		} else {
	 		// Do human things.
		}

	}
	public void endTurn(){
	 	// Pushes the current player to the end of the 
	 	// queue, so it's turn is the farthest away
		players.push(current_player);
	}

	public Board getBoard() {
		return board;
	}
	
	public void setPlayers(ArrayDeque<Player> players) {
		this.players = players;
	}
	
	public void setSolution(String person, String weapon, String room){
		soln = new Solution(person, weapon, room);
	}

	private void deal(){
		// Process deck
		Stack deck = loadDeck();
		// Deal (Pop) the rest off stack to players, while choosing the solutions.
		boolean dealtWeapon = false;
		boolean dealtPerson = false;
		boolean dealtRoom = false;
		String weapon = null;
		String person = null;
		String room = null;
		while(!deck.empty()){
			Card temp = (Card) deck.pop();
			// If the card type person hasn't been drawn yet, and the card is a person
			if(temp.type == CardType.PERSON && !dealtPerson){
				dealtPerson = true;
				person = temp.name;
				temp = (Card) deck.pop(); // Make sure the player DOESN'T get the card that's in the solution
			// Else if the card type weapon hasn't been drawn yet, and the card is a weapon
			} else if (temp.type == CardType.WEAPON && !dealtWeapon){
				dealtWeapon = true;
				weapon = temp.name;
				temp = (Card) deck.pop(); // Make sure the player DOESN'T get the card that's in the solution
			}  else if (temp.type == CardType.ROOM && !dealtRoom){
				dealtRoom = true;
				room = temp.name;
				temp = (Card) deck.pop(); // Make sure the player DOESN'T get the card that's in the solution
			}
			// Give the cards that haven't been taken by the solution to the player.
			Player tempPlayer = players.pop();
			tempPlayer.addCard(temp);
			players.offer(tempPlayer);
		}
		soln = new Solution(person, weapon, room);
	}
	
	private ArrayDeque<Player> loadPeople(){
		// Make an queue of people that will work like a circular queue
		ArrayDeque<Player> people = new ArrayDeque<Player>();
		try {
			FileReader reader = new FileReader(DECK_FILE);
			Scanner in = new Scanner(reader);
			
			while (!in.nextLine().equals("*PERSON")){
				// Iterate until we find the person header
			}
			
			//When we find it we will add all of the names to the queue until we
			// hit the next header
			String name = "*";
			while (in.hasNextLine()){
				name = in.nextLine();
				if( name.contains("*")){
					break;
				} else if (!name.equals(HUMAN_PLAYER))  {
					people.push(new ComputerPlayer(name));
				} else {
					people.push(new HumanPlayer(name));
				}
			}
			
		} catch (FileNotFoundException e) {
			System.out.println(e.getLocalizedMessage());
		}
		
		return people;
	}
	
	private Stack<Card> loadDeck() {
		// Reads in Deck's data, adds it to deck, returns deck.
		List tempList = new ArrayList();
		Stack deck = new Stack();
		String buffer;
		CardType type = null;
		try {
			FileReader reader = new FileReader(DECK_FILE);
			Scanner in = new Scanner(reader);
			while(in.hasNextLine()) {
				buffer = in.nextLine();
				// If weapon, person, or room, change type
				if(buffer.equals("*WEAPON")){
					type = CardType.WEAPON;
				} else if ( buffer.equals("*PERSON")){
					type = CardType.PERSON;
				} else if ( buffer.equals("*ROOM")){
					type = CardType.ROOM;
				// Else, create card and add to list
				} else {
					tempList.add(new Card(buffer, type));
				}
			}
			// Shuffles cards
			Collections.shuffle(tempList);
			// Adds cards to deck (a stack)
			for(int i=0; i < tempList.size(); ++i){
				deck.push(tempList.get(i));
			}
			
		} catch (FileNotFoundException e) {
			System.out.println(e.getLocalizedMessage());
		}
		return deck;
	}
	
	public static void main(String[] args) {
		ClueGame game = new ClueGame("deck.txt");
		
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.setVisible(true);

	}
}
