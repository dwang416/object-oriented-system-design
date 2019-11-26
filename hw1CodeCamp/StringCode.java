import java.util.HashSet;
import java.util.Set;

// CS108 HW1 -- String static methods

public class StringCode {

	/**
	 * Given a string, returns the length of the largest run.
	 * A a run is a series of adjacent chars that are the same.
	 * So the max run of "xxyyyz" is 3, and the max run of "xyz" is 1
	 * @param str
	 * @return max run length
	 */
	public static int maxRun(String str) {
		if (str.length() == 0) return 0; // empty string
		char cur = str.charAt(0);
		int cnt = 1, res = 1;
		for (int i = 1; i < str.length(); i++) {
			if (str.charAt(i) == cur) cnt++;
			else {
				cur = str.charAt(i);
				res = Math.max(cnt, res);
				cnt = 1;
			}
		}
		res = Math.max(cnt, res);
		return res;
	}

	
	/**
	 * Given a string, for each digit in the original string,
	 * replaces the digit with that many occurrences of the character
	 * following. So the string "a3tx2z" yields "attttxzzz".
	 * and "12x" yields "2xxx"
	 * @param str
	 * @return blown up string
	 */
	public static String blowup(String str) {
		if (str.length() == 0) return "";	// empty string
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			if (Character.isDigit(str.charAt(i))) {
				if (i == str.length() - 1) break;
				int occurs = Character.getNumericValue(str.charAt(i));
				char ch = str.charAt(i+1);
				for (int cnt = 0; cnt < occurs; cnt++) sb.append(ch);
			}
			else {
				sb.append(str.charAt(i));
			}
		}
		return sb.toString();
	}
	
	/**
	 * Given 2 strings, consider all the substrings within them
	 * of length len. Returns true if there are any such substrings
	 * which appear in both strings.
	 * Compute this in linear time using a HashSet. Len will be 1 or more.
	 */
	public static boolean stringIntersect(String a, String b, int len) {
		if (a.length() < len || b.length() < len) return false;
		Set<String> set = new HashSet<>();
		for (int i = 0; i < a.length() - len + 1; i++) {
			set.add(a.substring(i, i+len));
		}
		for (int i = 0; i < b.length() - len + 1; i++) {
			if (set.contains(b.substring(i, i+len))) return true;
		}
		return false;
	}
}
