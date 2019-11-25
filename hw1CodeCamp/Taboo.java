
/*
 HW1 Taboo problem class.
 Taboo encapsulates some rules about what objects
 may not follow other objects.
 (See handout).
*/

import java.util.*;

public class Taboo<T> {
	HashMap<T, HashSet<T>> map = new HashMap<>();
	/**
	 * Constructs a new Taboo using the given rules (see handout.)
	 * @param rules rules for new Taboo
	 */
	public Taboo(List<T> rules) {
		if (rules.size() > 1) {
			for (int i = 0; i < rules.size()-1; i++) {
				if (rules.get(i) == null || rules.get(i+1) == null) continue;
				if (map.containsKey(rules.get(i))) {
					map.get(rules.get(i)).add(rules.get(i+1));
				} else {
					HashSet<T> set = new HashSet<T>();
					set.add(rules.get(i+1));
					map.put(rules.get(i), set);
				}
			}
		}
	}
	
	/**
	 * Returns the set of elements which should not follow
	 * the given element.
	 * @param elem
	 * @return elements which should not follow the given element
	 */
	public Set<T> noFollow(T elem) {
		 if (map.containsKey(elem)) {
			 return map.get(elem);
		 } else {
			 return Collections.emptySet();
		 }
	}
	
	/**
	 * Removes elements from the given list that
	 * violate the rules (see handout).
	 * @param list collection to reduce
	 */
	public void reduce(List<T> list) {
		int i = 0;
		while (i < list.size()-1) {
			if (!map.containsKey(list.get(i))) continue;
			while (i < list.size()-1 && 
					map.get(list.get(i)).contains(list.get(i+1)) ) {
				list.remove(i+1);
			}
			i++;
		}
	}
}
