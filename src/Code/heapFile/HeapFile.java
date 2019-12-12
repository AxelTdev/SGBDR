package Code.heapFile;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import Code.BufferManager.BufferManager;
import Code.Rid.Rid;
import Code.diskManager.DiskManager;
import Code.pages.PageId;
import Code.record.Record;
import Code.reldef.RelDef;
import Code.type.Type;
import Code.util.Constants;

public class HeapFile {

	private RelDef relDef;

	private static PageId HEADER_PAGE = null;

	public HeapFile(RelDef rd) {
		this.relDef = rd;
		HEADER_PAGE = new PageId(rd.getFileIdx(), 0);
	}

	public void createNewOnDisk() {
		DiskManager.CreateFile(relDef.getFileIdx());
		try {
			// met PageSize 0 au fichier
			DiskManager.AddPage(relDef.getFileIdx());

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public PageId addDataPage() throws IOException {

		BufferManager u = BufferManager.getInstance();
		PageId l = new PageId(relDef.getFileIdx(), 0);
		byte[] temp = u.getPage(l);// headerpage

		ByteBuffer bb = ByteBuffer.wrap(temp);
		bb.putInt(0, bb.getInt(0) + 1);
		PageId nouvellePage = DiskManager.AddPage(relDef.getFileIdx());
		// met a jour cases slot de la data page concern�
		bb.putInt(4 * (nouvellePage.getPageIdx() - 1), this.relDef.getSlotCount());
		DiskManager.WritePage(l, temp);
		u.FreePage(new PageId(relDef.getFileIdx(), 0), 1);// libere la frame de la headerpage

		return nouvellePage;

	}

	public PageId getFreeDataPageId() {
		BufferManager u = BufferManager.getInstance();
		ByteBuffer bb = ByteBuffer.wrap(u.getPage(new PageId(relDef.getFileIdx(), 0)));
		int pageCount = bb.getInt(0);
		bb.position(4);
		for (int i = 1; i <= pageCount; i ++) {
			if (bb.getInt(i*4) > 0) {
				u.FreePage(new PageId(relDef.getFileIdx(), 0), 0);
				return new PageId(relDef.getFileIdx(), (i ) + 1);

			}
		}
		u.FreePage(new PageId(relDef.getFileIdx(), 0), 0);
		return null;
	}

	public RelDef getReldef() {
		return relDef;
	}

	public void setReldef() {

	}

	public Rid writeRecordToDataPage(Record r, PageId pg) {
		BufferManager u = BufferManager.getInstance();
		int indiceSlot = 0;
		byte[] tempByte = null;
		System.out.println("heloo " + pg.getPageIdx());
		tempByte = u.getPage(pg);
		System.out.println("ddddd" + tempByte.length);

		ByteBuffer bb = ByteBuffer.wrap(tempByte);
		byte[] temp = new byte[this.getReldef().getRecordSize()];
		for (int i = 0; i < this.getReldef().getSlotCount(); i++) {
			if (bb.get(i) == 0) {// slot vide
				bb.put(i, (byte) 1);// mise a jour du slot en occup�
				r.writeToBuffer(temp, 0);// recuperer dans le buffer le tableau values - position 0
				indiceSlot = (this.getReldef().getSlotCount()) + i * this.getReldef().getRecordSize();// position ou
				// mettre le record
				bb.position(indiceSlot);
				bb.put(temp);
				System.out.println("writepage byte");
				System.out.println(Arrays.toString(tempByte));
				System.out.println("abc" +bb.getChar(4));
				DiskManager.WritePage(pg, tempByte);

				u.FreePage(pg, 1);

				break;
			}

		}

		// actualisation de la header page

		temp = u.getPage(new PageId(this.getReldef().getFileIdx(), 0));// recuperer la header page

		bb = ByteBuffer.wrap(temp);
		int nbPage = pg.getPageIdx();
		System.out.println("lolol" + nbPage * 4);
		bb.putInt((nbPage - 1) * 4, ((bb.getInt((nbPage - 1) * 4)) - 1));// decr�mente le compteur de case libre de la
																			// page de donn�e
		DiskManager.WritePage(new PageId(this.getReldef().getFileIdx(), 0), temp);
		System.out.println("lolol" + bb.getInt(4));
		u.FreePage(new PageId(this.getReldef().getFileIdx(), 0), 1);

		return new Rid(pg, indiceSlot);

	}

	public Record[] getRecordsInDataPage(PageId pg) {
		BufferManager u = BufferManager.getInstance();
		byte[] temp = u.getPage(pg);
		byte[] recordByte = new byte[this.getReldef().getRecordSize()];
		ByteBuffer bb = ByteBuffer.wrap(temp);
		int y1 = 0;// compteur de record
		// Savoir le nombre de record � cr�er
		for (int i = 0; i < this.getReldef().getSlotCount(); i++) {
			if (bb.get(i) == 1) {
				y1++;

			}

		}
		
		System.out.println("slot count" + this.relDef.getSlotCount());

		Record[] listRecord = new Record[y1];

		// initialiser le tableau de records
		for (int i = 0; i < y1; i++) {

			listRecord[i] = new Record(relDef);
			listRecord[i].setValues(new String[this.getReldef().getTypeColonne().length]);
		}

		for (int i = 0, recordSize = this.getReldef().getSlotCount(); i < listRecord.length; i++, recordSize += this
				.getReldef().getRecordSize()) {
			recordByte = Arrays.copyOfRange(temp, recordSize , recordSize + this.getReldef().getRecordSize());
			bb = ByteBuffer.wrap(recordByte);

			listRecord[i].readFromBuffer(recordByte, 0);

		}
		System.out.println("getPage array");
		System.out.println(Arrays.toString(recordByte));
		
		
		u.FreePage(pg, 0);
		return listRecord;
	}

	public Rid InsertRecord(Record rd) throws IOException {
		PageId pg = this.getFreeDataPageId();
		
		if(pg == null) {//si pas de pages de cr�� ou pas de pages dispo ajouter une page je sais pas si un bon dajouter ici une page a voir
			pg = this.addDataPage();
			System.out.println(pg.getPageIdx() + "ss");
		}
		return this.writeRecordToDataPage(rd, pg);
	}

	public Record[] GetAllRecords() {
		HeapFile h = new HeapFile(this.getReldef());
		List<Record> l = new ArrayList<>();
		BufferManager u = BufferManager.getInstance();
		byte[] tempByte = u.getPage(new PageId(this.getReldef().getFileIdx(), 0));
		ByteBuffer bb = ByteBuffer.wrap(tempByte);
		int nbPage = bb.getInt(0);
		System.out.println(nbPage + "ezdez");
		Record[] rdList = null;
		for (int i = 1; i <= nbPage; i++) {			
			rdList = h.getRecordsInDataPage(new PageId(this.relDef.getFileIdx(), i + 1));//Pour �viter la header page
			Collections.addAll(l, rdList);

		}
		
		Record[] finalTemp = new Record[l.size()];
		for (int i = 0; i < finalTemp.length; i++) {
			finalTemp[i] = l.get(i);
		}

		u.FreePage(HeapFile.HEADER_PAGE, 0);

		return finalTemp;

	}

	public static void main(String[] args) {

		RelDef rd1 = new RelDef(1, 10, 20);
		HeapFile h = new HeapFile(rd1);
		Record r1 = new Record(rd1);
		Record r2 = new Record(rd1);

		String[] t = { "kaxel" };
		String [] test2 = {"cuirl"};
		r1.setValues(t);
	
		Type t1 = new Type("string5");
		Type t2 = new Type("int");
		Type t3 = new Type("int");
		Type[] type = { t1 };
		rd1.setTypeColonne(type);
		r2.setValues(test2);
		
		

		h.createNewOnDisk();

		try {
			
		
			for(int i = 0; i < 49; i++) {
			h.InsertRecord(r1);
			}
		
			
		

			Record[] lol = h.GetAllRecords();

			for (Record r : lol) {
				System.out.println(r);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}