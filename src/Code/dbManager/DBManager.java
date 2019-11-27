package Code.dbManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Code.BufferManager.BufferManager;
import Code.dbdef.DBDef;
import Code.fileManager.FileManager;
import Code.record.Record;
import Code.reldef.RelDef;
import Code.type.Type;
import Code.util.Constants;

public class DBManager {
	static final boolean LOG = true;
	private static DBManager instance = null;

	private DBManager() {

	}

	public static DBManager Init() throws ClassNotFoundException, IOException {
		if (instance == null) {
			instance = new DBManager();
		}
		DBDef.Init();
		FileManager.Init();
		return instance;
	}

	public static void Finish() {
		try {
			DBDef.Finish();
		} catch (IOException e) {

			e.printStackTrace();
		}
		BufferManager u = BufferManager.getInstance();
		u.FlushAll();
	}

	public static void ProcessCommand(String str) throws ClassNotFoundException, IOException {
		String tab[];
		tab = str.split(" ");
		switch (tab[0]) {

		case "create":
			int typeCount = tab.length - 3;
			Type[] typeTab = new Type[typeCount];
			for (int i = 3, y = 0; i < tab.length; i++, y++) {
				typeTab[y] = new Type(tab[i]);
			}

			// Conversion du nombre de tab en int
			int nbCol = Integer.parseInt(tab[2]);
			CreateRelation(tab[1], nbCol, typeTab);
			break;
		case "clean":
			Clean();
			break;
		case "insert":
			Insert(str);
			break;
		case "insertall":
			Insertall(str);
			break;
		case "selectall":

			SelectAll(str);

			break;
		case "select":
			Select(str);
			break;

		default:
			System.out.println("veuillez réessayer");
			break;
		}

	}

	public static void CreateRelation(String nomRelation, int nbColonne, Type[] typeColonne)
			throws ClassNotFoundException, IOException {
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
		System.out.println("record size  dezqdssssdqsdqsdqsdsqd" + recordSize);
		System.out.println("slot scount  dezedzdzdez" + (slotCount - 2));
		RelDef refdef = new RelDef(DBDef.nbRelation, recordSize, slotCount - 2);// j'ai un problème là

		refdef.setNomRelation(nomRelation);
		refdef.setNbColonne(nbColonne);
		refdef.setTypeColonne(typeColonne);
		DBDef.AddRelation(refdef);
		FileManager.CreateRelationFile(refdef);
	}

	public static void Clean() throws ClassNotFoundException, IOException {
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

	}

	public static void Insert(String str) throws IOException {
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

	}

	public static void Insertall(String str) throws FileNotFoundException, IOException {
		FileManager fm = FileManager.getInstance();
		String[] tab = str.split(" ");
		String[] valueMiseEnForme = null;
		int nbType = 0;
		// recherche du bon heapfile pour recuperer le reldef et donc le nb type pour
		// initialiser le tableau de valeurs
		for (int i = 0; i < fm.getHeapFile().size(); i++) {
			if (fm.getHeapFile().get(i).getReldef().getNomRelation().equals(tab[1])) {
				nbType = fm.getHeapFile().get(i).getReldef().getNbColonne();
				System.out.println("ddddddddddddddddddddddddddddddddslot count ee"
						+ fm.getHeapFile().get(i).getReldef().getSlotCount());
			}
		}
		System.out.println("nb element" + tab.length);
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

	}

	public static void SelectAll(String str) {
		String[] tab = str.split(" ");

		Record[] tabRecord = null;
		FileManager fm = FileManager.getInstance();
		tabRecord = fm.SelectAllFromRelation(tab[1]);
		StringBuilder str1 = new StringBuilder();
		for (int i = 0; i < tabRecord.length; i++) {
			str1.append(tabRecord[i]);
			str1.append("\n");
		}
		str1.append("\n Total records = " + tabRecord.length);
		System.out.println(str1);
	}

	public static void Select(String str) {
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
	}

	public static void main(String[] args) {
		try {
			// probleme avec les strings
			/*
			 * DBManager.ProcessCommand("clean");
			 * 
			 * DBManager.ProcessCommand("create R1 3 int string2 int");
			 * 
			 * for(int i = 0; i < 191; i++) { DBManager.ProcessCommand("insert R1 5 de 5");
			 * } DBManager.ProcessCommand("selectall R1");
			 */

			DBManager.ProcessCommand("create S 8 string2 int string4 float string5 int int int");

			DBManager.ProcessCommand("insertall S S1.csv");

			DBManager.ProcessCommand("selectall S ");

			DBManager.ProcessCommand("select S 2 19");

			DBManager.ProcessCommand("select S 3 Nati");

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
