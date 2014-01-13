package com.cesfelipesegundo.itis.model.comparators;

import java.util.Comparator;

import com.cesfelipesegundo.itis.model.QuestionStats;

public class QuestionStatsInsecurityEasinessComparator implements Comparator {

	public int compare(Object o1, Object o2) {
		if(((QuestionStats)o2).getConfidenceLevelAppeareances()==0&&((QuestionStats)o1).getConfidenceLevelAppeareances()==0)
			return 0;
		if(((QuestionStats)o2).getConfidenceLevelAppeareances()==0)
			return 1;
		if(((QuestionStats)o1).getConfidenceLevelAppeareances()==0)
			return -1;
		float order1 = (float)((QuestionStats)o1).getInsecurityEasiness()/(float)((QuestionStats)o1).getConfidenceLevelAppeareances();
		float order2 = (float)((QuestionStats)o2).getInsecurityEasiness()/(float)((QuestionStats)o2).getConfidenceLevelAppeareances();
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