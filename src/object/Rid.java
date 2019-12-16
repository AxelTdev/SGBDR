package object;

import java.util.Comparator;

public class Rid implements Comparable<Rid>{
	
	private PageId pg;
	private int indice_case;

	public Rid(PageId pg, int indice_case) {
		this.pg = pg;
		this.indice_case = indice_case;
	}

	
	public int compareTo(Rid o) {
		if(this.indice_case>o.indice_case) {
			return 0;
		}else {
			return 1;
		}
	}

	

}
