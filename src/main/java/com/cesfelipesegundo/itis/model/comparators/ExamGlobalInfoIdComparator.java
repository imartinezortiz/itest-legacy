package com.cesfelipesegundo.itis.model.comparators;

import java.util.Comparator;

import com.cesfelipesegundo.itis.model.ExamGlobalInfo;

public class ExamGlobalInfoIdComparator implements Comparator {

		public int compare(Object o1, Object o2) {
			Long order1 = ((ExamGlobalInfo)o1).getExamId();
			Long order2 = ((ExamGlobalInfo)o2).getExamId();
			return order1.compareTo(order2);
		}

}
