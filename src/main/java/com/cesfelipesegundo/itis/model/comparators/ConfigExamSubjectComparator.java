package com.cesfelipesegundo.itis.model.comparators;

import java.util.Comparator;

import es.itest.engine.course.business.entity.TestSessionTemplateSubject;

public class ConfigExamSubjectComparator implements Comparator<TestSessionTemplateSubject> {

	public int compare (TestSessionTemplateSubject o1, TestSessionTemplateSubject o2) {
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
