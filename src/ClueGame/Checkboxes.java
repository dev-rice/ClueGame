package ClueGame;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import card.Card;

public class Checkboxes extends JPanel {
	public Checkboxes(String label, ArrayList<Card> cards) {
		// TODO Auto-generated constructor stub
		super();
		setBorder(new TitledBorder (new EtchedBorder(), label));
		setLayout(new GridLayout(cards.size() - (cards.size() / 2), 2));
		
		//ArrayList<JCheckBox> checkboxes = new ArrayList<JCheckBox>();
		
		for (Card card : cards){
			JCheckBox temp_box = new JCheckBox();
			temp_box.setText(card.getName());
			add(temp_box);
		}	
		
	}
}
