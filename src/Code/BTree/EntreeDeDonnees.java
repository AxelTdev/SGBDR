package Code.BTree;

import java.util.ArrayList;
import java.util.List;

public class EntreeDeDonnees {
	private int clef;
	private List<Integer> rid;

	public EntreeDeDonnees(int clef) {
		this.clef = clef;
		this.rid = new ArrayList<>();

	}

	public EntreeDeDonnees(int clef, List<Integer> rid) {
		this.clef = clef;
		this.rid = rid;

	}

	public int getClef() {
		return clef;
	}

	public List<Integer> getRidList() {
		return rid;
	}

}
