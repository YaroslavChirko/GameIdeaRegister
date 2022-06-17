package gamereg.graphical;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import gamereg.dao.ConceptDao;
import gamereg.dao.models.Concept;
import gamereg.dao.models.Enemy;
import gamereg.dao.models.GameCharacter;
import gamereg.dao.models.Player;

public class InspectConceptFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	public InspectConceptFrame(ConceptDao conceptDao, String name) {
		Concept conceptToInspect = conceptDao.getConceptByTitle(name);
		
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(false);
		
		init(conceptDao, conceptToInspect);
		
		this.pack();
	}
	
	private void init(ConceptDao conceptDao, Concept concept) {
		JTextArea title = new JTextArea(concept.getTitle());
		title.setEditable(false);
		JTextArea description = new JTextArea(concept.getDescription());
		description.setEditable(false);
		JTextArea genre = new JTextArea(concept.getGenre());
		genre.setEditable(false);
		
		JScrollPane scrollCharacters = new JScrollPane();
		//scrollCharacters.setPreferredSize(new Dimension(200, 200));
		
		
		JList<GameCharacter> characterList = new JList<GameCharacter>(concept.getCharacters().toArray(new GameCharacter[concept.getCharacters().size()]));
		scrollCharacters.setViewportView(characterList);
		
		
		Container mainPane = this.getContentPane();
		mainPane.add(title);
		mainPane.add(description);
		mainPane.add(genre);
		mainPane.add(scrollCharacters);
		addPopupMenu(mainPane, conceptDao, concept);
		addContextToList(mainPane, characterList, conceptDao);
		
	}
	
	public void toggleVisibility() {
		this.setVisible(!this.isVisible());
	}
	
	public void addPopupMenu(Container mainPane, ConceptDao conceptDao, Concept concept) {
		JPopupMenu popup = new JPopupMenu();
		JMenuItem addPlayer = new JMenuItem("Add player");
		JMenuItem addEnemy = new JMenuItem("Add enemy");
		
		popup.add(addPlayer);
		popup.add(addEnemy);
		
		JPanel addCharacterPanel = new JPanel();
		addCharacterPanel.setLayout(new BoxLayout(addCharacterPanel, BoxLayout.Y_AXIS));
			
		JTextField name = new JTextField();
		JTextField powers = new JTextField();
		JTextField appearance = new JTextField();
		
		
		addCharacterPanel.add(name);
		addCharacterPanel.add(powers);
		addCharacterPanel.add(appearance);
		
		
		addPlayer.addActionListener( e-> {
			
			JTextArea story = new JTextArea();
			story.setEditable(true);
			
			JButton addButton = new JButton("Add Player");
			addButton.addActionListener( a-> {
				Player tempP = new Player();
				tempP.setName(name.getText());
				tempP.setAppearance(appearance.getText());
				tempP.setPowers(powers.getText());
				tempP.setStory(story.getText());
				
				conceptDao.addPlayer(tempP, concept.getTitle());
				
				mainPane.remove(addCharacterPanel);
				mainPane.invalidate();
				mainPane.repaint();
				//TODO find out how to resize after addition
			});
			
			addCharacterPanel.add(story);
			addCharacterPanel.add(addButton);
			mainPane.add(addCharacterPanel);
			
		});
		
		addEnemy.addActionListener( e-> {
			JTextArea movePattern = new JTextArea();
			movePattern.setEditable(true);
			
			JButton addButton = new JButton("Add Enemy");
			addButton.addActionListener( a-> {
				Enemy tempE = new Enemy();
				tempE.setName(name.getText());
				tempE.setAppearance(appearance.getText());
				tempE.setPowers(powers.getText());
				tempE.setMovePattern(movePattern.getText());
				
				conceptDao.addEnemy(tempE, concept.getTitle());
				mainPane.remove(addCharacterPanel);
				mainPane.invalidate();
				mainPane.repaint();
			});
			
			addCharacterPanel.add(movePattern);
			addCharacterPanel.add(addButton);
			
			
			mainPane.add(addCharacterPanel);
		});
		
		Component[] elements = mainPane.getComponents();
		
		for(Component component: elements) {
			component.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getButton() == MouseEvent.BUTTON3) {
						popup.show(component, e.getX(), e.getY());
					}
				}

			});
		}
		
		
	}
	
	private void addContextToList(Container mainPane, JList<GameCharacter> characters, ConceptDao conceptDao) {
		JPopupMenu popup = new JPopupMenu();
		JMenuItem edit = new JMenuItem("Edit");
		JMenuItem delete = new JMenuItem("Delete");
		
		popup.add(edit);
		popup.add(delete);
		
		edit.addActionListener( e-> {
			JPanel editCharacterPanel = new JPanel();
			editCharacterPanel.setLayout(new BoxLayout(editCharacterPanel, BoxLayout.Y_AXIS));
				
			JTextField name = new JTextField();
			JTextField powers = new JTextField();
			JTextField appearance = new JTextField();
			JButton editButton = new JButton("Update");
			
			
			editCharacterPanel.add(name);
			editCharacterPanel.add(powers);
			editCharacterPanel.add(appearance);
			
			GameCharacter selected = characters.getSelectedValue();
			if(selected.getClass() == Player.class) {
				JTextArea story = new JTextArea();
				story.setEditable(true);
				editCharacterPanel.add(story);
				
				name.setText(selected.getName());
				powers.setText(selected.getPowers());
				appearance.setText(selected.getAppearance());
				story.setText(((Player)selected).getStory());
				
				editButton.addActionListener( a-> {
					Player tempP = new Player();
					tempP.setId(((Player)selected).getId());
					tempP.setName(name.getText());
					tempP.setAppearance(appearance.getText());
					tempP.setPowers(powers.getText());
					tempP.setStory(story.getText());
					
					conceptDao.getPlayerDao().updatePlayer(tempP);
					
					mainPane.remove(editCharacterPanel);
					mainPane.invalidate();
					mainPane.repaint();
					//TODO find out how to resize after addition
				});
				
			}else {
				JTextArea movePattern = new JTextArea();
				movePattern.setEditable(true);
				editCharacterPanel.add(movePattern);
				
				name.setText(selected.getName());
				powers.setText(selected.getPowers());
				appearance.setText(selected.getAppearance());
				movePattern.setText(((Enemy)selected).getMovePattern());
				
				editButton.addActionListener( a-> {
					Enemy tempE = new Enemy();
					tempE.setId(((Enemy)selected).getId());
					tempE.setName(name.getText());
					tempE.setAppearance(appearance.getText());
					tempE.setPowers(powers.getText());
					tempE.setMovePattern(movePattern.getText());
					
					conceptDao.getEnemyDao().updateEnemy(tempE);
					
					mainPane.remove(editCharacterPanel);
					mainPane.invalidate();
					mainPane.repaint();
					//TODO find out how to resize after addition
				});
				
			}
			editCharacterPanel.add(editButton);
			mainPane.add(editCharacterPanel);
			
		});
		
		delete.addActionListener( e-> {
			GameCharacter selected = characters.getSelectedValue();
			if(selected.getClass() == Player.class) {
				conceptDao.getPlayerDao().deletePlayerById(((Player)selected).getId());
			}else {
				conceptDao.getEnemyDao().deleteEnemyById(((Enemy)selected).getId());
			}
		});
		characters.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getButton() == MouseEvent.BUTTON3) {
						popup.show(characters, e.getX(), e.getY());
					}
				}

		});
	}
}
