package Code.heapFile;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import Code.BufferManager.BufferManager;
import Code.Rid.Rid;
import Code.diskManager.DiskManager;
import Code.pages.PageId;
import Code.record.Record;
import Code.reldef.RelDef;
import Code.type.Type;


public class HeapFile {
	private RelDef relDef;

	public HeapFile(RelDef rd) {
		this.relDef = rd;
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

		System.out.println("add Data Page");
		BufferManager u = BufferManager.getInstance();
		byte[] temp = u.getPage(new PageId(relDef.getFileIdx(), 0));// headerpage

		ByteBuffer bb = ByteBuffer.wrap(temp);
		bb.putInt(0, bb.getInt(0) + 1);
		System.out.println("methode tets");
		System.out.println(bb.getInt(0));
		PageId nouvellePage = DiskManager.AddPage(relDef.getFileIdx());
		// met a jour cases slot de la data page concerné
		System.out.println("nb page");
		System.out.println(nouvellePage.getPageIdx());
		bb.putInt(4*nouvellePage.getPageIdx(), this.relDef.getSlotCount());
		u.FreePage(new PageId(relDef.getFileIdx(), 0), 1);// libere la frame de la headerpage

		System.out.println(" return add Data Page");

		return nouvellePage;

	}

	public PageId getFreeDataPageId() {
		BufferManager u = BufferManager.getInstance();
		ByteBuffer bb = ByteBuffer.wrap(u.getPage(new PageId(relDef.getFileIdx(), 0)));
		for (int i = 4; i <= bb.getInt(0)*4; i +=4) {
			if (bb.getInt(i) > 0) {
				
	
				return new PageId(relDef.getFileIdx(), i/4);
			}
		}
		return null;
	}

	public RelDef getReldef() {
		return relDef;
	}

	public Rid writeRecordToDataPage(Record r, PageId pg) {
		BufferManager u = BufferManager.getInstance();
		int indiceSlot = 0;
		ByteBuffer bb = ByteBuffer.wrap(u.getPage(pg));
		byte[] temp = new byte[this.getReldef().getRecordSize()];
		for (int i = 0; i < this.getReldef().getSlotCount(); i++) {
			if (bb.get(i) == 0) {// slot vide
				bb.put(i, (byte) 1);// mise a jour du slot en occupé
				r.writeToBuffer(temp, 0);// recuperer dans le buffer le tableau values - position 0
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
		BufferManager u = BufferManager.getInstance();
		byte[] temp = u.getPage(pg);
		byte[] recordByte = new byte[this.getReldef().getRecordSize()];
		ByteBuffer bb = ByteBuffer.wrap(temp);
		int y1 = 0;//compteur de record
		// création du tableau de Record
		for (int i = 0; i < this.getReldef().getSlotCount(); i++) {
			if(bb.get(i) == 1) {
				y1++;
			
		}
			
			
		}
		
		Record [] listRecord = new Record[y1]; 
		
				
		for (int i = 0; i < y1; i++) {
			
			listRecord[i] = new Record(relDef);
			
		}
		

		bb.position(this.getReldef().getSlotCount());

		for (int i = this.getReldef().getSlotCount() , x = 0; x <listRecord.length; i = i + this.getReldef().getRecordSize(), x++) {
			
			listRecord[x].setValues(new String[this.getReldef().getTypeColonne().length]);
			
			// mise en forme du tab byte pour un record
			
			recordByte = Arrays.copyOfRange(temp, i, i+this.getReldef().getRecordSize());
			
			
			listRecord[x].readFromBuffer(recordByte, 0);
			
			
			
			
		}
		return listRecord;
	}

	public static void main(String[] args) {
		
		//Type[] typeco = { Type.FLOAT, Type.INT, Type.FLOAT };
		//String[] value = { "4.5", "4", "3.5" };
		//r.setValues(value);
		//rd.setTypeColonne(typeco);
		
		// deuxieme record a mettre
		RelDef rd1 = new RelDef(1, 16, 10);
		HeapFile h = new HeapFile(rd1);
		Record r1 = new Record(rd1);
		Record r2 =new Record (rd1);
		
		h.createNewOnDisk();
		
		
		String[] value2 = {"axel", "toli"};
		String [] value3 = {"yolo", "aure"};
		Type t = new Type("string4");
		Type t1 = new Type("string4");
		Type[] type = {t, t1};
		r1.setValues(value2);
		r2.setValues(value3);
		rd1.setTypeColonne(type);
		try {
			
			PageId l1 = h.addDataPage();
			
			
			
			
			
			
			
			h.writeRecordToDataPage(r1, l1);
			h.writeRecordToDataPage(r2, l1);
			/**
			BufferManager u = BufferManager.getInstance();
			byte [] y = u.getPage(new PageId(h.relDef.getFileIdx(), 1));
			ByteBuffer bb = ByteBuffer.wrap(y);
			System.out.println(bb.get(1));
			System.out.println(bb.getChar(h.relDef.getSlotCount()));
			System.out.println(bb.getChar(h.relDef.getSlotCount()+h.relDef.getRecordSize()+2));
			u.FreePage(new PageId(h.getReldef().getFileIdx(), 1), 0);
			*/
			
			Record [] l = h.getRecordsInDataPage(l1);
			
				System.out.println(l[1].getValues()[1]);
			
			
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	

}
