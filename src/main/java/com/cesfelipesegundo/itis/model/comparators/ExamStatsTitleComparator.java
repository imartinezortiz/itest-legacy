package com.cesfelipesegundo.itis.model.comparators;

import java.util.Comparator;

import com.cesfelipesegundo.itis.model.ExamStats;

public class ExamStatsTitleComparator extends UTF8Adapter implements Comparator {

	public int compare(Object o1, Object o2) {
		String order1 = validate(((ExamStats)o1).getTitle());
		String order2 = validate(((ExamStats)o2).getTitle());
		return order1.compareTo(order2);
	}

}
