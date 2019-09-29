package Code.dbManager;

import Code.dbdef.DBDef;

public class DBManager {
	
	private static DBManager instance = null;

	private DBManager() {

	}
	

	public static DBManager Init() {
		if(instance==null) {
			instance=new DBManager();
		}
		DBDef.Init();
		return instance ;
	}

	public static void Finish() {
		DBDef.Finish();
	}

	public static void ProcessCommand(String str) {

	}

}
