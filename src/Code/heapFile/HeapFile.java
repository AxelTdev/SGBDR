package Code.heapFile;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
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
		// met a jour cases slot de la data page concerné
		bb.putInt(4 * (nouvellePage.getPageIdx() - 1), this.relDef.getSlotCount());
		DiskManager.WritePage(l, temp);
		u.FreePage(new PageId(relDef.getFileIdx(), 0), 1);// libere la frame de la headerpage

		return nouvellePage;

	}

	public PageId getFreeDataPageId() {
		BufferManager u = BufferManager.getInstance();
		ByteBuffer bb = ByteBuffer.wrap(u.getPage(new PageId(relDef.getFileIdx(), 0)));
		for (int i = 4; i <= bb.getInt(0) * 4; i += 4) {
			if (bb.getInt(i) > 0) {

				return new PageId(relDef.getFileIdx(), i / 4);
			}
		}
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
		byte[] tempByte = new byte[Constants.frameCount];
		tempByte = u.getPage(pg);
		ByteBuffer bb = ByteBuffer.wrap(tempByte);
		byte[] temp = new byte[this.getReldef().getRecordSize()];
		for (int i = 0; i < this.getReldef().getSlotCount(); i++) {
			if (bb.get(i) == 0) {// slot vide
				bb.put(i, (byte) 1);// mise a jour du slot en occupé
				r.writeToBuffer(temp, 0);// recuperer dans le buffer le tableau values - position 0
				indiceSlot = (this.getReldef().getSlotCount()) + i * this.getReldef().getRecordSize();// position ou
				// mettre le record
				bb.position(indiceSlot);// position ou mettre le record
				bb.put(temp);
				DiskManager.WritePage(pg, tempByte);

				u.FreePage(pg, 1);

				break;
			}

		}
		// actualisation de la header page

		temp = u.getPage(new PageId(this.getReldef().getFileIdx(), 0));// recuperer la header page

		bb = ByteBuffer.wrap(temp);
		int nbPage = pg.getPageIdx();
		bb.putInt(nbPage, (bb.getInt(nbPage) - 1));// decrémente le compteur de case libre de la page de donnée
		u.FreePage(new PageId(this.getReldef().getFileIdx(), 0), 1);

		return new Rid(pg, indiceSlot);

	}

	public Record[] getRecordsInDataPage(PageId pg) {
		BufferManager u = BufferManager.getInstance();
		byte[] temp = u.getPage(pg);
		byte[] recordByte = new byte[this.getReldef().getRecordSize()];
		ByteBuffer bb = ByteBuffer.wrap(temp);
		int y1 = 0;// compteur de record
		// Savoir le nombre de record à créer
		for (int i = 0; i < this.getReldef().getSlotCount(); i++) {
			if (bb.get(i) == 1) {
				y1++;

			}

		}

		Record[] listRecord = new Record[y1];

		// initialiser le tableau de records
		for (int i = 0; i < y1; i++) {

			listRecord[i] = new Record(relDef);
			listRecord[i].setValues(new String[this.getReldef().getTypeColonne().length]);
		}

		for (int i = 0, recordSize = this.getReldef().getSlotCount(); i < listRecord.length; i++, recordSize += this
				.getReldef().getRecordSize()) {
			recordByte = Arrays.copyOfRange(temp, recordSize, recordSize + this.getReldef().getRecordSize());
			bb = ByteBuffer.wrap(recordByte);

			listRecord[i].readFromBuffer(recordByte, 0);

		}

		return listRecord;
	}

	public Rid InsertRecord(Record rd) {
		HeapFile h = new HeapFile(this.getReldef());
		PageId pg = h.getFreeDataPageId();
		return h.writeRecordToDataPage(rd, pg);
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
		for (int i = 1; i < nbPage + 1; i++) {
			rdList = h.getRecordsInDataPage(new PageId(this.relDef.getFileIdx(), 1));
			System.out.println(rdList[0] + "dddiojijj");
		}

		rdList = h.getRecordsInDataPage(new PageId(this.relDef.getFileIdx(), 1));
		// System.out.println(rdList[0]);
		Record[] str = new Record[l.size()];
		for (int y = 0; y < l.size(); y++) {
			str[y] = l.get(y);
		}
		u.FreePage(HeapFile.HEADER_PAGE, 0);

		return str;

	}

	public static void main(String[] args) {

		RelDef rd1 = new RelDef(1, 32, 5);
		HeapFile h = new HeapFile(rd1);
		Record r1 = new Record(rd1);
		Record r2 = new Record(rd1);

		String[] t = { "axellml", "5", "546" };
		r1.setValues(t);
		String[] temp45 = t;
		r2.setValues(temp45);
		Type t1 = new Type("string7");
		Type t2 = new Type("int");
		Type t3 = new Type("int");
		Type[] type = { t1, t2, t3 };
		rd1.setTypeColonne(type);

		h.createNewOnDisk();
		try {
			PageId l = h.addDataPage();
			BufferManager u = BufferManager.getInstance();
			byte[] tempByte = u.getPage(new PageId(h.getReldef().getFileIdx(), 0));
			ByteBuffer bb = ByteBuffer.wrap(tempByte);
			h.InsertRecord(r1);
			Record [] ty = h.getRecordsInDataPage(l);
			System.out.println(ty[0]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
