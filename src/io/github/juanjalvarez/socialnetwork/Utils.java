package io.github.juanjalvarez.socialnetwork;

import java.util.HashSet;

public class Utils {

	public static String strRepeat(String tgt, int reps) {
		StringBuilder sb = new StringBuilder();
		for (int x = 0; x < reps; x++)
			sb.append(tgt);
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	public static <E> E[] getSet(E[] arr) {
		HashSet<E> set = new HashSet<E>();
		for (E e : arr)
			set.add(e);
		return set.toArray((E[]) new Object[0]);
	}
}