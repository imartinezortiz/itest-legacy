package com.cesfelipesegundo.itis.model.comparators;

import java.util.Comparator;

import com.cesfelipesegundo.itis.model.AnswerStats;

public class AnswerStatsComparator implements Comparator {

	private String orderBy;
	
	public AnswerStatsComparator(String order){
		this.orderBy = order;
	}
	public int compare(Object arg0, Object arg1) {
		AnswerStats a0 = (AnswerStats)arg0;
		AnswerStats a1 = (AnswerStats)arg1;
		if(orderBy.equalsIgnoreCase("idQuestion")){
			return a0.getIdQuestion().compareTo(a1.getIdQuestion());
		}
		if(orderBy.equalsIgnoreCase("markedNumber")){
			Integer marked0 = a0.getMarkedNumber();
			Integer marked1 = a1.getMarkedNumber();
			return marked0.compareTo(marked1);
		}
		if(orderBy.equalsIgnoreCase("solution")){
			Integer solution0 = a0.getSolution();
			Integer solution1 = a1.getSolution();
			return solution0.compareTo(solution1);
		}
		return 0;
	}

}
