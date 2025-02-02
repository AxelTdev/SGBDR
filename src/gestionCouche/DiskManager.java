package gestionCouche;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;

import constantes.Constants;
import object.PageId;

public class DiskManager {
	private static DiskManager instance = null;
	// private static File f =null; //on ne doit pas d�clarer le Fichier ici sinon
	// on ne pourra plus acc�der aux autres fichiers cr��s
	// permet de sotcker pour chaque fichier, le nombre de page existant
	private static HashMap<Integer, Integer> nbr_page_fichier = new HashMap<>(); // de 1 � n

	private static int maxPage = (Constants.pageSize / 4) - 1;
	private DiskManager() {

	}

	public static DiskManager getInstance() {
		if (instance == null) {
			instance = new DiskManager();
		}
		return instance;
	}

	public static void CreateFile(int fileIdx) {
		// DB/mets le fichier dans le repertoire DB
		File f = new File("src/DB/Data_" + fileIdx + ".rf");
		try {
			f.createNewFile();
			// on commence � indexer le ficher cr��
			nbr_page_fichier.put(fileIdx, 0);
		} catch (IOException e) {
			e.getMessage();
		}
	}

	public static PageId AddPage(int fileIdx) throws IOException {
		PageId p = null;
		// si la fichier n'existe pas
		if (!nbr_page_fichier.containsKey(fileIdx)) {
			System.out.println("Le fichier " + fileIdx + " n'existe pas");
			return p;
		}

		try {
			int compteur_page = nbr_page_fichier.get(fileIdx);
			if(compteur_page <= maxPage) {
			p = new PageId(fileIdx, compteur_page + 1);
			// on incr�mente le nbr de page pour le fichier fileIdx (+1)
			nbr_page_fichier.put(fileIdx, nbr_page_fichier.get(fileIdx) + 1);

			byte[] buffer = new byte[Constants.pageSize];

			RandomAccessFile r = new RandomAccessFile("src/DB/Data_" + fileIdx + ".rf", "rw");
			// on se positionne au bon index (� la bonne page) pour ajouter une nouvelle
			// page
			r.seek(Constants.pageSize * (p.getPageIdx()-1));
			r.write(buffer);
			r.close();
			}else {
				System.out.println("trop de pages ont �t� cr�� pour la header page!!");
			}

		} catch (Exception e) {
			System.out.println("Un probl�me est survenu lors de l'ajout de page");
		}
		return p;
	}

	public static void ReadPage(PageId p, byte[] buff) {
		RandomAccessFile r = null;
		try {
			
			r = new RandomAccessFile("src/DB/Data_" + p.getFileIdx() + ".rf", "rw");
			// on se positionne � la bonne page pour commencer � lire
			if (p.getPageIdx() == 0) {
				
				r.read(buff, 0, Constants.pageSize);
			} else {
				
				
				r.seek(Constants.pageSize * (p.getPageIdx()-1));
				r.read(buff);
			}

			r.close();

		} catch (IOException e1) {
			System.out.println("fichier non cr��, on ne peut pas r�cup�rer le buffer");
			e1.getMessage();
			e1.printStackTrace();

		}

	}

	public static void WritePage(PageId p, byte[] buff) {
		RandomAccessFile r = null;
		try {
			r = new RandomAccessFile("src/DB/Data_" + p.getFileIdx() + ".rf", "rw");
			// on se positionne � la bonne page pour commencer � lire
			if (p.getPageIdx() == 0) {
				
				r.write(buff, 0, Constants.pageSize);
			} else {
				
				
				r.seek(Constants.pageSize * (p.getPageIdx()-1));
				r.write(buff);
			}

			r.close();
		} catch (IOException e) {
			e.getMessage();
			e.printStackTrace();
		}

	}

	
}
