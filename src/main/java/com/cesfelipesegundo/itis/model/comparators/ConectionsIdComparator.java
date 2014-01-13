package com.cesfelipesegundo.itis.model.comparators;

import java.util.Comparator;

import com.cesfelipesegundo.itis.model.Conection;

public class ConectionsIdComparator implements Comparator{
	
	public int compare(Object o1, Object o2) {
		long order1 = ((Conection)o1).getId();
		long order2 = ((Conection)o2).getId();
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
