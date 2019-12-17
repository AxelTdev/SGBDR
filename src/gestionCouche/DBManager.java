package gestionCouche;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;

import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import constantes.Constants;
import interfacejava.Interface;
import object.Record;
import object.RelDef;
import object.Type;

public class DBManager {
	
	private static DBManager instance = null;
	private static String interfacetxt = "";

	private DBManager() {

	}

	public static DBManager init() throws ClassNotFoundException, IOException {
		if (instance == null) {
			instance = new DBManager();
		}
		DBDef.Init();
		FileManager.Init();
		return instance;
	}

	public static void finish() {
		try {
			DBDef.Finish();
		} catch (IOException e) {

			e.printStackTrace();
		}
		BufferManager u = BufferManager.getInstance();
		u.FlushAll();
	}

	public static void processCommand(String str) throws ClassNotFoundException, IOException {
	
		String tab[];
		tab = str.split(" ");
		switch (tab[0]) {

		case "create":
			createRelation(str);
			break;
		case "clean":
			clean();
			break;
		case "insert":
			insert(str);
			break;
		case "insertall":
			insertall(str);
			break;
		case "selectall":

			selectAll(str);

			break;
		case "join":
			join(str);
			break;
		case "select":
			select(str);
			break;

		case "delete":
			delete(str);
			break;
		case "fill":
			insertall(str);
			break;
		case "interface":
			visuel(str);
			break;
		case "createindex":
			// createindex(str);
			break;
		case "selectindex":
			break;
		default:
			System.out.println("veuillez réessayer");
			break;
		}

	}

	public static void createRelation(String str)
			throws ClassNotFoundException, IOException {
		String tab[];
		tab = str.split(" ");
		int typeCount = tab.length - 3;
		Type[] typeTab = new Type[typeCount];
		for (int i = 3, y = 0; i < tab.length; i++, y++) {
			typeTab[y] = new Type(tab[i]);
		}

		// Conversion du nombre de tab en int
		int nombreColonne = Integer.parseInt(tab[2]);
		int nbColonne = nombreColonne;
		String nomRelation = tab[1];
	
		Type[] typeColonne = typeTab;
		int recordSize = 0;
		for (int i = 0; i < nbColonne; i++) {
			if (typeColonne[i].getValue() == "int" || typeColonne[i].getValue() == "float") {
				recordSize += 4;

			} else if (typeColonne[i].getValue() == "string") {

				int longeur = typeColonne[i].getSize();
				recordSize += 2 * longeur;
			} else {
				System.out.println("type de colonne non reconnu");
			}
		}
		int slotCount = (Constants.pageSize) / recordSize;

		slotCount -= (slotCount / recordSize);
		
		RelDef refdef = new RelDef(DBDef.nbRelation, recordSize, slotCount);

		refdef.setNomRelation(nomRelation);
		refdef.setNbColonne(nbColonne);
		refdef.setTypeColonne(typeColonne);
		DBDef.AddRelation(refdef);
		FileManager.CreateRelationFile(refdef);
		DBManager.interfacetxt = "relation créée";
		
	}
	public void setInterfacetxtempty() {
		DBManager.interfacetxt = "";
	}
	public static void clean() throws ClassNotFoundException, IOException {
		// destruction des fichiers catalog et datasX.rf
		File repertoire = new File("src" + File.separator + "DB");
		File[] contenu = repertoire.listFiles();
		if (contenu != null) {
			for (File file : contenu) {
				file.delete();
			}
		}
		// met à 0 dans le BufferManager
		BufferManager u = BufferManager.getInstance();
		u.FlushAll();
		// vider le tableau de heapfile dans FileManager
		FileManager fm = FileManager.getInstance();
		fm.reset();
		// vider le tableau de reldef de la classe DBDef
		DBDef dbd = DBDef.Init();
		dbd.reset();
		DBManager.interfacetxt = "commande clean executée";

	}

	public static void join(String str) {
		String tab[] = str.split(" ");

		String nomR1 = tab[1];
		String nomR2 = tab[2];
		int col1 = Integer.parseInt(tab[3]);
		int col2 = Integer.parseInt(tab[4]);

		col1--;// pour atteindre la bonne colonne
		col2--;

		FileManager a = FileManager.getInstance();
		List<Record> result =a.join(nomR1, nomR2, col1, col2);
		for(int i = 0; i < result.size(); i++) {
			System.out.println(result.get(i));
			DBManager.interfacetxt += result.get(i);
			DBManager.interfacetxt += "\n";
			
		}
		System.out.println("\n Total records = " + result.size());
		DBManager.interfacetxt += "\n Total records = " + result.size() + "\n";
		DBManager.interfacetxt += "commande join executée";
		
	}

	public static void insert(String str) throws IOException {
		FileManager fm = FileManager.getInstance();
		String[] tab = str.split(" ");
		int valuesSlot = tab.length - 2;

		String[] valuesTab = new String[valuesSlot];

		for (int i = 0, y = 2; i < valuesTab.length; i++, y++) {
			valuesTab[i] = tab[y];
		}

		Record rd = new Record(null);
		rd.setValues(valuesTab);

		fm.InsertRecordInRelation(rd, tab[1]);
		DBManager.interfacetxt = "commande insert executée";
	}

	public static void insertall(String str) throws FileNotFoundException, IOException {
		FileManager fm = FileManager.getInstance();
		String[] tab = str.split(" ");
		String[] valueMiseEnForme = null;
		int nbType = 0;
		// recherche du bon heapfile pour recuperer le reldef et donc le nb type pour
		// initialiser le tableau de valeurs
		for (int i = 0; i < fm.getHeapFile().size(); i++) {
			if (fm.getHeapFile().get(i).getReldef().getNomRelation().equals(tab[1])) {
				nbType = fm.getHeapFile().get(i).getReldef().getNbColonne();
				
			}
		}
		
		String[] valuesTab = new String[nbType];// pas de commande donc -1 et pas -2
		File filecsv = new File(tab[2]);

		try (FileReader fr = new FileReader(filecsv); BufferedReader br = new BufferedReader(fr);) {
			String s = null;

			while ((s = br.readLine()) != null) {

				valueMiseEnForme = s.split(",");

				// insertion des records 1 à 1
				for (int i = 0; i < valuesTab.length; i++) {
					valuesTab[i] = valueMiseEnForme[i];

				}

				Record rd = new Record(null);
				rd.setValues(valuesTab);

				fm.InsertRecordInRelation(rd, tab[1]);

			}
		}
		DBManager.interfacetxt = "commande insertall executée";
	}

	public static List<Record> selectAll(String str) {
		String[] tab = str.split(" ");

		List<Record> listRecord = null;
		FileManager fm = FileManager.getInstance();
		listRecord = fm.SelectAllFromRelation(tab[1]);
		StringBuilder str1 = new StringBuilder();
		for (int i = 0; i < listRecord.size(); i++) {
			str1.append(listRecord.get(i));
			
			str1.append("\n");
		}
		str1.append("\n Total records = " + listRecord.size());
		System.out.println(str1);
		DBManager.interfacetxt +=str1.toString() + "\n";
		DBManager.interfacetxt = "commande selectAll executée";
		
		return listRecord;
	}

	/*
	 * 
	 * Etape 1 : je fait une sélection sur les Records à supprimée, toute la
	 * relation (tous les Records) pour obtenir les Records qui respectent la
	 * condition Etape 2 : Je réinitialise la DataPage Etape 3 : je remets les
	 * Records à la chaine dans le fichier
	 */
	public static void delete(String str) throws IOException {
		FileManager fm = FileManager.getInstance();
		String[] tab = str.split(" ");
		List<Record> listRecordAsupprimer = new ArrayList<>();
		listRecordAsupprimer = fm.SelectFromRelation(tab[1], Integer.parseInt(tab[2]), tab[3]);

		List<Record> listRecordTotal = new ArrayList<>();

		listRecordTotal = fm.SelectAllFromRelation(tab[1]);

		List<Record> listRecordFinal = new ArrayList<>();

		boolean mettre;

		for (int i = 0; i < listRecordTotal.size(); i++) {
			mettre = true;
			for (int y = 0; y < listRecordAsupprimer.size(); y++) {

				if (listRecordTotal.get(i).equals(listRecordAsupprimer.get(y))) {

					mettre = false;
					break;

				}

			}
			if (mettre) {
				listRecordFinal.add(listRecordTotal.get(i));
			}

		}

		// mise a zero du fichier dataPage
		HeapFile hf = null;
		for (int i = 0; i < fm.getHeapFile().size(); i++) {
			if (fm.getHeapFile().get(i).getReldef().getNomRelation().equals(tab[1])) {
				hf = fm.getHeapFile().get(i);

			}
		}
		File f = new File("src/DB/Data_" + hf.getReldef().getFileIdx() + ".rf");
		f.delete();
		hf.createNewOnDisk();

		for (int i = 0; i < listRecordFinal.size(); i++) {
			fm.InsertRecordInRelation(listRecordFinal.get(i), hf.getReldef().getNomRelation());
		}
		System.out.println("Total deleted records = " + listRecordAsupprimer.size());
		DBManager.interfacetxt = "Total deleted records = " + listRecordAsupprimer.size();

	}
public String getInterfacetxt() {
	return DBManager.interfacetxt;
}
	/*
	 * public static void createindex(String str) {
	 * 
	 * 
	 * String[] tab = str.split(" "); // hashMap représentant B+Tree String
	 * nomRelation = tab[1];
	 * 
	 * int ordre = Integer.parseInt(tab[3]);
	 * 
	 * //recuperer les Records String commande = "arg1 " + tab[1];
	 * 
	 * List<Record> recordList = SelectAll(commande);
	 * 
	 * Collections.sort(recordList, new Record(null).new
	 * triParColonne(Integer.parseInt(tab[2])));
	 * 
	 * System.out.println("order des record  " + recordList); Noeud racine = new
	 * Noeud();//noeud racine Arbre Tree = new Arbre(null, ordre); }
	 */

	public static void select(String str) {
		FileManager fm = FileManager.getInstance();
		String[] tab = str.split(" ");
		List<Record> listRecord = new ArrayList<>();
		listRecord = fm.SelectFromRelation(tab[1], Integer.parseInt(tab[2]), tab[3]);
		StringBuilder str1 = new StringBuilder();
		for (int i = 0; i < listRecord.size(); i++) {
			str1.append(listRecord.get(i));
			str1.append("\n");
		}
		str1.append("Total records = " + listRecord.size());
		System.out.println(str1.toString());
		interfacetxt = str1.toString();
	}
	public static void visuel(String str) {
		
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// désactive l'utilisation de la police gras
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				try {
					Interface.createAndShowGUI();
				} catch (IOException e) {

					e.printStackTrace();
				}

			}
		});
	}

	
}