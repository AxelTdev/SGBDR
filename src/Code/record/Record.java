package Code.record;

import java.nio.ByteBuffer;
import Code.reldef.RelDef;
import Code.type.Type;
import Code.util.Constants;

public class Record {
	private static final Code.type.Type INT = new Code.type.Type("int");
	private static final Code.type.Type FLOAT = new Code.type.Type("float");
	
	private RelDef refdef;
	private String[] values;

	public Record(RelDef reldef) {
		this.refdef = reldef;

	}

	public void writeToBuffer(byte[] buff, int position) {
		ByteBuffer bb = ByteBuffer.wrap(buff);
		bb.position(position);

		for (int i = 0; i < refdef.getTypeColonne().length; i++) {

			switch (refdef.getTypeColonne()[i].getValue()) {
			
			case "int":
				int a = Integer.parseInt(values[i]);
				bb.putInt(a);

				break;
			case "float":
				float b = Float.parseFloat(values[i]);
				bb.putFloat(b);
				break;
			default :
				System.out.println("rentre string");
				int longeur = values[i].length();
				System.out.println("longeur des typecol");
				System.out.println(longeur);
				System.out.println("longeur des typecol");
				char[] tableauChar = values[i].toCharArray();
				System.out.println("debut tableau de char");
				for (int y = 0; y < longeur; y++) {
					
					bb.putChar(tableauChar[y]);
					
					
				}
				for (int y = 0; y < longeur*2; y =y+2) {
					
					System.out.println(bb.getChar(y));
					
					
				}
				
				
				System.out.println("fin tableau de char");
				break;

			
			}

		}
	}

	public void readFromBuffer(byte[] buff, int position) {
		ByteBuffer bb = ByteBuffer.wrap(buff);
		this.values = new String[this.refdef.getTypeColonne().length];
		bb.position();
		String str = null;
		for (int i = 0; i < refdef.getTypeColonne().length; i++) {
			switch (refdef.getTypeColonne()[i].getValue()) {
			case "int":

				str = Integer.toString(bb.getInt());
				values[i] = str;
				break;
			case "float":
				str = Float.toString(bb.getFloat());
				values[i] = str;
				break;
			default :
				int longeur = refdef.getTypeColonne()[i].getSize();
				
				byte[] tabString = new byte[longeur*2];
				// remplir le ByteBuffer
				for(byte test : buff) {
					System.out.println(test);
				}
				for (int y = 0; y < longeur; y++) {
					tabString[y] = bb.get();
					

				}
				String strTemp =  new String(tabString);
				
			
				
				
				values[i] =strTemp;

				break;
			

			}

		}

	}

	public void setValues(String[] temp) {
		this.values = temp;
	}

	public String[] getValues() {
		return this.values;
	}
	
	public static void main(String [] args) {
		
		String t = "axe";
		String t22 = "laurentlll";
		
		byte [] l = t.getBytes();
		byte[] l1 = t22.getBytes();
		
		byte[] c = new byte[l.length + l1.length];
		System.arraycopy(l, 0, c, 0, l.length);
		System.arraycopy(l1, 0, c, l.length, l1.length);
		
		Type t3 = new Type("string3");
		Type t4 = new Type("string10");
		Type [] typeCol = { t3, t4};
		RelDef rel = new RelDef(1,17,2);
		rel.setTypeColonne(typeCol);
		Record r = new Record(rel);
		r.readFromBuffer(c, 0);
		
		for(int i = 0; i <r.values.length; i++) {
			System.out.println(r.values[i]);
			System.out.println("longeur des string");
			System.out.println(rel.getTypeColonne()[i].getSize());
			
		}
		
		//byte [] b = new byte[1100];
		//r.writeToBuffer(b, 0);
		//ByteBuffer bb = ByteBuffer.wrap(b);
		/*for(int i = 0; i < b.length; i++) {
			System.out.println(bb.get());
		}*/
	}
}
