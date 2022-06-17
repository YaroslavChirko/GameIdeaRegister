package gamereg.graphical;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import gamereg.dao.ConceptDao;
import gamereg.dao.models.Concept;

public class EditFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private ConceptDao conceptDao;
	private Concept toEdit;
	private JTextField title;
	private JTextArea description;
	private JList<Concept.Genre> genre;
	private JButton addConceptButton = new JButton("Add concept");
	
	
	public EditFrame(ConceptDao conceptDao, String name) {
		this.conceptDao = conceptDao;
		this.toEdit = conceptDao.getConceptByTitle(name);
	
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(false);
		
		init(toEdit);
		
		this.pack();
	}
	
	private void init(Concept toEdit) {
		title = new JTextField(100);
		title.setText(toEdit.getTitle());
		
		description = new JTextArea(10, 200);
		description.setEditable(true);
		description.setText(toEdit.getDescription());
		
		genre = new JList<Concept.Genre>(Concept.Genre.values());
		genre.setSelectedIndex(toEdit.getGenreEnum().ordinal());
		
		addConceptButton.addActionListener( e-> {
			Concept toAdd = new Concept();
			toAdd.setTitle(title.getText().substring(0, Math.min(200, title.getText().length())));
			toAdd.setDescription(description.getText().substring(0, Math.min(2000, description.getText().length())));
			toAdd.setGenre(genre.getSelectedValue());
			
			conceptDao.updateConcept(toEdit.getTitle() , toAdd);
			
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
