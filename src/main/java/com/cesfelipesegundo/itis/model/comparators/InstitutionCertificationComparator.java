package com.cesfelipesegundo.itis.model.comparators;

import java.util.Comparator;

import com.cesfelipesegundo.itis.model.Institution;

public class InstitutionCertificationComparator extends UTF8Adapter implements Comparator {

	public int compare(Object o1, Object o2) {
		if(((Institution)o1).getCertification() == null && ((Institution)o2).getCertification() == null)
			return 0;
		if(((Institution)o1).getCertification()==null)
			return -1;
		if(((Institution)o2).getCertification()==null)
			return 1;
		
		String order1 = validate(((Institution)o1).getCertification());
		String order2 = validate(((Institution)o2).getCertification());
		
		return order1.compareTo(order2);
	}
}
