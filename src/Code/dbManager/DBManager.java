package Code.dbManager;

import Code.BufferManager.BufferManager;
import Code.dbdef.DBDef;
import Code.reldef.RelDef;
import Code.reldef.RelDef.Type;
import Code.util.Constants;

public class DBManager {

	private static DBManager instance = null;

	private DBManager() {

	}

	public static DBManager Init() {
		if (instance == null) {
			instance = new DBManager();
		}
		DBDef.Init();
		return instance;
	}

	public static void Finish() {
		DBDef.Finish();
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

	public static void CreateRelation(String nomRelation, int nbColonne, Type[] typeColonne) {
		int recordSize = 0;
		for (int i = 0; i < nbColonne; i++) {
			if (typeColonne[i] == Type.INT || typeColonne[i] == Type.FLOAT) {
				recordSize += 4;

			} else if (typeColonne[i] == Type.STRING) {
				
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
