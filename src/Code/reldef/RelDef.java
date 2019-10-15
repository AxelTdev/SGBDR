package Code.reldef;

public class RelDef {

	private String nomRelation;

	private int nbColonne;

	private String[] typeColonne = { "int", "float", "stringT" };

	public static void create(String s) {

	}

	public int getNbColonne() {
		return this.nbColonne;
	}

	public String[] getTypeColonne() {
		return typeColonne;

	}

	public void setNomRelation(String nomRelation) {
		this.nomRelation = nomRelation;
	}

	public void setNbColonne(int nbColonne) {
		this.nbColonne = nbColonne;
	}

	public void setTypeColonne(String[] typeColonne) {
		this.typeColonne = typeColonne;
	}
}
