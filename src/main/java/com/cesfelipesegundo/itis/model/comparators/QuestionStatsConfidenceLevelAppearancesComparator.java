package com.cesfelipesegundo.itis.model.comparators;

import java.util.Comparator;

import com.cesfelipesegundo.itis.model.QuestionStats;

public class QuestionStatsConfidenceLevelAppearancesComparator implements Comparator {

	public int compare(Object o1, Object o2) {
		int order1 = ((QuestionStats)o1).getConfidenceLevelAppeareances();
		int order2 = ((QuestionStats)o2).getConfidenceLevelAppeareances();
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
