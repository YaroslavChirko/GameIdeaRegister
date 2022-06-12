package gamereg.graphical;

import java.util.Collections;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import gamereg.dao.ConceptDao;
import gamereg.dao.ConnectionLayer;
import gamereg.dao.models.Concept;
import gamereg.dao.models.GameCharacter;

public class MainWindowCreator {
	JFrame mainFrame;
	ConceptDao conceptDao;
	
	public MainWindowCreator() {
		String propertyPath = Thread.currentThread().getContextClassLoader().getResource("db.properties").getPath();
		
		mainFrame = new JFrame("Game Idea Register");
		conceptDao = new ConceptDao(new ConnectionLayer(propertyPath).getConn());//TODO use real path to java/resources
		//call method to populate the frame
		initFrame(mainFrame);
		
		mainFrame.setVisible(true);
		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		mainFrame.pack();
	}
	
	private void initFrame(JFrame frame) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		//add table but don't make it visible yet
		JTable conceptTable = new JTable(/*new ConceptTableModel()*/);
		
		//add button to get all records
		JButton loadEntriesButton = new JButton("Load Entries");
		//TODO make other thread to load entries, perhaps page them as well
		
		loadEntriesButton.addActionListener(e ->{
			List<Concept> concepts = conceptDao.getConcepts();
			int numOfRows = concepts.size();
			int numOfColumns = Concept.class.getDeclaredFields().length;
			
			//ConceptTableModel conceptModel =  (ConceptTableModel)conceptTable.getModel();
			//populate the table
			DefaultTableModel conceptModel = (DefaultTableModel)conceptTable.getModel();
			conceptModel.setColumnCount(numOfColumns);
			conceptModel.setRowCount(numOfRows);
			//conceptModel.addConcepts(concepts);
			int currentRow = 0;
			for(Concept concept: concepts) {
				conceptModel.setValueAt(concept.getTitle(), currentRow, 0);
				conceptModel.setValueAt(concept.getGenre(), currentRow, 1);
				conceptModel.setValueAt(concept.getDescription(), currentRow, 2);
				conceptModel.setValueAt("No characters for now", currentRow, 3);
				
				currentRow++;
			}
			
		});
		
		JButton addConceptButton = new JButton("Add Concept");
		addConceptButton.addActionListener( e-> {
			//call to create window
			AddConceptWindow addConceptWindow = new AddConceptWindow(conceptDao);
			//call to open the created window
			addConceptWindow.toggleVisibility();
			
		});
		
		JScrollPane tableScrollPane = new JScrollPane(conceptTable);
		panel.add(tableScrollPane);
		panel.add(loadEntriesButton);
		panel.add(addConceptButton);
		
		//add button to add concepts (opens a new window)
		//add buttons to add player characters and enemies (to the row, opens a new window)
	
		frame.add(panel);
	}

	/*private class ConceptTableModel extends AbstractTableModel{

		private final List<Concept> concepts = Collections.emptyList();
		private final String[] columnNames = {"Title", "Genre", "Description", "GameCharacters"};
		//hardcoded for now not to bother with try-catch in reflection
		private final Class[] columnTypes = {String.class, String.class, String.class, GameCharacter.class};
		
		@Override
		public int getRowCount() {
			return concepts.size();
		}

		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Object res = null;
			try {
				res = Concept.class.getField(columnNames[columnIndex]).get(concepts.get(rowIndex));
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
				e.printStackTrace();
			}
			
			return res;
		}
		
		public void addConcept (Concept c) {
			this.concepts.add(c);
		}
		
		public void addConcepts(List<Concept> newConcepts) {
			this.concepts.addAll(newConcepts);
		}
		
		
	}*/
	
}
