package com.cesfelipesegundo.itis.model.comparators;

import java.util.Comparator;

import com.cesfelipesegundo.itis.model.Group;

public class GroupYearComparator extends UTF8Adapter implements Comparator {

	public int compare(Object o1, Object o2) {
		String order1 = validate(((Group)o1).getYear());
		String order2 = validate(((Group)o2).getYear());
		return order1.compareTo(order2);
	}
}
