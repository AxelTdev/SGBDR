package Code.diskManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

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
		File file = new File("DB/Data_" + fileIdx +".rf");

	}

	public static PageId AddPage(int fileIdx) throws IOException {

		PageId p = new PageId(fileIdx, compteur_page);
		compteur_page++;
		byte[] buffer = new byte[Constants.pageSize];
		
			RandomAccessFile r = new RandomAccessFile("DB/Data_" + p.fileIdx + ".rf", "rw");
			r.write(buffer);
			r.close();
		
		return p;
	}

	public static void ReadPage(PageId p, ByteBuffer b) {
		try (RandomAccessFile r = new RandomAccessFile("DB/Data_" + p.fileIdx + ".rf", "r");) {

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void WritePage(PageId p, ByteBuffer b) {

	}
}
