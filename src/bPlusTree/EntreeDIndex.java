package Code.BTree;

public class EntreeDIndex {
	private int clef;
	private Noeud fils;
	
	public EntreeDIndex(int clef, Noeud fils) {
		this.clef = clef;
		this.fils = fils;
	}
	public int getClef() {
		return clef;
	}
	public Noeud getFils() {
		return fils;
	}
}

