package com.cesfelipesegundo.itis.model.comparators;

import java.util.Comparator;
import java.util.Date;

import com.cesfelipesegundo.itis.model.ExamGlobalInfo;

public class ExamGlobalInfoStartDateComparator implements Comparator {

	public int compare(Object o1, Object o2) {
		Date order1 = ((ExamGlobalInfo)o1).getStartDate();
		Date order2 = ((ExamGlobalInfo)o2).getStartDate();
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
