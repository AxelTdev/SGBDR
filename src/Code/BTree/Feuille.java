package Code.BTree;

import java.util.ArrayList;
import java.util.List;

public class Feuille extends Noeud{
	private List<EntreeDeDonnees> entreeDonne;
	
	public Feuille(Noeud parent) {
		super(parent);
		this.entreeDonne = new ArrayList<>();
	}
	
	public List<EntreeDeDonnees> getEntreeDeDonnee(){
		return entreeDonne;
	}
	public Noeud getNoeudParent() {
		return super.getNoeudParent();
	}
	
}
