package bplusTree;

import java.io.*;
import java.util.*;



class BPlusTree {

	public static Node tree;
	public static int degree;
	public static boolean debug;

	BPlusTree(int x) {
		degree = x;
		tree = new LeafNode(degree);
		debug = false;
	}

	public static void insertIntoTree(DataNode dnode) {
		tree = tree.insert(dnode);
	}

	public static void searchTree(int x) throws IOException {

		if (tree.search(new DataNode(x))) {
			System.out.println("trouvé");
		} else {
			System.out.println("pas trouvé");
		}
	}

	public static void printTree() {
		Vector<Node> nodeList = new Vector();

		nodeList.add(tree);

		boolean done = false;
		int height = 0, count_nodes = 0;
		while (!done) {
			height++;
			Vector<Node> nextLevelList = new Vector();
			String toprint = "";

			for (int i = 0; i < nodeList.size(); i++) {
				count_nodes++;
				Node node = (Node) nodeList.elementAt(i);

				toprint += node.toString() + " ";

				if (node.isLeafNode()) {
					done = true;
				} else {
					for (int j = 0; j < node.size() + 1; j++) {
						nextLevelList.add(((TreeNode) node).getPointerAt(j));
					}
				}
			}

			System.out.println(toprint + System.getProperty("line.separator"));
			nodeList = nextLevelList;
		}
		System.out.println("Height: " + height + " Number of nodes: " + count_nodes);
	}

	public static void readDegree(int x) {
		new BPlusTree(x);
	}
}

abstract class Node {
	protected Vector<DataNode> data;
	protected Node parent;
	protected int maxsize;

	public boolean isLeafNode() {
		return this.getClass().getName().trim().equals("LeafNode");
	}

	abstract Node insert(DataNode dnode);

	abstract boolean search(DataNode x);

	protected boolean isFull() {
		return data.size() == maxsize - 1;
	}

	public DataNode getDataAt(int index) {
		return (DataNode) data.elementAt(index);
	}

	protected void propagate(DataNode dnode, Node right) {

		if (parent == null) {

			TreeNode newparent = new TreeNode(maxsize);

			newparent.data.add(dnode);
			newparent.pointer.add(this);
			newparent.pointer.add(right);

			this.setParent(newparent);
			right.setParent(newparent);
		} else {
			if (!parent.isFull()) {
				boolean dnodeinserted = false;
				for (int i = 0; !dnodeinserted && i < parent.data.size(); i++) {
					if (((DataNode) parent.data.elementAt(i)).inOrder(dnode)) {
						parent.data.add(i, dnode);
						((TreeNode) parent).pointer.add(i + 1, right);
						dnodeinserted = true;
					}
				}
				if (!dnodeinserted) {
					parent.data.add(dnode);
					((TreeNode) parent).pointer.add(right);
				}
				right.setParent(this.parent);
			} else {
				((TreeNode) parent).split(dnode, this, right);
			}
		}
	}

	public int size() {
		return data.size();
	}

	@SuppressWarnings("unchecked")
	Node(int degree) {
		parent = null;

		data = new Vector();
		maxsize = degree;
	}

	public String toString() {
		String s = "";
		for (int i = 0; i < data.size(); i++) {
			s += ((DataNode) data.elementAt(i)).toString() + " ";
		}
		return s + "*";
	}

	protected Node findRoot() {
		Node node = this;

		while (node.parent != null) {
			node = node.parent;
		}

		return node;
	}

	protected void setParent(Node newparent) {
		this.parent = newparent;
	}
}

class LeafNode extends Node {
	private LeafNode nextNode;

	LeafNode(int degree) {
		super(degree);

		nextNode = null;
	}

	private void setNextNode(LeafNode next) {
		nextNode = next;
	}

	protected LeafNode getNextNode() {
		return nextNode;
	}

	public boolean search(DataNode x) {
		for (int i = 0; i < data.size(); i++) {
			if (((DataNode) data.elementAt(i)).getData() == x.getData()) {
				return true;
			}
		}
		return false;
	}

	protected void split(DataNode dnode) {
		boolean dnodeinserted = false;
		for (int i = 0; !dnodeinserted && i < data.size(); i++) {
			if (((DataNode) data.elementAt(i)).inOrder(dnode)) {
				data.add(i, dnode);
				dnodeinserted = true;
			}
		}
		if (!dnodeinserted) {
			data.add(data.size(), dnode);
		}

		int splitlocation;
		if (maxsize % 2 == 0) {
			splitlocation = maxsize / 2;
		} else {
			splitlocation = (maxsize + 1) / 2;
		}

		LeafNode right = new LeafNode(maxsize);

		for (int i = data.size() - splitlocation; i > 0; i--) {
			right.data.add(data.remove(splitlocation));
		}

		right.setNextNode(this.getNextNode());
		this.setNextNode(right);

		// DataNode mid = (DataNode) data.elementAt(data.size()-1);
		DataNode mid = (DataNode) right.data.elementAt(0);

		this.propagate(mid, right);
	}

	public Node insert(DataNode dnode) {
		if (data.size() < maxsize - 1) {
			boolean dnodeinserted = false;
			int i = 0;
			while (!dnodeinserted && i < data.size()) {
				if (((DataNode) data.elementAt(i)).inOrder(dnode)) {
					data.add(i, dnode);
					dnodeinserted = true;
				}
				i++;
			}
			if (!dnodeinserted) {
				data.add(data.size(), dnode);
			}
		} else {
			this.split(dnode);
		}

		return this.findRoot();
	}
}

class TreeNode extends Node {
	protected Vector<Node> pointer;

	@SuppressWarnings("unchecked")
	TreeNode(int x) {
		super(x);
		pointer = new Vector();
	}

	public Node getPointerTo(DataNode x) {
		int i = 0;
		boolean xptrfound = false;
		while (!xptrfound && i < data.size()) {
			if (((DataNode) data.elementAt(i)).inOrder(x)) {
				xptrfound = true;
			} else {
				i++;
			}

		}
		return (Node) pointer.elementAt(i);
	}

	public Node getPointerAt(int index) {
		return (Node) pointer.elementAt(index);
	}

	boolean search(DataNode dnode) {
		Node next = this.getPointerTo(dnode);
		return next.search(dnode);
	}

	protected void split(DataNode dnode, Node left, Node right) {
		int splitlocation, insertlocation = 0;
		if (maxsize % 2 == 0) {
			splitlocation = maxsize / 2;
		} else {
			splitlocation = (maxsize + 1) / 2 - 1;
		}

		boolean dnodeinserted = false;
		for (int i = 0; !dnodeinserted && i < data.size(); i++) {
			if (((DataNode) data.elementAt(i)).inOrder(dnode)) {
				data.add(i, dnode);
				((TreeNode) this).pointer.remove(i);
				((TreeNode) this).pointer.add(i, left);
				((TreeNode) this).pointer.add(i + 1, right);
				dnodeinserted = true;

				insertlocation = i;
			}
		}
		if (!dnodeinserted) {
			insertlocation = data.size();
			data.add(dnode);
			((TreeNode) this).pointer.remove(((TreeNode) this).pointer.size() - 1);
			((TreeNode) this).pointer.add(left);
			((TreeNode) this).pointer.add(right);
		}

		DataNode mid = (DataNode) data.remove(splitlocation);

		TreeNode newright = new TreeNode(maxsize);

		for (int i = data.size() - splitlocation; i > 0; i--) {
			newright.data.add(this.data.remove(splitlocation));
			newright.pointer.add(this.pointer.remove(splitlocation + 1));
		}
		newright.pointer.add(this.pointer.remove(splitlocation + 1));

		if (insertlocation < splitlocation) {
			left.setParent(this);
			right.setParent(this);
		} else if (insertlocation == splitlocation) {
			left.setParent(this);
			right.setParent(newright);
		} else {
			left.setParent(newright);
			right.setParent(newright);
		}
		this.propagate(mid, newright);
	}

	Node insert(DataNode dnode) {
		Node next = this.getPointerTo(dnode);

		return next.insert(dnode);
	}
}

class DataNode {
	private Integer data;

	DataNode() {
		data = null;
	}

	public String toString() {
		return data.toString();
	}

	public DataNode(int x) {
		data = x;
	}

	public int getData() {
		return data.intValue();
	}

	public boolean inOrder(DataNode dnode) {
		return (dnode.getData() <= this.data.intValue());
	}
}

//till here

abstract class DataUnit {

}

class NodeUnit extends DataUnit {
	public static int max = 4;
	private NodeBU[] elem = new NodeBU[max - 1];
	private DataUnit[] ptrs = new DataUnit[max];
	private NodeUnit prnt;
	private int eInd = 0;
	private int count = 0;
	private int pInd = 0;

	public boolean isFull() {
		if (eInd == max - 1)
			return true;
		else
			return false;
	}

	public void putElem(NodeBU n) {
		elem[eInd++] = n;
		count = eInd;
	}

	public void putElemAt(NodeBU n, int i) {
		elem[i] = n;
	}

	public void putPtr(DataUnit a) {
		ptrs[pInd++] = a;
	}

	public void putPtrAt(DataUnit a, int i) {
		ptrs[i] = a;
	}

	public NodeUnit getPrnt() {
		return prnt;
	}

	public NodeBU getTop() {
		return elem[0];
	}

	public void setPrnt(NodeUnit _prnt) {
		prnt = _prnt;
	}

	public void printElem() {
		for (int i = 0; i < count; i++) {
			System.out.print(elem[i].getData() + " ");
		}
	}

	public DataUnit getPtr(int ind) {
		return ptrs[ind];
	}

	public NodeBU getElem(int ind) {
		return elem[ind];
	}

	public void decElem() {
		eInd--;
	}

	public NodeBU deleteElem() {
		eInd--;
		count = eInd;
		return elem[eInd + 1];
	}

	public void decPtr() {
		pInd--;
	}

	public int getCount() {
		return count;
	}

	public DataUnit checkBelong(int n) {
		for (int i = 0; i < count; i++) {
			if (elem[i].getData() > n)
				return ptrs[i];
		}
		if (ptrs[count] == null)
			return ptrs[count - 1];
		return ptrs[count];
	}

	public boolean isLeaf() {
		if (ptrs[0] instanceof FileUnit) {
			return true;
		} else {
			return false;
		}
	}

}

class FileUnit extends DataUnit {
	private int index;

	public FileUnit(int _index) {
		index = _index;
	}

	public int getIndex() {
		return index;
	}
}

class NodeBU {
	private int data;

	public NodeBU(int _data) {
		data = _data;
	}

	public int getData() {
		return data;
	}
}

abstract class Tree {
	protected NodeUnit root;

	public NodeUnit getRoot() {
		return root;
	}

	public void printRec(DataUnit d) {
		if (d == null) {
			return;
		}
		if (d instanceof FileUnit) {
			FileUnit d2 = (FileUnit) d;
			System.out.println("File Index: " + d2.getIndex());
			return;
		}
		NodeUnit d2 = (NodeUnit) d;
		int till = NodeUnit.max;
		if (d2.isLeaf()) {
			till--;
		}
		d2.printElem();
		System.out.println();
		for (int i = 0; i < till; i++) {
			printRec(d2.getPtr(i));
		}
	}

	public void printTree() {
		printRec(root);
	}

	public int findNodes() {
		return getNodes(root);
	}

	public int findHeight() {
		return getHeight(root);
	}

	public int getNodes(DataUnit d) {
		if (d == null || d instanceof FileUnit) {
			return 0;
		}
		NodeUnit d2 = (NodeUnit) d;
		int till = NodeUnit.max;
		if (d2.isLeaf()) {
			till--;
		}
		int count = 1;
		for (int i = 0; i < till; i++) {
			count += getNodes(d2.getPtr(i));
		}
		return count;
	}

	public int getHeight(DataUnit d) {
		if (d == null || d instanceof FileUnit) {
			return 0;
		}
		NodeUnit d2 = (NodeUnit) d;
		int till = NodeUnit.max;
		if (d2.isLeaf()) {
			till--;
		}
		int count = 1;
		int maxval = 0;
		for (int i = 0; i < till; i++) {
			int var = getHeight(d2.getPtr(i));
			if (var > maxval)
				maxval = var;
		}
		count += maxval;
		return count;
	}

	public void retRecord(int n) {
		NodeUnit s = root;
		DataUnit c;
		while (!((c = s.checkBelong(n)) instanceof FileUnit)) {
			s = (NodeUnit) c;
		}
		for (int i = 0; i < s.getCount(); i++) {
			if (s.getElem(i).getData() == n) {
				c = s.getPtr(i);
				System.out.println("Index of search: " + ((FileUnit) c).getIndex());
				return;
			}
		}
		System.out.println("Record Not Found!");
	}

	abstract public <E> void makeTree(List<E> data);
}


class BottomUp extends Tree {
	public void createPrnt(NodeUnit a, NodeUnit b) {
		NodeUnit n = new NodeUnit();
		root = n;
		n.putPtr(a);
		n.putPtr(b);
		n.putElem(b.getTop());
		// here
		if (!b.isLeaf()) {
			NodeBU temp = new NodeBU(b.getTop().getData() + 1);
			b.decElem();
			b.putElem(temp);
			b.decElem();
		}
		// end
		a.setPrnt(n);
		b.setPrnt(n);
	}

	public void prntAdd(NodeUnit prnt, NodeUnit d) {
		if (prnt.isFull()) {
			NodeUnit u = newContainer(d, d.getTop(), prnt);
			d.setPrnt(u);
			// here
			if (!d.isLeaf()) {
				NodeBU temp = new NodeBU(d.getTop().getData() + 1);
				d.decElem();
				d.putElem(temp);
				d.decElem();
			}
			// end
		} else {
			prnt.putPtr(d);
			prnt.putElem(d.getTop());
			d.setPrnt(prnt);
			// here
			if (!d.isLeaf()) {
				NodeBU temp = new NodeBU(d.getTop().getData() + 1);
				d.decElem();
				d.putElem(temp);
				d.decElem();
			}
			// end
		}
	}

	public NodeUnit newContainer(DataUnit d, NodeBU n, NodeUnit old) {
		NodeUnit u = new NodeUnit();
		u.putPtr(d);
		u.putElem(n);
		NodeUnit prnt = old.getPrnt();
		if (prnt != null) {
			prntAdd(prnt, u);
		} else {
			createPrnt(old, u);
		}
		return u;
	}

	public <E> void makeTree(List<E> data) {
		
			
			
			NodeUnit u = new NodeUnit();
			root = u;
			String c;
			for(int i = 0; i < data.size(); i++) {
				NodeBU n = new NodeBU((Integer) data.get(i));
				FileUnit f = new FileUnit(i);
				
				if (u.isFull()) {
					NodeUnit temp = newContainer(f, n, u);
					u.putPtr(temp);
					u = temp;
				} else {
					u.putPtr(f);
					u.putElem(n);
				}
			}
	
			
		
	}

	
}

public class BTree {
	public static void main(String[] args) throws IOException {


		System.out.println("Bottom Up:");

		List<Integer> data = new ArrayList<>();

		data.add(7);
		data.add(5);
		data.add(2);
		data.add(19);
		data.add(19);
		data.add(14);
		data.add(16);
		data.add(19);
		data.add(15);
		data.add(15);
		data.add(1);
		data.add(19);
		// sort list
		Collections.sort((List<Integer>) data);

		//Tree bup = new BottomUp();
		

		//bup.makeTree(data);

		BPlusTree b = new BPlusTree(2);
		for(int i =0; i < 10; i++) {
		BPlusTree.insertIntoTree(new DataNode(1));
		}
		BPlusTree.searchTree(2);
		
		
		
		//bup.printTree();
		
		
		
		
		

		// System.out.println(end-starttime);
		//System.out.println("Height: " + bup.findHeight() + " Number of Nodes: " + bup.findNodes() + "\n");
		// bup.retRecord(1000);

		


	}
}