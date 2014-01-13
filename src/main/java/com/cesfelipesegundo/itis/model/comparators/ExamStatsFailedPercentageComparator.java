package com.cesfelipesegundo.itis.model.comparators;

import java.util.Comparator;

import com.cesfelipesegundo.itis.model.ExamStats;

public class ExamStatsFailedPercentageComparator implements Comparator {

	public int compare(Object o1, Object o2) {
		double order1 = ( (double)((ExamStats)o1).getDoneNumber() - (double)((ExamStats)o1).getPassNumber() ) / (double)((ExamStats)o1).getDoneNumber();
		double order2 = ( (double)((ExamStats)o2).getDoneNumber() - (double)((ExamStats)o2).getPassNumber() ) / (double)((ExamStats)o2).getDoneNumber();
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
