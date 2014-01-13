package com.cesfelipesegundo.itis.model.comparators;

import java.util.Comparator;

import com.cesfelipesegundo.itis.model.ConfigExamSubject;

public class ConfigExamSubjectComparator implements Comparator<ConfigExamSubject> {

	public int compare (ConfigExamSubject o1, ConfigExamSubject o2) {
		int order1 = o1.getSubject().getOrder();
		int order2 = o2.getSubject().getOrder();
		if (order1 == order2) {
			int dif1 = o1.getMaxDifficulty();
			int dif2 = o2.getMaxDifficulty();
			if (dif1 == dif2)
				return 0;
			else if (dif1 < dif2)
				return -1;
			else return 1;
    	} else {
    		if (order1 < order2) {
    			return -1;
    		} else {
    			return 1;    			
    		}
    	}
	}

}
