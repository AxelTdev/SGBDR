package Code.BufferManager;

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

		while (!accesPage) {

			for (i = 0; (i < Constants.frameCount); i++) {

				if (buffer_pool[i].getIdPage() == pg) {
					buffer_pool[i].setPin_count(buffer_pool[i].getPin_count() + 1);
					accesPage = true;

					break;

				} else if (buffer_pool[i].getPin_count() == 0) {
					if (buffer_pool[i].getRef_bit() == 0) {
						// remplacement ou frame libre

						DiskManager.ReadPage(pg, buffer_pool[i].getBuff());
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

		}
		return this.getBufferManager()[i].getBuff();
	}

	public void FreePage(PageId pg, int valdirty) {

		for (int i = 0; i < buffer_pool.length; i++) {
			if (buffer_pool[i].getIdPage() == pg) {
				if (buffer_pool[i].getPin_count() == 1) {
					buffer_pool[i].setRef_bit(buffer_pool[i].getRef_bit() + 1);
				}
				buffer_pool[i].setPin_count(buffer_pool[i].getPin_count() - 1);
				buffer_pool[i].setFlag_dirty(valdirty);

			}

		}

	}

	public void FlushAll() {
		for (int i = 0; i < buffer_pool.length; i++) {

			if (buffer_pool[i].getFlag_dirty() == 1) {
				DiskManager.WritePage(buffer_pool[i].getIdPage(), buffer_pool[i].getBuff());
			}

			buffer_pool[i].setBuff(new byte[Constants.pageSize]);
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
