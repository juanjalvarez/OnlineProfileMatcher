package io.github.juanjalvarez.socialnetwork;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;

/**
 * Collection of algorithms used throughout the application.
 * 
 * @author Juan J. Alvarez <juanalvarez2@mail.usf.edu>
 *
 */
public class Algorithms {

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
	 * Finds and yields the longest substring common to both given strings.
	 * 
	 * @param str1
	 *            First string.
	 * @param str2
	 *            Second string.
	 * @return Longest substring common to both given strings.
	 */
	public static String longestCommonSubstring(String str1, String str2) {
		String longest = "", section;
		int x, y;
		for (x = 0; x < str1.length(); x++)
			for (y = x; y < str1.length(); y++) {
				section = str1.substring(x, y + 1);
				if (str2.contains(section) && section.length() > longest.length())
					longest = section;
			}
		return longest;
	}

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
			rep1[x] = Algorithms.<E> countRepetitions(arr1, repVal[x]);
		for (x = 0; x < repVal.length; x++)
			rep2[x] = Algorithms.<E> countRepetitions(arr2, repVal[x]);
		double numerator = 0, part1 = 0.0, part2 = 0.0, denominator;
		for (x = 0; x < repVal.length; x++) {
			numerator += rep1[x] * rep2[x];
			part1 += rep1[x] * rep1[x];
			part2 += rep2[x] * rep2[x];
		}
		denominator = Math.sqrt(part1) * Math.sqrt(part2);
		return numerator / denominator;
	}

	public static double cosineSimilarity(String str1, String str2) {
		return Algorithms.<Character> cosineSimilarity(primitiveArrayToWrapperArray(str1.toCharArray()),
				primitiveArrayToWrapperArray(str2.toCharArray()));
	}

	/**
	 * Yields the longest common substring name similarity ratio.
	 * 
	 * @param str1
	 *            First string.
	 * @param str2
	 *            Second string.
	 * @return Longest common substring name similarity ratio.
	 */
	public static double LCSStringSimilarity(String str1, String str2) {
		String strA = str1, strB = str2;
		String lcs = "";
		while (true) {
			lcs = longestCommonSubstring(strA, strB);
			if (lcs.equals(""))
				break;
			strA = strA.replace(lcs, "");
			strB = strB.replace(lcs, "");
		}
		return 1.0 - ((double) (strA.length() + strB.length()) / (double) (str1.length() + str2.length()));
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
		return Algorithms.<Character> uniqueElementRatio(primitiveArrayToWrapperArray(str1.toCharArray()),
				primitiveArrayToWrapperArray(str2.toCharArray()));
	}

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
			difference += Math.abs(Algorithms.<E> countRepetitions(arr1, repVal[x])
					- Algorithms.<E> countRepetitions(arr2, repVal[x]));
		return 1.0 - (double) difference / (double) (arr1.length + arr2.length);
	}

	public static double elementDifferenceRatio(String str1, String str2) {
		return Algorithms.<Character> elementDifferenceRatio(primitiveArrayToWrapperArray(str1.toCharArray()),
				primitiveArrayToWrapperArray(str2.toCharArray()));
	}

	public static double hammingDistance(byte[] seq1, byte[] seq2) {
		byte[] small = seq1;
		byte[] big = seq2;
		if (small.length > big.length) {
			byte[] tmp = small;
			small = big;
			big = tmp;
		}
		int diff = big.length - small.length, x;
		for (x = 0; x < small.length; x++)
			if (small[x] != big[x])
				diff++;
		return 1.0 - ((double) diff / (double) big.length);
	}

	public static double hammingDistance(String str1, String str2) {
		return hammingDistance(str1.getBytes(), str2.getBytes());
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

	public static void main(String[] a) throws Exception {
		System.out.println("Please enter the two strings to compare, separated by '\\n'");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String str1 = br.readLine();
		String str2 = br.readLine();
		br.close();
		System.out.println(String.format("String1: '%s'\nString2: '%s'", str1, str2));
		System.out.println();
		System.out.println(String.format("LCS similarity: %.2f", LCSStringSimilarity(str1, str2)));
		System.out.println(String.format("UER similarity: %.2f", Algorithms.uniqueElementRatio(str1, str2)));
		System.out.println(String.format("Cosine similarity: %.2f", cosineSimilarity(str1, str2)));
		System.out.println(String.format("EDR similarity: %.2f", elementDifferenceRatio(str1, str2)));
		System.out.println(String.format("Hamming distance: %.2f", hammingDistance(str1, str2)));
	}
}