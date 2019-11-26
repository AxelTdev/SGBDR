package Code.record;

import java.nio.ByteBuffer;
import Code.reldef.RelDef;
import Code.type.Type;
import Code.util.Constants;

public class Record {

	private RelDef refdef;
	private String[] values;

	public Record(RelDef reldef) {
		this.refdef = reldef;

	}

	public void setReldef(RelDef reldef) {
		this.refdef = reldef;
	}

	public void writeToBuffer(byte[] buff, int position) {
		ByteBuffer bb = ByteBuffer.wrap(buff);
		bb.position(position);

		for (int i = 0; i < refdef.getTypeColonne().length; i++) {
			if (refdef.getTypeColonne()[i].getValue().startsWith("string")) {
				int longeur = values[i].length();

				char[] tableauChar = values[i].toCharArray();

				for (int y = 0; y < longeur; y++) {

					bb.putChar(tableauChar[y]);

				}
				continue;

			}

			switch (refdef.getTypeColonne()[i].getValue()) {

			case "int":
				int a = Integer.parseInt(values[i]);
				bb.putInt(a);

				break;
			case "float":
				float b = Float.parseFloat(values[i]);
				bb.putFloat(b);
				break;
			default:

			}

		}
	}

	public void readFromBuffer(byte[] buff, int position) {
		ByteBuffer bb = ByteBuffer.wrap(buff);
		this.values = new String[this.refdef.getTypeColonne().length];
		bb.position();
		String str = null;
		int position1 = 0;

		for (int i = 0; i < refdef.getTypeColonne().length; i++) {
			if (refdef.getTypeColonne()[i].getValue().startsWith("string")) {
				int longeur = refdef.getTypeColonne()[i].getSize();
				bb.position();
				byte[] tabString = new byte[longeur];
				// remplir le ByteBuffer
				char[] tabChar = new char[longeur];

				for (int y = 0, p = position1; y < longeur; y++, p += 2) {
					tabChar[y] = bb.getChar(p);

				}
				String strTemp = String.valueOf(tabChar);

				values[i] = strTemp;
				position1 = (longeur * 2);
				bb.position(position1);

				continue;
			}

			switch (refdef.getTypeColonne()[i].getValue()) {
			case "int":

				str = Integer.toString(bb.getInt());
				values[i] = str;

				break;
			case "float":
				str = Float.toString(bb.getFloat());
				values[i] = str;
				break;
			default:

			}

		}

	}

	@Override
	public String toString() {
		String str = "valeurs du record \n";
		for (int i = 0; i < this.getValues().length - 1; i++) {
			str += this.getValues()[i] + " ; ";
		}
		str +=this.getValues()[this.getValues().length-1];
		return str;
	}

	public void setValues(String[] temp) {
		this.values = temp;
	}

	public String[] getValues() {
		return this.values;
	}

	
	  public static void main(String [] args) {
	  
	  Record rd = new Record(new RelDef(1,5,8));
	  String [] temp = {"yo", "ttt", "eze"};
	  rd.setValues(temp);
	  System.out.print(rd);
	 
	  }
	 
}
