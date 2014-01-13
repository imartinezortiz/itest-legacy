package com.cesfelipesegundo.itis.model.comparators;

import java.util.Comparator;

import com.cesfelipesegundo.itis.model.ExamStats;

public class ExamStatsGradeAverageComparator implements Comparator {

	public int compare(Object o1, Object o2) {
		double order1 = ((ExamStats)o1).getGradeAverage();
		double order2 = ((ExamStats)o2).getGradeAverage();
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
