package com.cesfelipesegundo.itis.model.comparators;

import java.util.Comparator;

import com.cesfelipesegundo.itis.model.ExamGlobalInfo;


public class ExamGlobalInfoCenterComparator implements Comparator {

	public int compare(Object o1, Object o2) {
		String order1 = ((ExamGlobalInfo)o1).getCenter();
		String order2 = ((ExamGlobalInfo)o2).getCenter();
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
