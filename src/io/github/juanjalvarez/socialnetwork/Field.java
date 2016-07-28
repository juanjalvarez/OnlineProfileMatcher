package io.github.juanjalvarez.socialnetwork;

/**
 * Field defined by a field type for categorizational purposes, a file name and
 * an actual value.
 * 
 * @author Juan J. Alvarez <juan.alvarez7@upr.edu>
 *
 */
public class Field {

	private FieldType type;
	private String name;
	private String value;

	/**
	 * Constructs the field based on the three given attributes.
	 * 
	 * @param type
	 *            Type of field.
	 * @param name
	 *            Name of the field.
	 * @param value
	 *            Value stored in the field.
	 */
	public Field(FieldType type, String name, String value) {
		this.type = type;
		this.name = name;
		if (value == null || value.equals(""))
			this.value = "null";
		else
			this.value = value;
	}

	/**
	 * Returns the type of field.
	 * 
	 * @return Type of field.
	 */
	public FieldType getType() {
		return type;
	}

	/**
	 * Sets the type of field.
	 * 
	 * @param type
	 *            New type of field.
	 */
	public void setType(FieldType type) {
		this.type = type;
	}

	/**
	 * Returns the name of the field.
	 * 
	 * @return Name of the field.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the field.
	 * 
	 * @param name
	 *            New name of the field.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the value stored in the field.
	 * 
	 * @return Value stored in the field.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value stored in the field.
	 * 
	 * @param value
	 *            Value to be stored in the field.
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Creates a Field from the given string.
	 * 
	 * @param data
	 *            String to form a Field from.
	 * @return Field formed from the given string.
	 * @throws Exception
	 *             If the given string could not be parsed.
	 */
	public static Field parseField(String data) throws Exception {
		String[] split = data.split(":|>");
		if (split.length < 3)
			throw new Exception(String.format("'%s' is not a valid string form of Field", data));
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