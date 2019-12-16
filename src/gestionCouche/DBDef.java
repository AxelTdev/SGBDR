package gestionCouche;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import object.RelDef;

public class DBDef implements Serializable {

	private static final long serialVersionUID = 1L;
	public static ArrayList<RelDef> reldef = new ArrayList<>();//jai mit en public sinon je peux acceder dans la classe FileManager
	public static int nbRelation = 0;//J'ai mit en public aussi pour lutiliser sans insancier et activer la serialisation

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
	
	public void reset() {
		reldef.clear();
		nbRelation = 0;
	}

	/*
	 * elle enregistre dans le fichier catalog.def /**@throws FileNotFoundException
	 * 
	 * @throws IoException
	 */

	public static void serializeDBDef() throws IOException {
		
		String fileName =  "src" + File.separator + "DB" + File.separator + "Catalog.def";
		FileOutputStream fileOut = null;

		try {
			fileOut = new FileOutputStream(fileName);
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		out.writeObject(instance);
		out.close();
		fileOut.close();
		
	}

	public static void deserialiserDBDef() throws ClassNotFoundException, IOException {
		
		String fileName =  "src" + File.separator + "DB" + File.separator + "Catalog.def";
		File file = new File(fileName);

		FileInputStream fis;
		if (file.exists()) {
		
			fis = new FileInputStream(fileName);
			ObjectInputStream ois = new ObjectInputStream(fis);
			instance = (DBDef) ois.readObject();
			ois.close();
		} else {
			
			return;
		}

		

	}

	public static void main(String[] args) {
		
		try {
			
			DBDef.Finish();
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

}
