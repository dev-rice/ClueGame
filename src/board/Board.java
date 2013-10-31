package board;

import java.awt.Color;
import java.awt.Graphics;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;

import player.Player;
import board.RoomCell.DoorDirection;

public class Board extends JPanel {
	private ArrayList<BoardCell> cells;
	private Map<Character,String> rooms;
	private int numRows, numColumns;
	private String layout, legend;
	private Map<Integer, LinkedList<Integer>> adjacencies;
	private boolean[] visited;
	private Set<BoardCell> targets;
	private ArrayList<Player> current_players;

	public Board(String layout, String legend) {
		this.layout = layout;
		this.legend = legend;
		rooms = new HashMap<Character,String>();
		cells = new ArrayList<BoardCell>();
		adjacencies = new HashMap<Integer, LinkedList<Integer>>();
		numRows = 0;
		numColumns = 0;
		targets = new HashSet<BoardCell>();
		visited = new boolean[numRows*numColumns];
		current_players = new ArrayList<Player>();
		
		loadConfigFiles();
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponents(g);
		
		for (BoardCell cell : cells){
			cell.draw(g);
		}
		
		for (Player player : current_players){
			player.draw(g);
		}
		int size = BoardCell.TILE_SIZE;
		g.setColor(Color.WHITE);
		g.drawString("Conservatory", 2*size, 2*size);
		g.drawString("Ballroom", 2*size, 11*size);
		g.drawString("Closet", 3*size, 19*size);
		g.drawString("Kitchen", 2*size, 24*size);
		g.drawString("Bathroom", 12*size, 23*size);
		g.drawString("Bowling Alley", 22*size, 23*size);
		g.drawString("Potion Room", 22*size, 9*size);
		g.drawString("Garage", 20*size, 2*size);
		g.drawString("Aquarium", 10*size, 2*size);
		g.drawString("Stairs", 13*size, 11*size);
		
	}
	
	public void updatePlayers(ArrayDeque<Player> players) {
		for (Player player : players) {
			current_players.add(player);
		}
	}
	
	public void calcAdjacencies(){
		for (int i = 0; i < numRows*numColumns;i++){
			adjacencies.put(i, getAdjacencies(i));
		}
	}
	
	public void startTargets(int row, int column, int steps){
		Arrays.fill(visited,false);
		calcAdjacencies();
		targets.clear();
		int cell = calcIndex(row,column);
		if(cell != -1){
			visited[cell] = true;
		}
		calcTargets(cell,steps);
	}
	
	public void calcTargets(int cell, int steps){
		LinkedList<Integer> list = new LinkedList<Integer>();
		for (Integer i:adjacencies.get(cell)){
			if (!visited[i]){
				list.add(i);
			}
		}
		for (Integer i:list){
			visited[i] = true;
			if (getCellAt(i).isDoorWay()){
				if (!targets.contains(getCellAt(i))){
					targets.add(getCellAt(i));
				}
			}
			else if (steps == 1){
				if (!targets.contains(getCellAt(i))){
					targets.add(getCellAt(i));
				}
			}
			else{
				calcTargets(i,steps-1);
			}
			visited[i] = false;
		}
	}
	
	public Set<BoardCell> getTargets(){
		return targets;
	}
	
	public LinkedList<Integer> getAdjacencies(int index){
		LinkedList<Integer> list = new LinkedList<Integer>();
		int cellX = 0, cellY = 0;
		try {
			cellX = index % numColumns;
			cellY = index / numColumns;
		} catch ( ArithmeticException e) {
			System.out.println("Error: Encountered a divide by zero error.");
		}
		
		if (getCellAt(calcIndex(cellY,cellX)).isRoom()){
			if (!getCellAt(calcIndex(cellY,cellX)).isDoorWay()){
				return list;
			}
		}
		
		if (getCellAt(calcIndex(cellY,cellX)).isDoorWay()){
			if (getRoomCellAt(cellY,cellX).getDoorDirection() == DoorDirection.UP){
				list.push(calcIndex(cellY-1,cellX));
			}
			else if (getRoomCellAt(cellY,cellX).getDoorDirection() == DoorDirection.DOWN){
				list.push(calcIndex(cellY+1,cellX));
			}
			else if (getRoomCellAt(cellY,cellX).getDoorDirection() == DoorDirection.LEFT){
				list.push(calcIndex(cellY,cellX-1));
			}
			else if (getRoomCellAt(cellY,cellX).getDoorDirection() == DoorDirection.RIGHT){
				list.push(calcIndex(cellY,cellX+1));
			}
			return list;
		}
		
		if (calcIndex(cellY,cellX-1) != -1){
			if (getCellAt(calcIndex(cellY,cellX-1)).isWalkway()){	//Push target always if is walkway
				list.push(calcIndex(cellY,cellX-1));
			}
			else if ( (getCellAt(calcIndex(cellY,cellX-1)).isDoorWay())  	&&		(!getCellAt(calcIndex(cellY,cellX)).isDoorWay())){ //Push target if adj cell is door and current is not
				if (getRoomCellAt(cellY,cellX-1).getDoorDirection() == DoorDirection.RIGHT)
					list.push(calcIndex(cellY,cellX-1));
			}
		}
		if (calcIndex(cellY,cellX+1) != -1){
			if (getCellAt(calcIndex(cellY,cellX+1)).isWalkway()){	//Push target always if is walkway
				list.push(calcIndex(cellY,cellX+1));
			}
			else if ( (getCellAt(calcIndex(cellY,cellX+1)).isDoorWay())  	&&		(!getCellAt(calcIndex(cellY,cellX)).isDoorWay())	){ //Push target if adj cell is door and current is not
				if (getRoomCellAt(cellY,cellX+1).getDoorDirection() == DoorDirection.LEFT)
					list.push(calcIndex(cellY,cellX+1));
			}
		}
		if (calcIndex(cellY+1,cellX) != -1){
			if (getCellAt(calcIndex(cellY+1,cellX)).isWalkway()){	//Push target always if is walkway	
				list.push(calcIndex(cellY+1,cellX));
			}
			else if ( (getCellAt(calcIndex(cellY+1,cellX)).isDoorWay())  	&&		(!getCellAt(calcIndex(cellY,cellX)).isDoorWay())	){ //Push target if adj cell is door and current is not
				if (getRoomCellAt(cellY+1,cellX).getDoorDirection() == DoorDirection.UP)
					list.push(calcIndex(cellY+1,cellX));
			}
		}
		if (calcIndex(cellY-1,cellX) != -1){
			if (getCellAt(calcIndex(cellY-1,cellX)).isWalkway()){	//Push target always if is walkway
				list.push(calcIndex(cellY-1,cellX));
			}
			else if ( (getCellAt(calcIndex(cellY-1,cellX)).isDoorWay())  	&&		(!getCellAt(calcIndex(cellY,cellX)).isDoorWay())	){ //Push target if adj cell is door and current is not
				if (getRoomCellAt(cellY-1,cellX).getDoorDirection() == DoorDirection.DOWN)	
					list.push(calcIndex(cellY-1,cellX));
			}
		}
		return list;
	}

	public void loadConfigFiles(){
		try {
			loadRoomConfig();
		} catch (BadConfigFormatException e) {
			System.out.println(e);;
		}
		try {
			loadBoardConfig();
		} catch (BadConfigFormatException e) {
			System.out.println(e);
		}
		visited = new boolean[numRows*numColumns];
	}

	public void loadRoomConfig() throws BadConfigFormatException {
		try {
			FileReader reader = new FileReader(legend);
			Scanner in = new Scanner(reader);
			while(in.hasNextLine()) {
				String line = in.nextLine();
				String[] parts = line.split(",");
				if(parts.length != 2) {
					throw new BadConfigFormatException("Error, legend formatted wrong");
				}
				parts[1] = parts[1].trim();
				rooms.put(parts[0].charAt(0), parts[1]);
			}
		} catch (FileNotFoundException e) {
			System.out.println(e.getLocalizedMessage());
		}

	}

	public void loadBoardConfig() throws BadConfigFormatException {
		try {
			FileReader reader = new FileReader(layout);
			Scanner in = new Scanner(reader);
			while(in.hasNextLine()){
				String line = in.nextLine();
				String[] parts = line.split(",");
				if(numColumns == 0) {
					numColumns = parts.length;
				}
				if(numColumns != parts.length){
					throw new BadConfigFormatException("Error, number of columns in rows is not the same.");
				}
				for(int i = 0; i < parts.length; i++) {
					if(!rooms.containsKey(parts[i].charAt(0))) {
						throw new BadConfigFormatException("Error, board contains invalid parts.");
					}
					if(parts[i].equals("W")) {
						WalkwayCell wc = new WalkwayCell(numRows, i);
						cells.add(wc);
					} else {
						RoomCell rc = new RoomCell(numRows, i, parts[i]);
						cells.add(rc);
					}
				}
				numRows = numRows + 1;
			} 
		} catch (FileNotFoundException e) {
			System.out.println(e.getLocalizedMessage());
		}

	}

	public int calcIndex(int row, int column){
		if ( (column > -1)&&(column<numColumns)&&(row>-1)&&(row<numRows) ){
			return numColumns*row+column;
		}
		return -1;
	}

	public RoomCell getRoomCellAt(int row, int column){
		return (RoomCell) cells.get(calcIndex(row,column));
	}

	public ArrayList<BoardCell> getCells() {
		return cells;
	}

	public Map<Character, String> getRooms() {
		return rooms;
	}

	public int getNumRows() {
		return numRows;
	}

	public int getNumColumns() {
		return numColumns;
	}

	public BoardCell getCellAt(int index){
		return cells.get(index);
	}

}
