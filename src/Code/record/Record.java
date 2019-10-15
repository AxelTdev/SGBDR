package Code.record;

import java.nio.ByteBuffer;

import Code.reldef.RelDef;
import Code.util.Constants;

public class Record {
	private RelDef refdef;
	private String[] values;

	public Record(RelDef reldef) {
		this.refdef = reldef;

	}

	public void writeToBuffer(byte[] buff, int position) {
		ByteBuffer bb = ByteBuffer.wrap(buff);

		for (int i = 0; i < refdef.getTypeColonne().length; i++) {

			switch (refdef.getTypeColonne()[i]) {
			case "int":
				int a = Integer.parseInt(values[i]);
				bb.putInt(a);
				break;
			case "float":
				float b = Float.parseFloat(values[i]);
				bb.putFloat(b);
				break;
			default:
				if (refdef.getTypeColonne()[i].contains("string")) {
					// savoir la longueur du string
					char longeur = refdef.getTypeColonne()[i].charAt(refdef.getTypeColonne()[i].length() - 1);
					int longeurInt = Character.getNumericValue(longeur);

					char[] tableauChar = values[i].toCharArray();

					// remplir le ByteBuffer
					for (int y = 0; y < longeurInt; y++) {
						System.out.println(tableauChar[y]);
						bb.putChar(tableauChar[y]);

					}
				} else {
					System.out.println("type de colonne non reconnu");
				}

			}
		}

	}

	public void readFromBuffer(byte[] buff, int position) {
		ByteBuffer bb = ByteBuffer.wrap(buff);
		String str = null;
		for (int i = 0; i < refdef.getTypeColonne().length; i++) {
			switch (refdef.getTypeColonne()[i]) {
			case "int":

				str = Integer.toString(bb.getInt());
				values[i] = str;
				break;
			case "float":
				str = Float.toString(bb.getFloat());
				values[i] = str;
				break;
			default:
				if (refdef.getTypeColonne()[i].contains("string")) {
					// savoir la longueur du string
					char longeur = refdef.getTypeColonne()[i].charAt(refdef.getTypeColonne()[i].length() - 1);
					int longeurInt = Character.getNumericValue(longeur);

					char[] tabString = new char[longeurInt];
					// remplir le ByteBuffer

					for (int y = 0; y < longeurInt; y++) {
						tabString[y] = bb.getChar();

					}
					values[i] = new String(tabString);
				} else {
					System.out.println("type de colonne non reconnu");
				}

			}

		}

	}

	public void setValues(String[] temp) {
		this.values = temp;
	}

	public String[] getValues() {
		return this.values;
	}

	public static void main(String args[]) {
		byte[] buff = new byte[Constants.pageSize];
		RelDef reldef = new RelDef();
		String[] typeColonne = { "int", "float", "string3" };
		reldef.setTypeColonne(typeColonne);
		Record r = new Record(reldef);
		String[] temp = { "3", "3.3", "axe" };
		r.setValues(temp);

		r.writeToBuffer(buff, 0);// comment utiliser le parametre position
		for (int i = 0; i < buff.length; i++) {
			System.out.println(buff[i]);
		}

		String[] temp1 = { "2", "2", "3" };
		r.setValues(temp1);
		r.readFromBuffer(buff, 0);

		for (int i = 0; i < r.getValues().length; i++) {
			System.out.println(r.values[i]);
		}
	}
}
