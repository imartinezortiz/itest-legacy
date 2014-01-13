package com.cesfelipesegundo.itis.model.comparators;

import java.util.Comparator;

import com.cesfelipesegundo.itis.model.ExamGlobalInfo;

public class ExamGlobalInfoSubjectComparator extends UTF8Adapter implements Comparator {

	public int compare(Object o1, Object o2) {
		String order1 = validate(((ExamGlobalInfo)o1).getSubject());
		String order2 = validate(((ExamGlobalInfo)o2).getSubject());
		if(order1 == null && order2 == null)
			return 0;
		if(order1==null)
			return -1;
		if(order2==null)
			return 1;
		int surnameOrder = order1.compareTo(order2);
		return surnameOrder;
	}
}
