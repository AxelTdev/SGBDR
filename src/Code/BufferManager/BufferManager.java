package Code.BufferManager;

import Code.diskManager.DiskManager;
import Code.frame.Frame;
import Code.pages.PageId;
import Code.util.Constants;

public class BufferManager {
	public static final BufferManager INSTANCE = new BufferManager();
	private Frame f;

	private BufferManager() {

	}

	public static BufferManager getInstance() {
		return INSTANCE;
	}

	public byte[] getPage(PageId pg) {
		f = new Frame(pg);
		DiskManager.ReadPage(pg, f.getBuff());
		// remplacement dune frame
		return f.getBuff();
	}

	public void FreePage(PageId pg, int valdirty) {

		f.setPin_count(0);

		f.setFlag_dirty(valdirty);

	}

	public void FlushAll() {
		if (f.getFlag_dirty() == 1) {
			DiskManager.WritePage(f.getIdPage(), f.getBuff());
		}
		f.setFlag_dirty(0);
		f.setPin_count(0);
		f.setRef_bit(0);
		byte[] init = new byte[Constants.pageSize];
		f.setBuff(init);
		f.setIdpage(null);
	}

}
