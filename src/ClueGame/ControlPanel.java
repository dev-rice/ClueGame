package ClueGame;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.jws.Oneway;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ControlPanel extends JPanel {
	public ControlPanel(){
		super();
		JButton button = new JButton();
		
		setLayout(new GridLayout(4,1));
		
		button.setSize(500, 500);
		
		button.addActionListener(new ActionListener() {
		     public void actionPerformed(ActionEvent e)
		     {
		        System.out.println("You clicked me!");
		     }
		});
		//Uncomment if you dare.
		//add(button);
	}
	
}
