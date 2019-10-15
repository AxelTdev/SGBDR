package Code.dbdef;

import java.util.ArrayList;

import Code.reldef.RelDef;

public class DBDef {
	
	private static ArrayList <RelDef> reldef=new ArrayList<>();
	private static int nbRelation=0;
	
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

	public static void AddRelation(RelDef reldel) {

		reldef.add(reldel);
		nbRelation++;
	}
	
	public int getNbRelation() {
		return nbRelation;
	}

}
