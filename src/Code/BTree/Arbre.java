package Code.BTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Code.Rid.Rid;

public class Arbre {
	private Noeud racine;
	private int ordre;

	public Arbre(Noeud racine, int ordre) {
		this.racine = racine;
		this.ordre = ordre;

	}

	/*
	 * 
	 * methode pour chargement par lot des données
	 * 
	 * @param donnee liste de données à inserer dans larbre
	 */
	public void bulkLoad(List<Integer> donnee) {
		
		
		
		
		for (int i = 0; i < donnee.size(); i++) {

			NoeudInterm ndi = new NoeudInterm(new Noeud(), new Noeud());//noeud racine et noeud supplementaire
			Feuille f = new Feuille(ndi);
			
			EntreeDIndex ei = new EntreeDIndex(1, f);//clef et noeud (ici feuille)
			
			this.rajouterEntree(ndi, ei);//rajoute un index entre noeud intermediare et entreedindex
			
			EntreeDeDonnees e = new EntreeDeDonnees(1);
			
			this.rajouterEntree(f, e);
			
			e.getRidList().add(donnee.get(0));    
		}

	}

	/*
	 * methode pour chercher un noeud fils
	 * 
	 */
	public Noeud chercherFils(NoeudInterm nd, int cle) {
		Noeud noeudFils = null;
		for (int i = 0; i < nd.getListEntreeIndex().size(); i++) {
			if (nd.getListEntreeIndex().get(i).getClef() == cle) {
				noeudFils = (Noeud) nd.getListEntreeIndex();
			}
			break;
		}
		return noeudFils;
	}

	/*
	 * methode pour rajouter une entree de donnee dans une feuille
	 * 
	 */
	public void rajouterEntree(Feuille f, EntreeDeDonnees ed) {
		f.getEntreeDeDonnee().add(ed);
	}

	/*
	 * 
	 * methode pour rentree une entree d'indexs dans un noeud intermediare
	 * 
	 * 
	 */
	public void rajouterEntree(NoeudInterm n, EntreeDIndex ei) {
		n.getListEntreeIndex().add(ei);
	}

	public Noeud diviser(Noeud n) {
		// diviser un noeud
		return n;

	}

	public static void main(String[] args) {
		List<Integer> data = new ArrayList<>();
		
		data.add(7);data.add(5);data.add(2);
		data.add(19);data.add(19);data.add(14);
		data.add(16);data.add(19);data.add(15);
		data.add(15);data.add(1);data.add(19);
		//sort list
		Collections.sort(data);
		System.out.println(data);
		Arbre a = new Arbre(new Noeud(), 2);
		
			  a.bulkLoad(data);
	}

}
