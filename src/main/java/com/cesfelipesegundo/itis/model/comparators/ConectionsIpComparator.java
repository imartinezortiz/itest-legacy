package com.cesfelipesegundo.itis.model.comparators;

import java.util.Comparator;

import com.cesfelipesegundo.itis.model.Conection;

public class ConectionsIpComparator extends UTF8Adapter implements Comparator{

	public int compare(Object o1, Object o2) {
		String order1 = validate(((Conection)o1).getIp());
		String order2 = validate(((Conection)o2).getIp());
		return order1.compareTo(order2);
	}

}
