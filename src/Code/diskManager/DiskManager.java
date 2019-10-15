package Code.diskManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import Code.frame.Frame;
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

	public static void main(String[] args) {
		DiskManager u = DiskManager.getInstance();
		Frame[] f = new Frame[5];
		u.CreateFile(1);
		
		for (int i = 0; i < 5; i++) {
			try {
				
				f[i] = new Frame(u.AddPage(1));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		byte[] buff = new byte[Constants.pageSize];
		u.ReadPage(f[2].getIdPage(), buff);
		f[2].setBuff(buff);
		System.out.println(f[4].getBuff()[0]);
	}

}
