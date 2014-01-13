package com.cesfelipesegundo.itis.model.comparators;

import java.util.Comparator;
import com.cesfelipesegundo.itis.model.Conection;



public class ConectionsUserNameComparator extends UTF8Adapter implements Comparator{

	public int compare(Object o1, Object o2) {
		String order1 = validate(((Conection)o1).getUser().getUserName());
		String order2 = validate(((Conection)o2).getUser().getUserName());
		return order1.compareTo(order2);
	}
}
