package board;

import java.awt.Graphics;

import card.Card;

public abstract class BoardCell {
	protected int row;
	protected int column;
	public static final int TILE_SIZE = 16;

	public boolean isWalkway(){
		return false;
	}
	
	public boolean isRoom(){
		return false;
	}
	
	public boolean isDoorWay(){
		return false;
	}
	
	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}
	
	public void draw(Graphics g){
		
	}

	@Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }

        BoardCell cell = (BoardCell) obj;
        return (row == cell.getRow() && column == cell.getColumn());
                
    }

}
