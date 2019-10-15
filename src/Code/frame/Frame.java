package Code.frame;

import Code.pages.PageId;
import Code.util.Constants;

public class Frame {
	private byte [] buff;
	private PageId pg;
	private int pin_count;
	private int ref_bit;
	private int flag_dirty;
	
	
	
	
	public Frame(PageId pg) {
		this.buff = new byte[Constants.pageSize];
		this.pg = pg;
		this.pin_count = 0;
		this.ref_bit = 1;
		this.flag_dirty = 0;
		
		if(pin_count == 0) {
			this.ref_bit = 1;
		}
		
	}
	
	public void Clock(Frame [] f) {
		boolean remplacement = false;
		for(int i = 0; !remplacement; i++) {
			if(pin_count == 0 && ref_bit == 1) {
				ref_bit = 0;
			}
			if(pin_count == 0 && ref_bit == 0) {
				f[i] = new Frame(this.pg);
				this.flag_dirty++;
				this.pin_count = 1;
				remplacement = true;
				
			}
		}
	}
	public PageId getIdPage() {
		return this.pg;
	}
	public byte [] getBuff(){
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
	public void setBuff(byte [] buff) {
		this.buff = buff;
	}
	public void setIdpage(PageId pg) {
		this.pg = pg;
	}
}
