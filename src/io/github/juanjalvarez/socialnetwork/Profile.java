package io.github.juanjalvarez.socialnetwork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Profiles are composed of a list of fields that define their attributes and
 * information.
 * 
 * @author Juan J. Alvarez <juan.alvarez7@upr.edu>
 *
 */
public class Profile {

	private HashMap<FieldType, ArrayList<Field>> fieldCache;
	private HashMap<String, Field> fields;
	private Set<String> fieldNames;

	/**
	 * Constructs a profile with no fields.
	 */
	public Profile() {
		fields = new HashMap<String, Field>();
		fieldCache = new HashMap<FieldType, ArrayList<Field>>();
		fieldNames = new HashSet<String>();
	}

	/**
	 * Returns the field with the given name if it exists.
	 * 
	 * @param field
	 *            Name of the field being searched for.
	 * @return Field with the given name.
	 */
	public Field getField(String field) {
		return fields.get(field);
	}

	/**
	 * Adds the given field to the profile in context.
	 * 
	 * @param field
	 *            New field to add to the profile.
	 */
	public void addField(Field field) {
		fields.put(field.getName(), field);
		ArrayList<Field> fieldList = fieldCache.get(field.getType());
		if (fieldList == null)
			fieldList = new ArrayList<Field>();
		fieldList.add(field);
		fieldCache.put(field.getType(), fieldList);
		fieldNames.add(field.getName());
	}

	/**
	 * Deletes the field with the given name if it exists.
	 * 
	 * @param name
	 *            Name of the field being deleted.
	 */
	public void deleteField(String name) {
		fields.remove(fields.get(name));
	}

	/**
	 * Gets a list of field names.
	 * 
	 * @return List of field names.
	 */
	public String[] getFieldNames() {
		return fieldNames.toArray(new String[fieldNames.size()]);
	}

	/**
	 * Gets a list off all of the fields of the given type.
	 * 
	 * @param type
	 *            Type of field being queried.
	 * @return List of fields of the given type.
	 */
	public Field[] getFieldsByType(FieldType type) {
		ArrayList<Field> tmp = fieldCache.get(type);
		if (tmp == null)
			return new Field[0];
		return tmp.toArray(new Field[tmp.size()]);
	}

	/**
	 * Gets all of the fields in the profile.
	 * 
	 * @return List of all fields.
	 */
	public Field[] getAllFields() {
		ArrayList<Field> list = new ArrayList<Field>();
		for (FieldType type : FieldType.values())
			if (fieldCache.get(type) != null)
				list.addAll(fieldCache.get(type));
		return list.toArray(new Field[list.size()]);
	}

	/**
	 * Forms a profile from the given string.
	 * 
	 * @param data
	 *            String to be parsed.
	 * @return Profile formed from the given string.
	 */
	public static Profile parseProfile(String data) {
		String[] lines = data.split("\\n");
		if (lines.length == 0)
			try {
				throw new Exception("No data received");
			} catch (Exception e) {
				e.printStackTrace();
			}
		Profile p = new Profile();
		try {
			for (String s : lines)
				p.addField(Field.parseField(s));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		ArrayList<Field[]> list = new ArrayList<Field[]>();
		for (FieldType type : FieldType.values())
			list.add(getFieldsByType(type));
		int x, y;
		Field[] tmp;
		for (x = 0; x < list.size(); x++)
			for (y = x + 1; y < list.size(); y++)
				if (list.get(y).length < list.get(x).length) {
					tmp = list.get(x);
					list.set(x, list.get(y));
					list.set(y, tmp);
				}
		for (x = 0; x < list.size(); x++) {
			tmp = list.get(x);
			for (y = 0; y < tmp.length; y++) {
				sb.append(tmp[y].toString());
				if (!(x == list.size() - 1 && y == tmp.length - 1))
					sb.append("\n");
			}
		}
		return sb.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Profile))
			return false;
		Profile p = (Profile) o;
		String[] keyA = getFieldNames();
		String[] keyB = getFieldNames();
		Field fieldA, fieldB;
		String tmpField;
		if (keyA.length != keyB.length)
			return false;
		int x;
		for (x = 0; x < keyA.length; x++) {
			tmpField = keyA[x];
			if (!tmpField.equals(keyB[x]))
				return false;
			fieldA = getField(tmpField);
			fieldB = p.getField(tmpField);
			if (fieldA.getType() != fieldB.getType())
				return false;
			if (!fieldA.getValue().equals(fieldB.getValue()))
				return false;
		}
		return true;
	}
}