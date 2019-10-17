package Code.record;

import java.nio.ByteBuffer;
import Code.reldef.RelDef;
import Code.reldef.RelDef.Type;
import Code.util.Constants;

public class Record {
	private RelDef refdef;
	private String[] values;

	public Record(RelDef reldef) {
		this.refdef = reldef;

	}

	public void writeToBuffer(byte[] buff, int position) {
		ByteBuffer bb = ByteBuffer.wrap(buff);
		bb.position(position);

		for (int i = 0; i < refdef.getTypeColonne().length; i++) {

			switch (refdef.getTypeColonne()[i]) {
			case INT:
				int a = Integer.parseInt(values[i]);
				bb.putInt(a);

				break;
			case FLOAT:
				float b = Float.parseFloat(values[i]);
				bb.putFloat(b);
				break;
			case STRING:
				int longeur = refdef.getTypeColonne()[i].getSize();
				char[] tableauChar = values[i].toCharArray();
				for (int y = 0; y < longeur; y++) {

					bb.putChar(tableauChar[y]);

				}
				break;

			default:

				System.out.println("type de colonne non reconnu");
			}

		}
	}

	public void readFromBuffer(byte[] buff, int position) {
		ByteBuffer bb = ByteBuffer.wrap(buff);
		bb.position();
		String str = null;
		for (int i = 0; i < refdef.getTypeColonne().length; i++) {
			switch (refdef.getTypeColonne()[i]) {
			case INT:

				str = Integer.toString(bb.getInt());
				values[i] = str;
				break;
			case FLOAT:
				str = Float.toString(bb.getFloat());
				values[i] = str;
				break;
			case STRING:

				int longeur = refdef.getTypeColonne()[i].getSize();
				char[] tabString = new char[longeur];
				// remplir le ByteBuffer

				for (int y = 0; y < longeur; y++) {
					tabString[y] = bb.getChar();

				}
				values[i] = new String(tabString);

				break;
			default:
				System.out.println("type de colonne non reconnu");

			}

		}

	}

	public void setValues(String[] temp) {
		this.values = temp;
	}

	public String[] getValues() {
		return this.values;
	}
}
