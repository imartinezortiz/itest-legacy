package com.cesfelipesegundo.itis.model.comparators;

import es.itest.engine.test.business.entity.TestSessionGrade;
import es.itest.engine.test.business.entity.TestSessionTemplate;

import java.util.Comparator;

public class TemplateGradeComparator extends UTF8Adapter implements Comparator<TestSessionGrade>{

	private String orderBy;
	
	public TemplateGradeComparator(String order){
		orderBy = order;
	}
	
	public int compare(TestSessionGrade arg0, TestSessionGrade arg1) {
		if(orderBy.equalsIgnoreCase("student")){
			return orderByStudent(arg0,arg1);
		}
		if(orderBy.equalsIgnoreCase("exam")){
			return orderByExam(arg0.getExam(),arg1.getExam());
		}
		if(orderBy.equalsIgnoreCase("grade")){
			return orderByGrade(arg0,arg1);
		}
		if(orderBy.equalsIgnoreCase("startDate")){
			return orderByStartDate(arg0,arg1);
		}
		if(orderBy.equalsIgnoreCase("endDate")){
			return orderByEndDate(arg0,arg1);
		}
		if(orderBy.equalsIgnoreCase("duration")){
			return orderByDuration(arg0,arg1);
		}
		if(orderBy.equalsIgnoreCase("maxGrade")){
			return orderByMaxGrade(arg0,arg1);
		}
		if(orderBy.equalsIgnoreCase("ip")){
			return orderByIp(arg0,arg1);
		}
		return 0;
	}

	private int orderByIp(TestSessionGrade arg0, TestSessionGrade arg1) {
		String ip1 = arg0.getIp();
		String ip2 = arg0.getIp();
		return ip1.compareTo(ip2);
	}

	private int orderByDuration(TestSessionGrade arg0, TestSessionGrade arg1) {
		long date0 = arg0.getEnd().getTime();
		long date1 = arg1.getEnd().getTime();
		if(date0==date1 && date0==0){
			return 0;
		}else{
			if(date0==0)
				return -1;
			else if(date1==0)
				return 1;
			else
				return arg0.getDuration().compareTo(arg1.getDuration());
		}
	}

	private int orderByMaxGrade(TestSessionGrade arg0, TestSessionGrade arg1) {
		Double maxGrade0 = arg0.getExam().getMaxGrade();
		Double maxGrade1 = arg1.getExam().getMaxGrade();
		return maxGrade0.compareTo(maxGrade1);
	}

	private int orderByEndDate(TestSessionGrade arg0, TestSessionGrade arg1) {
		return arg0.getEnd().compareTo(arg1.getEnd());
	}

	private int orderByStartDate(TestSessionGrade arg0, TestSessionGrade arg1) {
		return arg0.getBegin().compareTo(arg1.getBegin());
	}

	private int orderByGrade(TestSessionGrade arg0, TestSessionGrade arg1) {
		return arg0.getGrade().compareTo(arg1.getGrade());
	}

	private int orderByExam(TestSessionTemplate exam, TestSessionTemplate exam2) {
		String name1 = exam.getTitle();
		String name2 = exam2.getTitle();
		return name1.compareTo(name2);
	}

	private int orderByStudent(TestSessionGrade arg0, TestSessionGrade arg1) {
		String userName1 = arg0.getLearner().getSurname()+arg0.getLearner().getName();
		String userName2 = arg1.getLearner().getSurname()+arg1.getLearner().getName();
		int result = userName1.compareTo(userName2);
		// Si tienen el mismo nombre y apellidos comparamos en funcion de su dni
		if(result==0){
			return arg0.getLearner().getPersId().compareTo(arg1.getLearner().getPersId());
		}
		return result;
	}

}
