package com.cesfelipesegundo.itis.model.comparators;

import java.util.Comparator;

import com.cesfelipesegundo.itis.model.ExamStats;

public class ExamStatsCustomized implements Comparator {

	public int compare(Object o1, Object o2) {
		boolean order1 = ((ExamStats)o1).isCustomized();
		boolean order2 = ((ExamStats)o2).isCustomized();
		if (order1 == order2) {
    		return 0;
    	} else if(order1 == true && order2 == false){
    		return 1;
    	} else return -1;
	}

}