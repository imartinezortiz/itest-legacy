package com.cesfelipesegundo.itis.model.comparators;

import java.util.Comparator;

import com.cesfelipesegundo.itis.model.QuestionStats;

public class QuestionStatsUnansweredPercentageComparator implements Comparator {

	public int compare(Object o1, Object o2) {
		double order1 = ((double)((QuestionStats)o1).getAppeareances() - (double)((QuestionStats)o1).getAnswers()) / (double)((QuestionStats)o1).getAppeareances();
		double order2 = ((double)((QuestionStats)o2).getAppeareances() - (double)((QuestionStats)o2).getAnswers()) / (double)((QuestionStats)o2).getAppeareances();
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
