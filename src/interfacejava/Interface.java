package interfacejava;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Interface extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// attribut de la fenetre

	static private final String newline = "\n";
	JButton openButton, saveButton, ajoute_host, compare;
	static JButton afficher_la_liste;
	static JButton retirer_une_image;
	static JButton vide_ressource;
	static JButton recherche_image;
	static JButton graphique;
	static JButton graph_methode;

	private JTextArea log;
	private JFileChooser fc;

	// attributs fondamentaux
	private ArrayList<Image> images;
	private static Image hostImage;
	//private static ArrayList<ModeleLBP> histos;
	//private ModeleLBP hostLBP;
	private Object[] result1;
	private Object[] method1;
	private Object[] method2;
	private Object[] method3;

	// methode pour ajouter les bouttons au container

	public Interface() {

		super(new BorderLayout());// héritage avec Jpanel
		images = new ArrayList<Image>();
		hostImage = null;

		// cr�ation de la console et configuration de ces dimensions
		log = new JTextArea(25, 45);
		log.setMargin(new Insets(5, 5, 5, 5));
		log.setEditable(false);
		JScrollPane logScrollPane = new JScrollPane(log);

		// Cr�ation de l'objet permetant la boite de dialogue
		fc = new JFileChooser();

		// accepte les chemins de fichiers et de r�pertoires
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

		// declaration du bouton
		openButton = new JButton("create");
		// bouton sensible aux �v�nements
		openButton.addActionListener(this);

		// creation des objects boutons et ajout des boutons � l'interception des
		// actions de l'utilisateur avec ces boutons

		ajoute_host = new JButton("insert");
		ajoute_host.addActionListener(this);
		compare = new JButton("insertall");
		compare.addActionListener(this);
		afficher_la_liste = new JButton("delete");
		afficher_la_liste.addActionListener(this);
		retirer_une_image = new JButton("createindex");
		retirer_une_image.addActionListener(this);
		vide_ressource = new JButton("selectindex");
		vide_ressource.addActionListener(this);
		recherche_image = new JButton("join");
		recherche_image.addActionListener(this);
		saveButton = new JButton("clean");
		saveButton.addActionListener(this);
		graphique = new JButton("exit");
		graphique.addActionListener(this);

		// Panel barre supérieure
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(openButton);
		buttonPanel.add(ajoute_host);
		buttonPanel.add(compare);
		buttonPanel.add(saveButton);
		buttonPanel.add(graphique);

		// Panel barre du bas
		JPanel buttonPanel2 = new JPanel();
		buttonPanel2.add(afficher_la_liste);
		buttonPanel2.add(retirer_une_image);
		buttonPanel2.add(vide_ressource);
		buttonPanel2.add(recherche_image);

		add(buttonPanel2, BorderLayout.SOUTH);// situe en barre du bas
		add(buttonPanel, BorderLayout.PAGE_START);// situe les objets bouttons dans la partie sup�rieur de la fenetre
		add(logScrollPane, BorderLayout.CENTER);// situe la console dans la partie centrale de la fenetre

	}

	public void actionPerformed(ActionEvent e) {

		// manipuler boutton ajouterimages
		if (e.getSource() == openButton) {// selon le boutton choisit
			
			// manipuler boutton saveButton
		}
		if (e.getSource() == saveButton) {
			

		}
		// manipuler boutton comparaison
		if (e.getSource() == compare) {
			
		}
		// manipuler le boutton pour choisir l'image hote
		if (e.getSource() == ajoute_host) {

			

		}

		// manipuler boutton vider
		
		
	}

	// C'est la methode de la cr�ation de la fenetre elle-meme nomm�e frame
	private static void createAndShowGUI() throws IOException {
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

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// désactive l'utilisation de la police gras
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				try {
					createAndShowGUI();
				} catch (IOException e) {

					e.printStackTrace();
				}

			}
		});

	}
}
