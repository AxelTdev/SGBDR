package Code.type;

public class Type {

	private String value;
	private int size;

	public Type(String str) {
		if (str.startsWith("string")) {
			String temp = str.substring(6);
			this.size = Integer.parseInt(temp);
			
			System.out.println(this.size);
			this.value = "string";

		}
		if (str.startsWith("int")) {
			this.size = 4;
			this.value = "int";
		}
		if (str.startsWith("float")) {
			this.size = 4;
			this.value = "float";
		}

	}

	public int getSize() {
		return this.size;
	}

	public String getValue() {
		return value;
	}

	public enum Typeenum {
		INT("int"), FLOAT("float"), STRING("string");

		private String value;
		private int size;

		Typeenum(String type) {
			this.value = "string";
			if (type.startsWith("string")) {
				this.value = "string";
			} else {
				this.value = type;
				this.size = 0;
			}

		}

		public String getType() {
			if (size > 0)
				return value + size;
			else
				return value;
		}

		public void setSize(int size) {
			this.size = size;
		}

		public int getSize() {
			return this.size;
		}
	}
}
