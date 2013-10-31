package tests;

import java.io.FileNotFoundException;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import board.BadConfigFormatException;
import board.Board;
import board.BoardCell;
import board.RoomCell;

public class clueTest {
	private static Board board;
	public static final int NUM_ROOMS = 11; // total equals rooms plus walkway plus closet
	public static final int NUM_ROWS = 26;
	public static final int NUM_COLUMNS = 26;

	@BeforeClass
	public static void setUp() {
		board = new Board("Layout.csv", "Legend.csv");
		board.loadConfigFiles();
	}

	@Test
	public void testRooms() {
		Map<Character, String> rooms = board.getRooms();
		Assert.assertEquals(NUM_ROOMS, rooms.size());
		Assert.assertEquals("Conservatory", rooms.get('C'));
		Assert.assertEquals("Bowling Alley", rooms.get('Y'));
		Assert.assertEquals("Potion Room", rooms.get('P'));
		Assert.assertEquals("Aquarium", rooms.get('A'));
		Assert.assertEquals("Walkway", rooms.get('W'));
	}

	@Test
	public void testBoardSize() {
		Assert.assertEquals(NUM_ROWS, board.getNumRows());
		Assert.assertEquals(NUM_COLUMNS, board.getNumColumns());

	}

	@Test
	public void testDoorDirection() {
		RoomCell room = board.getRoomCellAt(2,15);
		Assert.assertTrue(room.isDoorWay());
		Assert.assertEquals(RoomCell.DoorDirection.RIGHT, room.getDoorDirection());
		room = board.getRoomCellAt(3, 3);
		Assert.assertTrue(room.isDoorWay());
		Assert.assertEquals(RoomCell.DoorDirection.DOWN, room.getDoorDirection());
		room = board.getRoomCellAt(10,21);
		Assert.assertTrue(room.isDoorWay());
		Assert.assertEquals(RoomCell.DoorDirection.LEFT, room.getDoorDirection());
		room = board.getRoomCellAt(21,13);
		Assert.assertTrue(room.isDoorWay());
		Assert.assertEquals(RoomCell.DoorDirection.UP, room.getDoorDirection());
		room = board.getRoomCellAt(0, 0);
		Assert.assertFalse(room.isDoorWay());	
		BoardCell cell = board.getCellAt(board.calcIndex(5,0));
		Assert.assertFalse(cell.isDoorWay());	
	}

	@Test
	public void testNumberOfDoorways() {
		int numDoors = 0;
		int totalCells = board.getNumColumns() * board.getNumRows();
		Assert.assertEquals(NUM_ROWS*NUM_COLUMNS, totalCells);
		for (int i=0; i<totalCells; i++) {
			BoardCell cell = board.getCellAt(i);
			if (cell.isDoorWay())
				numDoors++;
		}
		Assert.assertEquals(25, numDoors);
	}

	@Test
	public void testRoomInitials() {
		Assert.assertEquals('C', board.getRoomCellAt(0, 0).getCellData());
		Assert.assertEquals('B', board.getRoomCellAt(6,0).getCellData());
		Assert.assertEquals('Y', board.getRoomCellAt(25, 25).getCellData());
		Assert.assertEquals('G', board.getRoomCellAt(0,25).getCellData());
		Assert.assertEquals('T', board.getRoomCellAt(23,13).getCellData());
	}

	@Test
	public void testCalcIndex() {
		int actual = board.calcIndex(1,1);
		int expected = 27;
		Assert.assertEquals(expected, actual);

		actual = board.calcIndex(2,3);
		expected = 55;
		Assert.assertEquals(expected, actual);
	}

	@Test (expected = BadConfigFormatException.class)
	public void testColumnException() throws BadConfigFormatException, FileNotFoundException {
		Board b = new Board("BadColumns.csv", "Legend.csv");
		b.loadRoomConfig();
		b.loadBoardConfig();
	}
	@Test (expected = BadConfigFormatException.class)
	public void testRoomException() throws BadConfigFormatException, FileNotFoundException {
		Board b = new Board("BadRoom.csv", "Legend.scv");
		b.loadRoomConfig();
		b.loadBoardConfig();
	}
	@Test (expected = BadConfigFormatException.class)
	public void testRoomFormatException() throws BadConfigFormatException, FileNotFoundException {
		Board b = new Board("Layout.csv", "BadLegend.txt");
		b.loadRoomConfig();
		b.loadBoardConfig();
	}
}
