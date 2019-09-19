package dbdef;

import reldef.RelDef;

public class DBDef {
	private RelDef[] reldef;
	private int nbRelation;

	public void Init() {

	}

	public void Finish() {

	}

	public void AddRelation(RelDef reldel) {

		reldef[nbRelation] = reldel;
		nbRelation++;
	}

}
