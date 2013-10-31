package ClueGame;

import java.awt.Component;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import card.Card;

public class Dropdown extends JPanel {
	public Dropdown(String label, ArrayList<Card> cards) {
		// TODO Auto-generated constructor stub
		super();
		setBorder(new TitledBorder (new EtchedBorder(), label));
		
		JComboBox<String> combo_box = new JComboBox<String>();
		
		for (Card card : cards){
			combo_box.addItem(card.getName());
		}
		
		add(combo_box);
	}
}
