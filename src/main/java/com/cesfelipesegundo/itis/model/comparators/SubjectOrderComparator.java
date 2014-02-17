package com.cesfelipesegundo.itis.model.comparators;

import java.util.Comparator;

import es.itest.engine.course.business.entity.Subject;

/**
 * Implements the sorting, based on order, of subject elems using the collection methods
 * @author chema
 *
 */
public class SubjectOrderComparator implements Comparator {

		public int compare(Object o1, Object o2) {
			int order1 = ((Subject)o1).getOrder();
			int order2 = ((Subject)o2).getOrder();
			if (order1 == order2) {
	    		return 0;
	    	} else {
	    		if (order1 < order2) {
	    			return -1;
	    		} else {
	    			return 1;    			
	    		}
	    	}
		}

	}
