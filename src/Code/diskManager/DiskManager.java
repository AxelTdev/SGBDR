package Code.diskManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;

import Code.pages.PageId;
import Code.util.Constants;

public class DiskManager {
	private static DiskManager instance = null;
	// private static File f =null; //on ne doit pas déclarer le Fichier ici sinon
	// on ne pourra plus accéder aux autres fichiers créés
	// permet de sotcker pour chaque fichier, le nombre de page existant
	private static HashMap<Integer, Integer> nbr_page_fichier = new HashMap<>(); // de 1 à n

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
			// on commence à indexer le ficher créé
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
			p = new PageId(fileIdx, compteur_page + 1);
			// on incrémente le nbr de page pour le fichier fileIdx (+1)
			nbr_page_fichier.put(fileIdx, nbr_page_fichier.get(fileIdx) + 1);

			byte[] buffer = new byte[Constants.pageSize];

			RandomAccessFile r = new RandomAccessFile("src/DB/Data_" + fileIdx + ".rf", "rw");
			// on se positionne au bon index (à la bonne page) pour ajouter une nouvelle
			// page
			r.seek(Constants.pageSize * (p.getPageIdx() - 1));
			r.write(buffer);
			r.close();

		} catch (Exception e) {
			System.out.println("Un problème est survenu lors de l'ajout de page");
		}
		return p;
	}

	public static void ReadPage(PageId p, byte[] buff) {
		RandomAccessFile r = null;
		try {
			
			r = new RandomAccessFile("src/DB/Data_" + p.getFileIdx() + ".rf", "rw");
			// on se positionne à la bonne page pour commencer à lire
			if (p.getPageIdx() == 0) {
				
				r.read(buff, 0, 100);
			} else {
				
				System.out.println(Constants.pageSize * (p.getPageIdx() - 1));
				r.seek(Constants.pageSize * (p.getPageIdx() - 1));
				r.read(buff);
			}

			r.close();

		} catch (IOException e1) {
			System.out.println("fichier non créé, on ne peut pas récupérer le buffer");
			e1.getMessage();
			e1.printStackTrace();

		}

	}

	public static void WritePage(PageId p, byte[] buff) {
		RandomAccessFile r = null;
		try {
			r = new RandomAccessFile("src/DB/Data_" + p.getFileIdx() + ".rf", "rw");
			// on se positionne à la bonne page pour commencer à lire
			if (p.getPageIdx() == 0) {
				
				r.write(buff, 0, 100);
			} else {
				
				System.out.println(Constants.pageSize * (p.getPageIdx() - 1));
				r.seek(Constants.pageSize * (p.getPageIdx() - 1));
				r.write(buff);
			}

			r.close();
		} catch (IOException e) {
			e.getMessage();
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws IOException {

		// test du DiskManager
		DiskManager.CreateFile(1);

		PageId pageId = DiskManager.AddPage(1);
		byte[] buff = new byte[Constants.pageSize];
		String str = "bonjour";
		DiskManager.WritePage(pageId, str.getBytes());

		/*
		 * PageId pageId2 = DiskManager.AddPage(1); byte[] buff2 = new
		 * byte[Constants.pageSize]; String str2 = "llllll";
		 * DiskManager.WritePage(pageId2, str2.getBytes());
		 */

		DiskManager.ReadPage(pageId, buff);

		for (int i = 0; i < buff.length; i++) {
			System.out.println(buff[i]);
		}
		System.out.println("****************************************");
		/*
		 * DiskManager.ReadPage(pageId2, buff2);
		 * 
		 * for (int i = 0; i < buff2.length; i++) { System.out.println(buff2[i]); }
		 * 
		 * }
		 */

	}
}
