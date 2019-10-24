package Code.heapFile;

import java.io.IOException;
import java.nio.ByteBuffer;

import Code.BufferManager.BufferManager;
import Code.Rid.Rid;
import Code.diskManager.DiskManager;
import Code.pages.PageId;
import Code.record.Record;
import Code.reldef.RelDef;
import Code.util.Constants;

public class HeapFile {
	private RelDef relDef;

	public void createNewOnDisk() {
		DiskManager.CreateFile(relDef.getFileIdx());
		try {

			PageId pg = DiskManager.AddPage(relDef.getFileIdx());
			BufferManager u = BufferManager.getInstance();
			byte[] temp = u.getPage(pg);
			ByteBuffer bb = ByteBuffer.wrap(temp);
			bb.put(new byte[Constants.pageSize]);
			u.FreePage(pg, 1);
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public PageId addDataPage() throws IOException {

		BufferManager u = BufferManager.getInstance();
		byte[] temp = u.getPage(new PageId(relDef.getFileIdx(), 0));// headerpage
		ByteBuffer bb = ByteBuffer.wrap(temp);
		bb.putInt(bb.getInt() + 1);//incrémente le compteur de pages
		bb.putInt(relDef.getSlotCount());//met a jour les cases de la page libres
		u.FreePage(new PageId(relDef.getFileIdx(), 0), 1);

		return DiskManager.AddPage(relDef.getFileIdx());

	}

	public PageId getFreeDataPageId() {
		BufferManager u = BufferManager.getInstance();
		ByteBuffer bb = ByteBuffer.wrap(u.getPage(new PageId(relDef.getFileIdx(), 0)));
		for (int i = 1; i < bb.getInt(); i++) {
			if (bb.getInt() > 0) {
				return new PageId(relDef.getFileIdx(), i);
			}
		}
		return null;
	}

	public RelDef getReldef() {
		return relDef;
	}

	public Rid writeRecordToDataPage(Record r, PageId pg) {// ou est la bytemap?
		BufferManager u = BufferManager.getInstance();
		int indiceSlot = 0;
		ByteBuffer bb = ByteBuffer.wrap(u.getPage(pg));
		byte[] temp = new byte[this.getReldef().getRecordSize()];// quelle taille?
		for (int i = 0; i < this.getReldef().getSlotCount(); i++) {
			if (bb.get(i) == 0) {// slot vide
				bb.put((byte) 1);// mise a jour du slot en occupé
				r.writeToBuffer(temp, 0);// recuperer dans le buffer le tableau values - quelle position?
				indiceSlot = this.getReldef().getSlotCount() + i * this.getReldef().getRecordSize();// position ou
																									// mettre le record
				bb.position(indiceSlot);// position ou mettre le record
				bb.put(temp);
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
		Record[] listRecord = new Record[this.getReldef().getRecordSize()];
		BufferManager u = BufferManager.getInstance();
		byte[] temp = u.getPage(pg);
		byte[] recordByte = new byte[this.getReldef().getRecordSize()];
		ByteBuffer bb = ByteBuffer.wrap(temp);

		// création du tableau de Record
		for (int i = 0; i < this.getReldef().getSlotCount(); i++) {
			listRecord[i] = new Record(this.getReldef());
		}

		bb.position(this.getReldef().getSlotCount());// tous les heap file ont la meme longueur de tuple?

		for (int i = this.getReldef().getSlotCount(), x = 0; i < temp.length; i++, x++) {

			// mise en forme du tab byte pour un record
			for (int y = 0; y < this.getReldef().getRecordSize(); y++, i++) {
				recordByte[y] = temp[i];
			}
			listRecord[x].readFromBuffer(recordByte, 0);// quelle position?
		}
		return listRecord;
	}
	
	

}
