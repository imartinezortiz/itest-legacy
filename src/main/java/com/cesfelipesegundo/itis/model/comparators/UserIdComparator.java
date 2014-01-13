package com.cesfelipesegundo.itis.model.comparators;

import java.util.Comparator;

import com.cesfelipesegundo.itis.model.User;

public class UserIdComparator implements Comparator {

	public int compare(Object o1, Object o2) {
		Long order1 = ((User)o1).getId();
		Long order2 = ((User)o2).getId();
		int surnameOrder = order1.compareTo(order2);
		if (surnameOrder != 0)
			return surnameOrder;
		// if strings are equal
		String order3 = ((User)o1).getSurname();
		String order4 = ((User)o2).getSurname();
		return order3.compareTo(order4);
	}
	
}
