package tests;

import java.util.LinkedList;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Test;

import board.Board;
import board.BoardCell;

public class pathTests {

	private static Board board;
	public static final int NUM_ROOMS = 11;
	public static final int NUM_ROWS = 26;
	public static final int NUM_COLUMNS = 26;
	
	@BeforeClass
	public static void setUp() {
		board = new Board("Layout.csv", "Legend.csv");
		board.loadConfigFiles();
		board.calcAdjacencies();
	}
	
	@Test
	public void testAdjacencyWalkways(){
		//Walkway-only adjacency
			LinkedList<Integer> testList = board.getAdjacencies(board.calcIndex(24,9));
			Assert.assertTrue(testList.contains(board.calcIndex(25,9)));
			Assert.assertEquals(4, testList.size());
		
		//Walkway Edges
			//Walkway against room cell - top edge
			testList = board.getAdjacencies(board.calcIndex(0,6));
			Assert.assertTrue(testList.contains(board.calcIndex(1,6)));
			Assert.assertEquals(2, testList.size());
			
			//Walkway against room cell - bottom edge
			testList = board.getAdjacencies(board.calcIndex(25,17));
			Assert.assertTrue(testList.contains(board.calcIndex(24,17)));
			Assert.assertEquals(2, testList.size());
			
			//left edge only path to right.squished between rooms
			testList = board.getAdjacencies(board.calcIndex(5,0));
			Assert.assertTrue(testList.contains(board.calcIndex(5,1)));
			Assert.assertEquals(1, testList.size());
			
			//Walkway against room cell - right edge
			testList = board.getAdjacencies(board.calcIndex(14,25));
			Assert.assertTrue(testList.contains(board.calcIndex(15,25)));
			Assert.assertEquals(2, testList.size());
			
	}
	
	@Test
	public void testAdjacencyDoorways(){
		// Test ballroom up doorway
			LinkedList<Integer> testList = board.getAdjacencies(board.calcIndex(7,4));
			Assert.assertTrue(testList.contains(board.calcIndex(8,4)));
			Assert.assertEquals(4, testList.size());
		// Test garage down
			testList = board.getAdjacencies(board.calcIndex(4,20));
			Assert.assertTrue(testList.contains(board.calcIndex(3,20)));
			Assert.assertEquals(4, testList.size());
	}
	
	@Test
	public void testRoomExitAdjacency(){
		// Test exit from closet to the right
			LinkedList<Integer> testList = board.getAdjacencies(board.calcIndex(19,8));
			Assert.assertTrue(testList.contains(board.calcIndex(19,9)));
			Assert.assertEquals(1, testList.size());
		// Test exit from bowling ally to up
			testList = board.getAdjacencies(board.calcIndex(20,21));
			Assert.assertTrue(testList.contains(board.calcIndex(19, 21)));
			Assert.assertEquals(1, testList.size());
	}
	@Test
	public void testRoomExitTarget(){
		board.startTargets(21, 13, 1);
		Set<BoardCell> targets= board.getTargets();
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(20, 13))));
		Assert.assertEquals(1, targets.size());

		board.startTargets(22,1, 2);
		targets= board.getTargets();
		Assert.assertEquals(3, targets.size());
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(20,1))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(21,0))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(21,2))));
	}
	
	@Test
	public void testRoomEnterTarget(){
		board.startTargets(11, 20, 1);
		Set<BoardCell> targets= board.getTargets();
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(11, 21))));
		Assert.assertEquals(4, targets.size());

		board.startTargets(17, 3, 2);
		targets= board.getTargets();
		Assert.assertEquals(5, targets.size());
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(16, 3))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(16, 2))));
	}
	@Test
	public void testWalkwayTargets(){
		board.startTargets(18, 13, 1);
		Set<BoardCell> targets= board.getTargets();
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(17, 13))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(19, 13))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(18, 12))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(18, 14))));
		Assert.assertEquals(4, targets.size());

		board.startTargets(0, 16, 2);
		targets= board.getTargets();
		Assert.assertEquals(2, targets.size());
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(1, 17))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(2, 16))));
		
		board.startTargets(15, 25, 3);
		targets= board.getTargets();
		Assert.assertEquals(4, targets.size());
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(14, 25))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(15, 24))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(14, 23))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(15, 22))));
		
		board.startTargets(12, 8, 1);
		targets= board.getTargets();
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(11, 8))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(13, 8))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(12, 9))));
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(12, 7))));
		Assert.assertEquals(4, targets.size());
	}

}
