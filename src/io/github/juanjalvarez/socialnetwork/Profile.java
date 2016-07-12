package io.github.juanjalvarez.socialnetwork;

import java.util.ArrayList;
import java.util.HashMap;

public class Profile {

	private HashMap<FieldType, ArrayList<Field>> fieldCache;
	private HashMap<String, Field> fields;

	public Profile() {
		fields = new HashMap<String, Field>();
		fieldCache = new HashMap<FieldType, ArrayList<Field>>();
	}

	public Field getField(String field) {
		return fields.get(field);
	}

	public void addField(Field field) {
		fields.put(field.getName(), field);
		ArrayList<Field> fieldList = fieldCache.get(field.getType());
		if (fieldList == null)
			fieldList = new ArrayList<Field>();
		fieldList.add(field);
		fieldCache.put(field.getType(), fieldList);
	}

	public void deleteField(String name) {
		fields.remove(fields.get(name));
	}

	public Field[] getFieldByType(FieldType type) {
		ArrayList<Field> tmp = fieldCache.get(type);
		if (tmp == null)
			return new Field[0];
		return tmp.toArray(new Field[tmp.size()]);
	}

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
		String[] set = fields.keySet().toArray(new String[fields.keySet().size()]);
		for (int x = 0; x < set.length; x++) {
			sb.append(fields.get(set[x]).toString());
			if (x != set.length - 1)
				sb.append("\n");
		}
		return sb.toString();
	}
}