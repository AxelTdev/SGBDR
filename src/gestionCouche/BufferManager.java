package gestionCouche;



import constantes.Constants;
import object.Frame;
import object.PageId;

public class BufferManager {
	public static final BufferManager INSTANCE = new BufferManager();
	private Frame[] buffer_pool = new Frame[Constants.frameCount];

	private BufferManager() {
	
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
				
			}
			for (i = 0; (i < Constants.frameCount); i++) {

				if(buffer_pool[i].getIdPage() == null){//case vide
					
					DiskManager.ReadPage(pg, temp);
					buffer_pool[i].setBuff(temp);
					buffer_pool[i].setPin_count(1);

					buffer_pool[i].setIdpage(pg);
					
					accesPage = true;
				
					break;
				}else if ( buffer_pool[i].getIdPage().getPageIdx() == pg.getPageIdx()) {//si la page est deja dans le buffer pool
					
					buffer_pool[i].setPin_count(buffer_pool[i].getPin_count() + 1);
					
					DiskManager.ReadPage(pg, temp);
					buffer_pool[i].setBuff(temp);
					accesPage = true;
				
					break;

				} else if (buffer_pool[i].getPin_count() == 0) {//remplacement
					if (buffer_pool[i].getRef_bit() == 0) {
						// remplacement
						
						DiskManager.ReadPage(pg, temp);
						buffer_pool[i].setBuff(temp);
						buffer_pool[i].setPin_count(1);

						buffer_pool[i].setIdpage(pg);
						accesPage = true;
						
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
	
		for (int i = 0; i < buffer_pool.length;i++) {
			
			if(buffer_pool[i].getIdPage() == null) {
				
				
				
				
				
				
				
				
				
				
				//rien faire au cas ou pas adresse
			}
			if (buffer_pool[i].getIdPage().getPageIdx() == pg.getPageIdx()) {
				if (buffer_pool[i].getPin_count() == 1) {
					buffer_pool[i].setRef_bit(buffer_pool[i].getRef_bit() + 1);
				}
				buffer_pool[i].setPin_count(buffer_pool[i].getPin_count() - 1);
				buffer_pool[i].setFlag_dirty(valdirty);
				
			
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

	

}
