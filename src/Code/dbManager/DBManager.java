package Code.dbManager;

import Code.BufferManager.BufferManager;
import Code.dbdef.DBDef;
import Code.reldef.RelDef;

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

	public static void CreateRelation(String nomRelation, int nbColonne, String[] typeColonne) {
		RelDef refdef = new RelDef();
		refdef.setNomRelation(nomRelation);
		refdef.setNbColonne(nbColonne);
		refdef.setTypeColonne(typeColonne);
		DBDef.AddRelation(refdef);
	}

}
