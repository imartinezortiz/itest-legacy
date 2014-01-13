package com.cesfelipesegundo.itis.model.comparators;

import java.util.Comparator;

import com.cesfelipesegundo.itis.model.ExamGlobalInfo;



public class ExamGlobalInfoTitleComparator extends UTF8Adapter implements Comparator {

	public int compare(Object o1, Object o2) {
		String order1 = validate(((ExamGlobalInfo)o1).getExamTitle());
		String order2 = validate(((ExamGlobalInfo)o2).getExamTitle());
		return order1.compareTo(order2);
	}

}
