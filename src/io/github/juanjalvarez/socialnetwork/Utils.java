package io.github.juanjalvarez.socialnetwork;

import java.util.HashSet;

/**
 * A number of miscellaneous utilities used across the software.
 * 
 * @author Juan J. Alvarez <juan.alvarez7@upr.edu>
 *
 */
public class Utils {

	/**
	 * Genereates a string by repeated the given string by the given amount of
	 * times.
	 * 
	 * @param tgt
	 *            String to be repeated by the given number of times.
	 * @param reps
	 *            The number of times to repeat the target string.
	 * @return The target string repeated by the given amount of times.
	 */
	public static String strRepeat(String tgt, int reps) {
		StringBuilder sb = new StringBuilder();
		for (int x = 0; x < reps; x++)
			sb.append(tgt);
		return sb.toString();
	}

	/**
	 * Filters the given list of elements and removes any repetitions. The
	 * result is a unique list of elements.
	 * 
	 * @param arr
	 *            Array to be filtered.
	 * @return Array of unique elements.
	 */
	@SuppressWarnings("unchecked")
	public static <E> E[] getSet(E[] arr) {
		HashSet<E> set = new HashSet<E>();
		for (E e : arr)
			set.add(e);
		return set.toArray((E[]) new Object[0]);
	}
}