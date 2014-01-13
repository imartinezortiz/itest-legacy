package com.cesfelipesegundo.itis.model.comparators;

import java.util.Comparator;

import com.cesfelipesegundo.itis.model.LearnerStats;

public class LearnerStatsExamsNumberComparator implements Comparator {

	public int compare(Object o1, Object o2) {
		int order1 = ((LearnerStats)o1).getExamsNumber();
		int order2 = ((LearnerStats)o2).getExamsNumber();
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
