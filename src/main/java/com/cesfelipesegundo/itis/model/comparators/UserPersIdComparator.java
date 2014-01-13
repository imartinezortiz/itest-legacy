package com.cesfelipesegundo.itis.model.comparators;

import java.util.Comparator;

import com.cesfelipesegundo.itis.model.User;

/**
 * Implements the sorting, based on persId, of user elems using the collection methods.
 * If Ids are equal, sorts by surname
 * @author J.M. Colmenar
 *
 */
public class UserPersIdComparator extends UTF8Adapter implements Comparator {

	public int compare(Object o1, Object o2) {
		String order1 = validate(((User)o1).getPersId().toLowerCase());
		String order2 = validate(((User)o2).getPersId().toLowerCase());
		int surnameOrder = order1.compareTo(order2);
		if (surnameOrder != 0)
			return surnameOrder;
		// if strings are equal
		order1 = validate(((User)o1).getSurname().toLowerCase());
		order2 = validate(((User)o2).getSurname().toLowerCase());
		return order1.compareTo(order2);
	}
	
}
