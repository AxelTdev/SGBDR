package Code.diskManager;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import Code.pages.PageId;

public class DiskManager {
	private int compteur_page = 1;
	private static DiskManager  instance = null;
	
	private DiskManager() {
		
	}

	public static DiskManager getInstance() {
		if(instance==null) {
			instance=new DiskManager ();
		}
		return instance;
		}
	
	public static void CreateFile (int fileIdx) {
		try {
			//DB/mets le fichier  dans le repertoire DB
			RandomAccessFile file = new RandomAccessFile("DB/Data_"+fileIdx+".rf", "rw");
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		}
	}
	
	public static PageId AddPage (int fileIdx) {
		
		PageId p = new PageId(fileIdx,compteur_page);
		compteur_page++;
		return p;
	}
	
	public static void ReadPage (PageId p, ByteBuffer b) {
		try (RandomAccessFile r=new RandomAccessFile ("DB/Data_"+p.fileIdx+".rf","r");) {
			            
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void WritePage (PageId p, ByteBuffer b) {
		
	}
}
