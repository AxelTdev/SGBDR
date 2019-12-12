package Code.BTree;

import java.util.ArrayList;
import java.util.List;

public class NoeudInterm extends Noeud {
	private List<EntreeDIndex> entreeIndex;
	private Noeud pointeurSup;
	
	public NoeudInterm(Noeud parent, Noeud pointeurSup){
		super(parent);
		this.entreeIndex = new ArrayList<>();
		this.pointeurSup = pointeurSup;
	}
	
	public Noeud getPointeurSup() {
		return pointeurSup;
	}
	public List<EntreeDIndex> getListEntreeIndex(){
		return entreeIndex;
	}
	
	
	
}
