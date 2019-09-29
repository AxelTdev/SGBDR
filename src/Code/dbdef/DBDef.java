package Code.dbdef;

import java.util.ArrayList;

import Code.reldef.RelDef;

public class DBDef {
	
	private ArrayList <RelDef> reldef=new ArrayList<>();
	private int nbRelation=0;
	
	private static DBDef instance = null;

	private DBDef() {	
	}
	
	//pour rendre la classe à instance unique
	public static DBDef Init() {
		if(instance==null) {
			instance = new DBDef ();
		}
		return instance;
	}

	public static void Finish() {
		
	}

	public void AddRelation(RelDef reldel) {

		reldef.add(reldel);
		nbRelation++;
	}

}
