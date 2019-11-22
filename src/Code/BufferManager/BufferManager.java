package Code.BufferManager;


import java.io.IOException;

import Code.diskManager.DiskManager;
import Code.frame.Frame;
import Code.pages.PageId;
import Code.util.Constants;

public class BufferManager {
	public static final BufferManager INSTANCE = new BufferManager();
	private Frame[] buffer_pool;

	private BufferManager() {
		this.buffer_pool = new Frame[Constants.frameCount];
		for (int i = 0; i < Constants.frameCount; i++) {
			this.buffer_pool[i] = new Frame();
		}
	}

	public static BufferManager getInstance() {
		return INSTANCE;
	}

	public byte[] getPage(PageId pg) {

		boolean accesPage = false;
		int i = 0;
		byte [] temp = new byte[Constants.pageSize];
		int compt = 0;
		while (!accesPage) {
			if (compt > 0) {
				System.out.println(compt);
				System.out.println(" ! remplacement clock ! ");
			}
			for (i = 0; (i < Constants.frameCount); i++) {

				if(buffer_pool[i].getIdPage() == null){//case vide
					
					DiskManager.ReadPage(pg, temp);
					buffer_pool[i].setBuff(temp);
					buffer_pool[i].setPin_count(1);

					buffer_pool[i].setIdpage(pg);
					System.out.println("case vide");
					accesPage = true;
					System.out.println("etat de frame " + i);
					System.out.println(buffer_pool[i].getPin_count());
					System.out.println(buffer_pool[i].getFlag_dirty());
					System.out.println(buffer_pool[i].getRef_bit());
					System.out.println("*********");
					break;
				}else if ( buffer_pool[i].getIdPage().getPageIdx() == pg.getPageIdx()) {//si la page est deja dans le buffer pool
					System.out.println(buffer_pool[i].getIdPage().getFileIdx());
					buffer_pool[i].setPin_count(buffer_pool[i].getPin_count() + 1);
					System.out.println("case occupée par meme page");
					DiskManager.ReadPage(pg, temp);
					buffer_pool[i].setBuff(temp);
					accesPage = true;
					System.out.println("etat de frame " + i);
					System.out.println(buffer_pool[i].getPin_count());
					System.out.println(buffer_pool[i].getFlag_dirty());
					System.out.println(buffer_pool[i].getRef_bit());
					System.out.println("*********");
					break;

				} else if (buffer_pool[i].getPin_count() == 0) {//remplacement
					if (buffer_pool[i].getRef_bit() == 0) {
						// remplacement
						System.out.println("remplacement");
						DiskManager.ReadPage(pg, temp);
						buffer_pool[i].setBuff(temp);
						buffer_pool[i].setPin_count(1);

						buffer_pool[i].setIdpage(pg);
						accesPage = true;
						System.out.println("etat de frame " + i);
						System.out.println(buffer_pool[i].getPin_count());
						System.out.println(buffer_pool[i].getFlag_dirty());
						System.out.println(buffer_pool[i].getRef_bit());
						System.out.println("*********");
						break;

					}
					if (buffer_pool[i].getRef_bit() != 0) {
						buffer_pool[i].setRef_bit(0);
					}
				}

			}

			compt++;

		}
		return this.getBufferManager()[i].getBuff();
	}

	public void FreePage(PageId pg, int valdirty) {
		System.out.println("entrée freepagee");
		for (int i = 0; i < buffer_pool.length;i++) {
			System.out.println("boucle freepagee");
			if(buffer_pool[i].getIdPage() == null) {
				//rien faire au cas ou pas adresse
			}
			if (buffer_pool[i].getIdPage().getPageIdx() == pg.getPageIdx()) {
				if (buffer_pool[i].getPin_count() == 1) {
					buffer_pool[i].setRef_bit(buffer_pool[i].getRef_bit() + 1);
				}
				buffer_pool[i].setPin_count(buffer_pool[i].getPin_count() - 1);
				buffer_pool[i].setFlag_dirty(valdirty);
				
				System.out.println("etat de frame " + i);
				System.out.println(buffer_pool[i].getPin_count());
				System.out.println(buffer_pool[i].getFlag_dirty());
				System.out.println(buffer_pool[i].getRef_bit());
				System.out.println("*********");
				break;
			}

		}

	}

	public void FlushAll() {
		for (int i = 0; i < buffer_pool.length; i++) {

			if (buffer_pool[i].getFlag_dirty() == 1) {
				DiskManager.WritePage(buffer_pool[i].getIdPage(), buffer_pool[i].getBuff());
			}

			buffer_pool[i].setBuff(null);
			buffer_pool[i].setIdpage(null);
			buffer_pool[i].setFlag_dirty(0);
			buffer_pool[i].setPin_count(0);
			buffer_pool[i].setRef_bit(0);
		}
	}

	public Frame[] getBufferManager() {
		return this.buffer_pool;
	}

	public static void main(String[] args) throws IOException {

		DiskManager.CreateFile(1);
		BufferManager u = new BufferManager();

		PageId[] id = new PageId[5];
		for (int i = 0; i < 5; i++) {

			id[i] = DiskManager.AddPage(i);

		}
		System.out.println("appel 1");
		u.getPage(id[0]);
		
		System.out.println("appel 2");
		u.getPage(id[0]);
		System.out.println("appel 3");
		u.getPage(id[1]);
		u.FreePage(id[1], 0);
		u.FreePage(id[0], 0);
		u.getPage(id[2]);

	}

}
