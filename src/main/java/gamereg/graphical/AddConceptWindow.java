package gamereg.graphical;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import gamereg.dao.ConceptDao;
import gamereg.dao.models.Concept;
import gamereg.dao.models.Concept.Genre;

public class AddConceptWindow extends JFrame{

	private JTextField title;
	private JTextArea description;
	private JList<Concept.Genre> genre;
	private JButton addConceptButton = new JButton("Add concept");
	
	ConceptDao conceptDao;
	
	public AddConceptWindow(ConceptDao conceptDao){
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(false);//it is only made visible by another window
		
		
		this.conceptDao = conceptDao;
		
		init();
		this.pack();
	}
	
	//can make it an initialization block instead
	private void init() {
		title = new JTextField(100);
		description = new JTextArea(10, 200);
		description.setEditable(true);
		genre = new JList<Concept.Genre>(Concept.Genre.values());
		
		addConceptButton.addActionListener( e-> {
			Concept toAdd = new Concept();
			toAdd.setTitle(title.getText().substring(0, Math.min(200, title.getText().length())));
			toAdd.setDescription(description.getText().substring(0, Math.min(2000, description.getText().length())));
			toAdd.setGenre(genre.getSelectedValue());
			
			conceptDao.addConcept(toAdd);
			
			this.dispose();
		});
		
		this.add(title);
		this.add(description);
		this.add(genre);
		this.add(addConceptButton);
	}
	
	public void toggleVisibility() {
		this.setVisible(!this.isVisible());
	}
	
}
