package com.cesfelipesegundo.itis.model.comparators;

import java.util.Comparator;

import com.cesfelipesegundo.itis.model.User;

/**
 * Implements the sorting, based on surname, of user elems using the collection methods.
 * If Ids are equal, sorts by name
 * @author J.M. Colmenar
 *
 */
public class UserSurnameComparator extends UTF8Adapter implements Comparator {

	public int compare(Object o1, Object o2) {
		String order1 = validate(((User)o1).getSurname().toLowerCase());
		String order2 = validate(((User)o2).getSurname().toLowerCase());
		int surnameOrder = order1.compareTo(order2);
		if (surnameOrder != 0)
			return surnameOrder;
		// if strings are equal
		order1 = validate(((User)o1).getName().toLowerCase());
		order2 = validate(((User)o2).getName().toLowerCase());
		return order1.compareTo(order2);
	}

}
