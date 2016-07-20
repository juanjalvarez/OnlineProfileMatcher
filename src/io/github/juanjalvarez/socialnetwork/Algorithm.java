package io.github.juanjalvarez.socialnetwork;

import java.util.HashSet;

import info.debatty.java.stringsimilarity.Cosine;
import info.debatty.java.stringsimilarity.JaroWinkler;
import info.debatty.java.stringsimilarity.MetricLCS;
import info.debatty.java.stringsimilarity.NGram;
import info.debatty.java.stringsimilarity.NormalizedLevenshtein;

/**
 * Collection of algorithms for various purposes such as string comparison.
 * 
 * @author Juan J. Alvarez <juanalvarez2@mail.usf.edu>
 *
 */
public class Algorithm {

	public static final MetricLCS METRIC_LCS = new MetricLCS();
	public static final NormalizedLevenshtein LEVENSHTEIN = new NormalizedLevenshtein();
	public static final JaroWinkler JARO_WINKLER = new JaroWinkler();
	public static final Cosine COSINE = new Cosine();
	public static final NGram N_GRAM = new NGram(2);

	/**
	 * Yields a string representation of the phonetic code of the given string.
	 * 
	 * @param s
	 *            String to get the phonetic code from.
	 * @return Phonetic code of the given string.
	 */
	public static String soundex(String s) {
		char[] charArray = s.toUpperCase().toCharArray();
		if (charArray.length == 0)
			return "ZERO";
		char firstChar = charArray[0];
		for (int i = 0; i < charArray.length; i++) {
			switch (charArray[i]) {
			case 'B':
			case 'F':
			case 'P':
			case 'V': {
				charArray[i] = '1';
				break;
			}
			case 'C':
			case 'G':
			case 'J':
			case 'K':
			case 'Q':
			case 'S':
			case 'X':
			case 'Z': {
				charArray[i] = '2';
				break;
			}
			case 'D':
			case 'T': {
				charArray[i] = '3';
				break;
			}

			case 'L': {
				charArray[i] = '4';
				break;
			}
			case 'M':
			case 'N': {
				charArray[i] = '5';
				break;
			}
			case 'R': {
				charArray[i] = '6';
				break;
			}
			default: {
				charArray[i] = '0';
				break;
			}
			}
		}
		StringBuilder sb = new StringBuilder();
		if (Character.isAlphabetic(firstChar))
			sb.append(firstChar);
		else
			sb.append('0');
		for (int i = 1; i < charArray.length; i++)
			if (charArray[i] != charArray[i - 1] && charArray[i] != '0')
				sb.append(charArray[i]);
		String result = sb.toString();
		return result.length() < 4 ? result + Utils.strRepeat("0", 4 - result.length())
				: result.length() > 4 ? result.substring(0, 4) : result;
	}

	/**
	 * Creates a string representing the set of characters in the target string.
	 * 
	 * @param s
	 *            Target string.
	 * @return String representation of the set of characters in the target
	 *         string.
	 */
	public static String eliminateRepeatedCharacters(String s) {
		HashSet<Character> map = new HashSet<Character>();
		for (Character c : s.toCharArray())
			map.add(c);
		StringBuilder sb = new StringBuilder();
		for (Character c : map)
			sb.append(c);
		return sb.toString();
	}

	/**
	 * Calculates the similarity of two lists based on the repetitions of their
	 * common elements.
	 * 
	 * @param arr1
	 *            First list.
	 * @param arr2
	 *            Second list.
	 * @return Similarity between arr1 and arr2.
	 */
	public static <E> double cosineSimilarity(E[] arr1, E[] arr2) {
		HashSet<E> set = new HashSet<E>();
		for (E e : arr1)
			set.add(e);
		for (E e : arr2)
			set.add(e);
		@SuppressWarnings("unchecked")
		E[] repVal = (E[]) set.toArray();
		int[] rep1 = new int[set.size()], rep2 = new int[set.size()];
		int x;
		for (x = 0; x < repVal.length; x++)
			rep1[x] = Algorithm.<E> countRepetitions(arr1, repVal[x]);
		for (x = 0; x < repVal.length; x++)
			rep2[x] = Algorithm.<E> countRepetitions(arr2, repVal[x]);
		double numerator = 0, part1 = 0.0, part2 = 0.0, denominator;
		for (x = 0; x < repVal.length; x++) {
			numerator += rep1[x] * rep2[x];
			part1 += rep1[x] * rep1[x];
			part2 += rep2[x] * rep2[x];
		}
		denominator = Math.sqrt(part1) * Math.sqrt(part2);
		return numerator / denominator;
	}

	/**
	 * Calculates the similarity of two strings based on the repeated characters
	 * that are common to the two given strings.
	 * 
	 * @param str1
	 *            First string.
	 * @param str2
	 *            Second string.
	 * @return Similarity between str1 and str2.
	 */
	public static double cosineSimilarity(String str1, String str2) {
		return Algorithm.<Character> cosineSimilarity(primitiveArrayToWrapperArray(str1.toCharArray()),
				primitiveArrayToWrapperArray(str2.toCharArray()));
	}

	/**
	 * 
	 * Yields the similarity ratio between the amount of common elements in the
	 * two given arrays and the total amount of unique values between the two
	 * given arrays.
	 * 
	 * @param firstList
	 *            First array.
	 * @param secondList
	 *            Second array.
	 * @return Similarity ratio for the two given arrays.
	 */
	public static <E> double uniqueElementRatio(E[] firstList, E[] secondList) {
		double hits = 0.0;
		if (firstList == null || secondList == null)
			return hits;
		E[] arr1 = Utils.getSet(firstList);
		E[] arr2 = Utils.getSet(secondList);
		HashSet<E> set = new HashSet<E>();
		for (E e : arr1)
			set.add(e);
		for (E e : arr2)
			if (set.contains(e))
				hits += 1.0;
			else
				set.add(e);
		return hits / (double) set.size();
	}

	/**
	 * Yields the similarity ratio between the common characters in the two
	 * given strings and the total amount of unique characters in the
	 * concatenation of both given strings.
	 * 
	 * @param str1
	 *            First string.
	 * @param str2
	 *            Second string.
	 * @return Similarity ratio for the two given strings.
	 */
	public static double uniqueElementRatio(String str1, String str2) {
		return Algorithm.<Character> uniqueElementRatio(primitiveArrayToWrapperArray(str1.toCharArray()),
				primitiveArrayToWrapperArray(str2.toCharArray()));
	}

	/**
	 * Yields the similarity ratio between the two given arrays similarly to how
	 * UniqueElementRatio does it but without taking into consideration the
	 * uniqueness of the common elements.
	 * 
	 * @param arr1
	 *            First array.
	 * @param arr2
	 *            Second array.
	 * @return Similarity ratio between arr1 and arr2.
	 */
	public static <E> double elementDifferenceRatio(E[] arr1, E[] arr2) {
		HashSet<E> set = new HashSet<E>();
		for (E e : arr1)
			set.add(e);
		for (E e : arr2)
			set.add(e);
		@SuppressWarnings("unchecked")
		E[] repVal = (E[]) set.toArray();
		int x, difference = 0;
		for (x = 0; x < repVal.length; x++)
			difference += Math.abs(
					Algorithm.<E> countRepetitions(arr1, repVal[x]) - Algorithm.<E> countRepetitions(arr2, repVal[x]));
		return 1.0 - (double) difference / (double) (arr1.length + arr2.length);
	}

	/**
	 * Yields the similarity ratio between two arrays using the
	 * ElementDifferenceRatio algorithm except with the characters of the two
	 * given trings.
	 * 
	 * @param str1
	 *            First string.
	 * @param str2
	 *            Second string.
	 * @return Similarity ratio between str1 and str2.
	 */
	public static double elementDifferenceRatio(String str1, String str2) {
		return Algorithm.<Character> elementDifferenceRatio(primitiveArrayToWrapperArray(str1.toCharArray()),
				primitiveArrayToWrapperArray(str2.toCharArray()));
	}

	private static double catSequence(String str1, String str2, boolean forward) {
		String a = str1, b = str2, tmp;
		if (b.length() < a.length()) {
			tmp = a;
			a = b;
			b = tmp;
		}
		int score = 0, idx = 0, x;
		for (x = forward ? 0 : b.length() - 1; forward ? (x < b.length() && idx < a.length())
				: (x > -1 && idx < a.length()); x = forward ? x + 1 : x - 1) {
			if (b.charAt(x) == a.charAt(idx)) {
				score++;
				idx++;
			}
		}
		return (double) score / (double) b.length();
	}

	public static double catSequenceAlgorithm(String str1, String str2) {
		return Math.max(catSequence(str1, str2, true), catSequence(str1, str2, false));
	}

	/**
	 * Converts a char array to a Character array.
	 * 
	 * @param arr
	 *            char array to be converted.
	 * @return Character array formed from the given char array.
	 */
	public static Character[] primitiveArrayToWrapperArray(char[] arr) {
		Character[] newArr = new Character[arr.length];
		for (int x = 0; x < arr.length; x++)
			newArr[x] = new Character(arr[x]);
		return newArr;
	}

	/**
	 * Claculates the factorial of any positive number.
	 * 
	 * @param n
	 *            The number which to calculate the factorial of.
	 * @return Factorial of the given number b.
	 */
	public static synchronized long factorial(long n) {
		long total = 0;
		while (n > 0)
			total += n--;
		return total;
	}

	/**
	 * Counts the repetitions of the given value in an array.
	 * 
	 * @param arr
	 *            Array to count in.
	 * @param val
	 *            Value to count.
	 * @return Amount of times the given value is repeated in the given array.
	 */
	public static <E> int countRepetitions(E[] arr, E val) {
		int count = 0;
		for (E e : arr)
			if (e.equals(val))
				count++;
		return count;
	}
}