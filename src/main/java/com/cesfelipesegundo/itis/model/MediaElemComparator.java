package com.cesfelipesegundo.itis.model;

import java.util.Comparator;

/**
 * To implement the sorting of media elems using the collection methods
 * @author chema
 *
 */
public class MediaElemComparator implements Comparator {

	public int compare(Object o1, Object o2) {
		int order1 = ((MediaElem)o1).getOrder();
		int order2 = ((MediaElem)o2).getOrder();
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