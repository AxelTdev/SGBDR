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

				int longeurInt = typeColonne[i].getSize();
				recordSize += 2 * longeurInt;
			} else {
				System.out.println("type de colonne non reconnu");
			}
		}
		int slotCount = Constants.pageSize / recordSize;

		RelDef refdef = new RelDef(DBDef.Init().getNbRelation(), recordSize, slotCount);
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

	public static void Insert(String str) {
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
		String[] tab = str.split(" ");

		File filecsv = new File(tab[2]);
		List<String> listCommand = new ArrayList<>();
		try (FileReader fr = new FileReader(filecsv); BufferedReader br = new BufferedReader(fr);) {
			while (br != null) {
				listCommand.add(br.readLine());
			}
		}
		for (int i = 0; i < listCommand.size(); i++) {
			Insert(listCommand.get(i));
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

		}
		str1.append("Total records = " + tabRecord.length);
		System.out.println(str);
	}
	
	public static void Select(String str) {
		FileManager fm = FileManager.getInstance();
		String[] tab = str.split(" ");
		List<Record> listRecord = new ArrayList<>();
		listRecord = fm.SelectFromRelation(tab[1], Integer.parseInt(tab[2]), tab[3]);
		StringBuilder str1 = new StringBuilder();
		for (int i = 0; i < listRecord.size(); i++) {
			str1.append(listRecord.size());

		}
		str1.append("Total records = " + listRecord.size());
		System.out.println(str);
	}
	

	public static void main(String[] args) {
		try {
			DBManager.ProcessCommand("create R 2 int int");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
