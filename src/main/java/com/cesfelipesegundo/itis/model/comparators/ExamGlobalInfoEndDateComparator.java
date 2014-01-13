package com.cesfelipesegundo.itis.model.comparators;

import java.util.Comparator;
import java.util.Date;

import com.cesfelipesegundo.itis.model.ExamGlobalInfo;

public class ExamGlobalInfoEndDateComparator implements Comparator {

	public int compare(Object o1, Object o2) {
		Date order1 = ((ExamGlobalInfo)o1).getEndDate();
		Date order2 = ((ExamGlobalInfo)o2).getEndDate();
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
