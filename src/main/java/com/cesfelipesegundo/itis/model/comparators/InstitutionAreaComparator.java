package com.cesfelipesegundo.itis.model.comparators;

import java.util.Comparator;

import com.cesfelipesegundo.itis.model.Institution;

public class InstitutionAreaComparator extends UTF8Adapter implements Comparator {

	public int compare(Object o1, Object o2) {
		String order1 = validate(((Institution)o1).getState());
		String order2 = validate(((Institution)o2).getState());
		if(order1 == null && order2 == null)
			return 0;
		if(order1==null)
			return -1;
		if(order2==null)
			return 1;
		
		return order1.compareTo(order2);
	}
}