package io.github.juanjalvarez.socialnetwork;

public class Field {

	private FieldType type;
	private String name;
	private String value;

	public Field(FieldType type, String name, String value) {
		this.type = type;
		this.name = name;
		this.value = value;
	}

	public FieldType getType() {
		return type;
	}

	public void setType(FieldType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static Field parseField(String data) {
		String[] split = data.split(":|>");
		if (split.length < 3)
			try {
				throw new Exception(String.format("'%s' is not a valid string form of Field", data));
			} catch (Exception e) {
				e.printStackTrace();
			}
		String typeString = split[0];
		String nameString = split[1];
		StringBuilder sb = new StringBuilder();
		for (int x = 2; x < split.length; x++)
			sb.append(split[x]);
		String valueString = sb.toString();
		FieldType newType = FieldType.ID;
		for (FieldType curType : FieldType.values())
			if (curType.toString().equals(typeString))
				newType = curType;
		return new Field(newType, nameString, valueString);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(type.toString());
		sb.append(":");
		sb.append(name);
		sb.append(">");
		sb.append(value);
		return sb.toString();
	}
}