package Code.diskManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import Code.pages.PageId;
import Code.util.Constants;

public class DiskManager {
	private static int compteur_page = 1;
	private static DiskManager instance = null;

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
		File file = new File("src/DB/Data_" + fileIdx + ".rf");
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.getMessage();
		}
	}

	public static PageId AddPage(int fileIdx) throws IOException {

		PageId p = new PageId(fileIdx, compteur_page);
		compteur_page++;
		byte[] buffer = new byte[Constants.pageSize];

		RandomAccessFile r = new RandomAccessFile("src/DB/Data_" + fileIdx + ".rf", "rw");
		r.write(buffer);
		r.close();

		return p;
	}

	public static void ReadPage(PageId p, byte[] buff) {
		RandomAccessFile r = null;
		try {
			r = new RandomAccessFile("src/DB/Data_" + p.getFileIdx() + ".rf", "rw");
		} catch (FileNotFoundException e1) {
			e1.getMessage();
			e1.printStackTrace();
		}
		try {
			r.read(buff);
		} catch (IOException e) {
			e.getMessage();
			e.printStackTrace();
		}

	}

	public static void WritePage(PageId p, byte[] buff) {
		RandomAccessFile r = null;
		try {
			r = new RandomAccessFile("src/DB/Data_" + p.getFileIdx() + ".rf", "rw");
		} catch (FileNotFoundException e1) {
			e1.getMessage();
			e1.printStackTrace();
		}
		try {
			r.write(buff);
		} catch (IOException e) {
			e.getMessage();
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws IOException {

		// test du DiskManager
		DiskManager.CreateFile(1);

		PageId PageId = DiskManager.AddPage(1);
		byte[] buff = new byte[Constants.pageSize];
		DiskManager.ReadPage(PageId, buff);
		for (int i = 0; i < buff.length; i++) {
			System.out.println(buff[i]);
		}
		String str = "bonjour";

		DiskManager.WritePage(PageId, str.getBytes());
		DiskManager.ReadPage(PageId, buff);

		for (int i = 0; i < buff.length; i++) {
			System.out.println(buff[i]);
		}

	}

}
