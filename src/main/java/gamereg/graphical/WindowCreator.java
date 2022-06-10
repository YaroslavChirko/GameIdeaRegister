package gamereg.graphical;

import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;

import gamereg.dao.ConceptDao;
import gamereg.dao.ConnectionLayer;
import gamereg.dao.models.Concept;

public class WindowCreator {
	JFrame mainFrame;
	ConceptDao conceptDao;
	
	public WindowCreator() {
		mainFrame = new JFrame("Game Idea Register");
		conceptDao = new ConceptDao(new ConnectionLayer("ds").getConn());//TODO use real path to java/resources
		//call method to populate the frame
		initFrame(mainFrame);
		
		mainFrame.setVisible(true);
		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		mainFrame.pack();
	}
	
	private void initFrame(JFrame frame) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(mainFrame, BoxLayout.Y_AXIS));
		
		//add table but don't make it visible yet
		JTable conceptTable = new JTable();
		
		//add button to get all records
		JButton loadEntriesButton = new JButton("Load Entries");
		//TODO make other thread to load entries, perhaps page them as well
		
		loadEntriesButton.addActionListener(e ->{
			List<Concept> concepts = conceptDao.getConcepts();
			
			//populate the table
			
			//make the table visible
			
		});
		
		panel.add(conceptTable);
		panel.add(loadEntriesButton);
		
		//add button to add concepts (opens a new window)
		//add buttons to add player characters and enemies (to the row, opens a new window)
	
		frame.add(panel);
	}
	
}
