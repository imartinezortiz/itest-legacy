package com.cesfelipesegundo.itis.model.comparators;

import java.util.Comparator;

import com.cesfelipesegundo.itis.model.User;

public class UserEmailComparator extends UTF8Adapter implements Comparator {

	public int compare(Object o1, Object o2) {
		String order1 = validate(((User)o1).getEmail());
		String order2 = validate(((User)o2).getEmail());
		if(order1 == null && order2 == null)
			return 0;
		if(order1==null)
			return -1;
		if(order2==null)
			return 1;
		int surnameOrder = order1.compareTo(order2);
		if (surnameOrder != 0)
			return surnameOrder;
		// if strings are equal
		order1 = validate(((User)o1).getSurname());
		order2 = validate(((User)o2).getSurname());
		return order1.compareTo(order2);
	}

}