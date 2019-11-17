package Code.reldef;

import Code.type.Type;

public class RelDef {
	static final boolean LOG = true;
	private String nomRelation;

	private int nbColonne;

	private Type[] typeColonne;

	private int fileIdx;

	private int recordSize;

	private int slotCount;

	public RelDef(int fileIdx, int recordSize, int slotCount) {
		this.fileIdx = fileIdx;
		this.recordSize = recordSize;
		this.slotCount = slotCount;

	}

	public static void create(String s) {

	}

	public int getNbColonne() {
		return this.nbColonne;
	}

	public int getFileIdx() {
		return fileIdx;
	}

	public int getSlotCount() {
		return slotCount;
	}

	public void setSlotCount(int v) {
		slotCount = v;
	}

	public int getRecordSize() {
		return recordSize;
	}

	

	public Type[] getTypeColonne() {
		return typeColonne;

	}

	public void setNomRelation(String nomRelation) {
		this.nomRelation = nomRelation;
	}

	public void setNbColonne(int nbColonne) {
		this.nbColonne = nbColonne;
	}

	public void setTypeColonne(Type[] typeColonne) {
		this.typeColonne = typeColonne;
	}
}
