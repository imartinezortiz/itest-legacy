package com.cesfelipesegundo.itis.model.comparators;

import java.util.Comparator;

import com.cesfelipesegundo.itis.model.QuestionStats;

public class QuestionStatsTextComparator extends UTF8Adapter implements Comparator {

	public int compare(Object o1, Object o2) {
		String order1 = validate(((QuestionStats)o1).getText().toLowerCase());
		String order2 = validate(((QuestionStats)o2).getText().toLowerCase());
		return order1.compareTo(order2);
	}

}
