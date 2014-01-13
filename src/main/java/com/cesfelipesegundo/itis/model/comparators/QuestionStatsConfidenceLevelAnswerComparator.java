package com.cesfelipesegundo.itis.model.comparators;

import java.util.Comparator;

import com.cesfelipesegundo.itis.model.QuestionStats;

public class QuestionStatsConfidenceLevelAnswerComparator implements Comparator{

	public int compare(Object arg0, Object arg1) {
		Integer int1 = ((QuestionStats) arg0).getConfidenceLevelAnswers();
		Integer int2 = ((QuestionStats) arg1).getConfidenceLevelAnswers();
		return int1.compareTo(int2);
	}

}
