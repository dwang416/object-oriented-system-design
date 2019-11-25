import java.util.*;

public class Appearances {
	
	/**
	 * Returns the number of elements that appear the same number
	 * of times in both collections. Static method. (see handout).
	 * @return number of same-appearance elements
	 */
	public static <T> int sameCount(Collection<T> a, Collection<T> b) {
		HashMap<T, Integer> map = new HashMap<>();
		for (T t: a) {
			map.put(t, map.getOrDefault(t, 0) + 1);
		}
		for (T t: b) {
			map.put(t, map.getOrDefault(t, 0) - 1);
		}
		int res = 0;
		for (int count: map.values()) {
			if (count == 0) res++;
		}
		return res;
	}
	
}
