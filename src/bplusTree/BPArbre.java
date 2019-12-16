package bplusTree;

class BPArbre {
	BPArbreNoeud racine;
	int minimumDegreeDe;

	public BPArbre(int minimumDegreeDe) {
		this.minimumDegreeDe = minimumDegreeDe;
	}

	// fonction qui traverse l'arbre
	public void traverse() {
		if (racine != null)
			racine.traverse();

	}

	public void insert(int k) {

		// si l'arbre est vide
		if (racine == null) {
			// memoire pour la racine
			racine = new BPArbreNoeud(minimumDegreeDe, true);
			racine.getTabCle()[0] = k; // inserer cle
			racine.setCurentNumberOfkeys(1); // mise a jour du nombre de cle dans la racine
		} else // si l'arbre est pas vide
		{
			// si la racine est complete l'arbre prend en taille
			if (racine.getCurentNumberOfkeys() == 2 * minimumDegreeDe - 1) {
				// memoire pour la nouvelle racine
				BPArbreNoeud s = new BPArbreNoeud(minimumDegreeDe, false);

				// rendre l'ancienne racine comme fils de la nouvelle racine
				s.getTabBPArbre()[0] = racine;

				// diviser l'ancienne racine et transferer 1 cle vers la nouvelle racine
				s.splitChild(0, racine);

				// la nouvelle racine a deux fils mtn, decider lequel des deux aura la nouvelle
				// cle
				int i = 0;
				if (s.getTabCle()[0] < k)
					i++;
				s.getTabBPArbre()[i].insertNonFull(k);

				// Change racine
				racine = s;
			} else // si la racine n'est pas complete, appeler insertNonFull pour la racine
				racine.insertNonFull(k);
		}
	}

	// fonction pour chercher une cle dans l'arbre
	public BPArbreNoeud search(int k) {
		return (racine == null) ? null : racine.search(k);
	}

	public static void main(String[] args) {
		BPArbre a = new BPArbre(2);
		a.insert(3);
		for (int i = 0; i < 20; i++) {
			a.insert(2);
		}
		a.search(3);
		

	}
}
