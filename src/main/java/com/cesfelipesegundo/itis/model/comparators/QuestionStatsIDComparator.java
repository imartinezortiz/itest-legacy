package com.cesfelipesegundo.itis.model.comparators;

import java.util.Comparator;

import com.cesfelipesegundo.itis.model.QuestionStats;

public class QuestionStatsIDComparator implements Comparator {

	public int compare(Object o1, Object o2) {
		long order1 = ((QuestionStats)o1).getId().longValue();
		long order2 = ((QuestionStats)o2).getId().longValue();
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
