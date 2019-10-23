package Code.frame;

import Code.pages.PageId;
import Code.util.Constants;

public class Frame {
	
	private byte[] buff;
	private PageId pg;
	private int pin_count;
	private int ref_bit;
	private int flag_dirty;

	public Frame() {
		this.buff = new byte[Constants.pageSize];
		this.pg = null;
		this.pin_count = 0;
		this.flag_dirty = 0;
		this.ref_bit = 0;
		

	}

	
	

	public PageId getIdPage() {
		return this.pg;
	}

	public byte[] getBuff() {
		return this.buff;
	}

	public void setPin_count(int a) {
		this.pin_count = a;
	}

	public int getPin_count() {
		return this.pin_count;
	}

	public void setFlag_dirty(int f) {
		this.flag_dirty = f;
	}

	public int getFlag_dirty() {
		return this.flag_dirty;
	}

	public void setRef_bit(int f) {
		this.ref_bit = f;
	}
	public int getRef_bit() {
		return this.ref_bit;
	}

	public void setBuff(byte[] buff) {
		this.buff = buff;
	}

	public void setIdpage(PageId pg) {
		this.pg = pg;
	}
}
