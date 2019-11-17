package Code.dbManager;

import java.io.IOException;

import Code.BufferManager.BufferManager;
import Code.dbdef.DBDef;
import Code.reldef.RelDef;
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

	public static void ProcessCommand(String str) {
		String tab[];
		tab = str.split(" ");
		switch (tab[0]) {

		case "create":
			RelDef.create(str);
			break;

		default:
			System.out.println("veuillez réessayer");
			break;
		}

	}

	public static void CreateRelation(String nomRelation, int nbColonne, Code.type.Type[] typeColonne) throws ClassNotFoundException, IOException {
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
	}

}
