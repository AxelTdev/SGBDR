package interfacejava;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import java.io.IOException;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import gestionCouche.DBManager;
/**
 * 
 * @author Axel
 * 
 * classe servant a afficher la console
 *
 */
public class Interface extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;


	JButton openButton;

	private JTextArea log;
	private JFileChooser fc;
	private static int compt = 0;


	public Interface() {

		super(new BorderLayout());// héritage avec Jpanel

		// cr�ation de la console et configuration de ces dimensions
		log = new JTextArea(25, 45);
		log.setMargin(new Insets(5, 5, 5, 5));
		log.setEditable(true);

		JScrollPane logScrollPane = new JScrollPane(log);
		KeyListener listener = new KeyListener() {

			@Override

			public void keyPressed(KeyEvent event) {

				if (event.getKeyCode() == KeyEvent.VK_ENTER) {
					compt++;
					if (compt % 2 == 1) {
						try {
							DBManager db = DBManager.init();
							db.createRelation(log.getText());//je suis obligé de le laisser pour recuperer le texte de bd
							log.append("\n");
							log.append(db.getInterfacetxt());
							db.setInterfacetxtempty();
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					} else {
						log.setText("");
					}
				}

			}

			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

		};

		log.addKeyListener(listener);
		// Cr�ation de l'objet permetant la boite de dialogue
		fc = new JFileChooser();

		// accepte les chemins de fichiers et de r�pertoires
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

		// declaration du bouton
		openButton = new JButton("ajouter .csv");
		// bouton sensible aux �v�nements
		openButton.addActionListener(this);

		// creation des objects boutons et ajout des boutons � l'interception des
		// actions de l'utilisateur avec ces boutons

		// Panel barre supérieure
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(openButton);

		add(buttonPanel, BorderLayout.NORTH);// situe les objets bouttons dans la partie sup�rieur de la fenetre
		add(logScrollPane, BorderLayout.CENTER);// situe la console dans la partie centrale de la fenetre

	}

	public String verifObjectList(Object o) {
		if (o instanceof List) {
		}
		return null;

	}

	public void actionPerformed(ActionEvent e) {

		// manipuler boutton ajouterimages
		if (e.getSource() == openButton) {// selon le boutton choisit

			fc.setCurrentDirectory(new File(System.getProperty("user.home")));
			int result = fc.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION) {
				File csv = fc.getSelectedFile();
				System.out.println("Selected file: " + csv.getAbsolutePath());
				if (csv.renameTo(new File(csv.getName()))) {
					log.append("ficher importé");
				} else {
					System.out.println("fichier non importé");
				}
			}

		}

		// manipuler boutton vider

	}

	// C'est la methode de la cr�ation de la fenetre elle-meme nomm�e frame
	public static void createAndShowGUI() throws IOException {
		// Cr�ation de la fenetre
		JFrame frame = new JFrame("Mini Base de Donnée");// declaration de la fenetre
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// ajout des �l�ments buttons, log, container, fc.. dans la fenetre
		frame.setLayout(new GridLayout(1, 0));

		frame.getContentPane().add(new Interface());

		// la fenetre apparait
		frame.pack();// taille de la taille selon les �l�ments dedans
		frame.setVisible(true);// visible

	}

	public static void main(String[] args) {

	}
}
