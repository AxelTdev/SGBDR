package Code.reldef;

public class RelDef {

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

	public enum Type {
		INT("int"), FLOAT("float"), STRING("string");

		private String value;
		private int size;

		Type(String type) {
			if (type.startsWith("string")) {
				
				this.value = "string";
			} else {
				this.value = type;
				this.size = 0;
			}

		}

		

		public String getType() {
			if (size > 0)
				return value + size;
			else
				return value;
		}

		public void setSize(int size) {
			this.size = size;
		}

		public int getSize() {
			return this.size;
		}
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
