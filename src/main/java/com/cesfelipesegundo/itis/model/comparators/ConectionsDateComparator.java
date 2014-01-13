package com.cesfelipesegundo.itis.model.comparators;

import java.util.Comparator;
import java.util.Date;

import com.cesfelipesegundo.itis.model.Conection;

public class ConectionsDateComparator implements Comparator{

	public int compare(Object o1, Object o2) {
		Date order1 = ((Conection) o1).getDate();
		Date order2 = ((Conection) o2).getDate();
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
