package com.cesfelipesegundo.itis.model.comparators;

import java.util.Comparator;

import com.cesfelipesegundo.itis.model.Course;
import com.cesfelipesegundo.itis.model.ExamStats;

public class CourseNumGroupsComparator implements Comparator{

	public int compare(Object o1, Object o2) {
		long order1 = ((Course)o1).getNumGroups();
		long order2 = ((Course)o2).getNumGroups();
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
