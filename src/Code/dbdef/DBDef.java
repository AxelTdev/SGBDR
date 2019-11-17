package Code.dbdef;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import Code.reldef.RelDef;

public class DBDef implements Serializable {

	private static final long serialVersionUID = 1L;
	private static ArrayList<RelDef> reldef = new ArrayList<>();
	private static int nbRelation = 0;

	private static DBDef instance = null;

	private DBDef() {
	}

	// pour rendre la classe à instance unique
	public static DBDef Init() throws ClassNotFoundException, IOException {
		if (instance == null) {
			instance = new DBDef();

			DBDef.deserialiserDBDef();

		}
		return instance;
	}

	public static void Finish() throws IOException {
		serializeDBDef();
	}

	public static void AddRelation(RelDef reldel) {

		reldef.add(reldel);
		nbRelation++;
	}

	public int getNbRelation() {
		return nbRelation;
	}

	/*
	 * elle enregistre dans le fichier catalog.def /**@throws FileNotFoundException
	 * 
	 * @throws IoException
	 */

	public static void serializeDBDef() throws IOException {
		System.out.println("Debut serialization DBDef");
		String fileName =  "src" + File.separator + "DB" + File.separator + "Catalog.def";
		FileOutputStream fileOut = null;

		try {
			fileOut = new FileOutputStream(fileName);
		} catch (FileNotFoundException e) {
			System.out.println("Fichier Introuvable + " + fileName);
			e.printStackTrace();
		}
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		out.writeObject(instance);
		out.close();
		fileOut.close();
		System.out.println("Fin serialization DBDef");
	}

	public static void deserialiserDBDef() throws ClassNotFoundException, IOException {
		System.out.println("Verifier si Catalog.def existe");
		String fileName =  "src" + File.separator + "DB" + File.separator + "Catalog.def";
		File file = new File(fileName);

		FileInputStream fis;
		if (file.exists()) {
			System.out.println(" fichier Catalog.def trouve");
			System.out.println(" debut  deserialiserDBDef du DBDef");
			fis = new FileInputStream(fileName);
			ObjectInputStream ois = new ObjectInputStream(fis);
			instance = (DBDef) ois.readObject();
			ois.close();
		} else {
			System.out.println(" fichier Catalog.def introuvable");
			return;
		}

		System.out.println("Fin Deserialiser DBDef");

	}

	public static void main(String[] args) {
		
		try {
			
			DBDef.Finish();
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

}
