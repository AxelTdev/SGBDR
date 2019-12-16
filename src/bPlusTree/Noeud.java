package Code.BTree;

public class Noeud {

	private Noeud parent;

	public Noeud(Noeud parent) {
		this.parent = parent;
	}
	
	public Noeud() {//constructeur noeud racine
		this.parent = null;
	}
	public Noeud getNoeudParent() {
		return parent;
	}
	public void setNoeudParent(Noeud n) {
		this.parent = n;
	}

}
