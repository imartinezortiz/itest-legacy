package com.cesfelipesegundo.itis.model.comparators;

import java.util.Comparator;

import com.cesfelipesegundo.itis.model.QuestionStats;

public class QuestionStatsSubjectComparator extends UTF8Adapter implements Comparator {

	public int compare(Object o1, Object o2) {
		String order1 = validate(((QuestionStats)o1).getSubject());
		String order2 = validate(((QuestionStats)o2).getSubject());
		return order1.compareTo(order2);
	}

}
