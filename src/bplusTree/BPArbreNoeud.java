package bplusTree;

import java.util.Arrays;

public class BPArbreNoeud {
	private int[] tabCle;
	private int minimumDegre;
	private BPArbreNoeud tabBPArbre[];
	private int curentNumberOfkeys;
	private boolean estFeuille;

	public BPArbreNoeud(int t, boolean leaf) {
		// copie du degre minimum donne et les proprietes de la feuille
		minimumDegre = t;
		estFeuille = leaf;

		// memoire pour le nbr max de cles possible et fils
		tabCle = new int[2 * minimumDegre - 1];
		tabBPArbre = new BPArbreNoeud[2 * minimumDegre];

		// on initialise le nombre de cles a 0
		curentNumberOfkeys = 0;
	}

	// fonction pour inserer une nouvelle cle dans le sous arbre avec le noeud pour
	// racine.
	// la condition est que le noeud ne doit pas etre complet qd on appelle la
	// fonction
	public void insertNonFull(int k) {
		// on initialise l'index a l'index de l'element le plus a droite
		int i = curentNumberOfkeys - 1;

		// If this is a leaf node
		if (estFeuille == true) {
			// La boucle fait deux choses
			// a) trouver ou mettre la nouvelle cle pour l'inserer
			// b) deplacer toutrs les plus grandes cles vers un seul endroit
			while (i >= 0 && tabCle[i] > k) {
				tabCle[i + 1] = tabCle[i];
				i--;
			}

			// inserer la nouvelle cle à la place trouvee precedemment
			tabCle[i + 1] = k;
			curentNumberOfkeys = curentNumberOfkeys + 1;
		} else // If this node is not leaf
		{
			// trouver le fils qui deva recevoir la nouvelle cle
			while (i >= 0 && tabCle[i] > k)
				i--;

			// verifier si le fils trouve est complet
			if (tabBPArbre[i + 1].curentNumberOfkeys == 2 * minimumDegre - 1) {
				// si le fils est complet, on le divise
				splitChild(i + 1, tabBPArbre[i + 1]);

				// apres separation, la cle du milieu de C[i] va au dessus et
				// C[i] est separe en deux. mtn on doit decider lequel va avoir la cle
				if (tabCle[i + 1] < k)
					i++;
			}
			tabBPArbre[i + 1].insertNonFull(k);
		}

	}

	// fonction pour diviser Child y du noeud . i est l'index. Child y doit etre
	// complet lorsqu'on appelle la fonction
	public void splitChild(int i, BPArbreNoeud y) {
		// creer un noeud qui va stocker (t-1) cles de y
		BPArbreNoeud z = new BPArbreNoeud(y.minimumDegre, y.estFeuille);
		z.curentNumberOfkeys = minimumDegre - 1;

		// copier la derniere (t-1) cle de y vers z
		for (int j = 0; j < minimumDegre - 1; j++)
			z.getTabCle()[j] = y.getTabCle()[j + minimumDegre];

		// copie du dernier t child de y vers z
		if (y.estFeuille == false) {
			for (int j = 0; j < minimumDegre; j++)
				z.getTabBPArbre()[j] = y.getTabBPArbre()[j + minimumDegre];
		}

		// reduire le nbr de clés dans y
		y.curentNumberOfkeys = minimumDegre - 1;

		// comme le noeud va avoir un nouveau fils on cree l'espace pour ce dernier
		for (int j = curentNumberOfkeys; j >= i + 1; j--)
			tabBPArbre[j + 1] = tabBPArbre[j];

		// relier le nouveau Child au noeud
		tabBPArbre[i + 1] = z;

		// une cle de y va bouger vers ce noeud, trouver la location de la
		// nouvelle cle et toutes les plus grandes cles vont vers le meme endroit
		for (int j = curentNumberOfkeys - 1; j >= i; j--)
			tabCle[j + 1] = tabCle[j];

		// copie de la cle du milieu de y vers ce noeud
		tabCle[i] = y.tabCle[minimumDegre - 1];

		// incrementer le compteur de cles dans ce noeud
		curentNumberOfkeys = curentNumberOfkeys + 1;
	}

	// fonction pour traverser tous les noeuds
	// du sous arbre enracine a ce noeud
	public void traverse() {
		// il y a n clés et n+1 enfants, traverse a travers n clés
		// et les premiers n enfants
		int i;
		for (i = 0; i < curentNumberOfkeys; i++) {
			// If this is not leaf, then before printing key[i],
			// traverse the subArbre rooted with child C[i].
			if (estFeuille == false)
				tabBPArbre[i].traverse();
			System.out.println(tabCle[i]);
		}

		// Print the subArbre rooted with last child
		if (estFeuille == false)
			tabBPArbre[i].traverse();
	}

	// A function to search a key in subArbre rooted with this node.
	public BPArbreNoeud search(int k) // returns NULL if k is not present.
	{
		// Find the first key greater than or equal to k
		int i = 0;

		while (i < curentNumberOfkeys && k > tabCle[i]) {
			System.out.println("tabcle : + " + tabCle[i]);
			i++;

		}
		// si la cle trouvee est egale a k, return ce noeud
		System.out.println(i);
		System.out.println(Arrays.toString(tabCle));
		if (tabCle[i] == k)
			return this;

		// si la cle n'est pas trouvee et que c'est un noeud feuille
		if (estFeuille == true)
			return null;

		// aller vers le fils
		return tabBPArbre[i].search(k);
	}

	/**
	 * @return le tabCle
	 */
	public int[] getTabCle() {
		return tabCle;
	}

	/**
	 * @return le minimumDegre
	 */
	public int getMinimumDegre() {
		return minimumDegre;
	}

	/**
	 * @return le tabBPArbre
	 */
	public BPArbreNoeud[] getTabBPArbre() {
		return tabBPArbre;
	}

	/**
	 * @return le curentNumberOfkeys
	 */
	public int getCurentNumberOfkeys() {
		return curentNumberOfkeys;
	}

	/**
	 * @return le estFeuille
	 */
	public boolean isEstFeuille() {
		return estFeuille;
	}

	/**
	 * @param tabCle the tabCle to set
	 */
	public void setTabCle(int[] tabCle) {
		this.tabCle = tabCle;
	}

	/**
	 * @param minimumDegre the minimumDegre to set
	 */
	public void setMinimumDegre(int minimumDegre) {
		this.minimumDegre = minimumDegre;
	}

	/**
	 * @param tabBPArbre the tabBPArbre to set
	 */
	public void setTabBPArbre(BPArbreNoeud[] tabBPArbre) {
		this.tabBPArbre = tabBPArbre;
	}

	/**
	 * @param curentNumberOfkeys the curentNumberOfkeys to set
	 */
	public void setCurentNumberOfkeys(int curentNumberOfkeys) {
		this.curentNumberOfkeys = curentNumberOfkeys;
	}

	/**
	 * @param estFeuille the estFeuille to set
	 */
	public void setEstFeuille(boolean estFeuille) {
		this.estFeuille = estFeuille;
	}

	public static void main(String[] args) {
		BPArbre t = new BPArbre(2);
		// BArbre t(3); // A B-Arbre with minium degree 1
		t.insert(3);
		t.insert(4);
		t.insert(6);
		t.insert(9);
		t.insert(10);
		t.insert(11);
		t.insert(12);
		t.insert(13);
		t.insert(20);
		t.insert(22);
		t.insert(23);
		t.insert(31);
		t.insert(35);
		t.insert(36);
		t.insert(38);
		t.insert(41);
		t.insert(44);

		System.out.println("Traversal of the constucted Arbre is ");
		t.traverse();
		/*
		 * int k = 6; if(t.search(k) != null) System.out.println("\nPresent"); else
		 * System.out.println( "\nNot Present");
		 */
		int k = 44;
		if (t.search(k) != null)
			System.out.println("\nPresent");
		else
			System.out.println("\nNot Present");
	}

}
