package com.cesfelipesegundo.itis.model.comparators;

import java.util.Comparator;

import com.cesfelipesegundo.itis.model.LearnerStats;

public class LearnerStatsFullNameComparator extends UTF8Adapter implements Comparator {

	public int compare(Object o1, Object o2) {
		String order1 = validate(((LearnerStats)o1).getSurname());
		String order2 = validate(((LearnerStats)o2).getSurname());
		int surnameOrder = order1.compareTo(order2);
		if (surnameOrder != 0)
			return surnameOrder;
		// if surnames are equal
		order1 = validate(((LearnerStats)o1).getName());
		order2 = validate(((LearnerStats)o2).getName());
		return order1.compareTo(order2);
	}

}
