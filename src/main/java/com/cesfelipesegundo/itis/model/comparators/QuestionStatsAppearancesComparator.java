package com.cesfelipesegundo.itis.model.comparators;

import java.util.Comparator;

import com.cesfelipesegundo.itis.model.QuestionStats;

public class QuestionStatsAppearancesComparator implements Comparator {

	public int compare(Object o1, Object o2) {
		int order1 = ((QuestionStats)o1).getAppeareances();
		int order2 = ((QuestionStats)o2).getAppeareances();
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
