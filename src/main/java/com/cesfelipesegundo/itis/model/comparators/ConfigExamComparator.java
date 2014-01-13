package com.cesfelipesegundo.itis.model.comparators;

import java.util.Comparator;
import java.util.Date;
import com.cesfelipesegundo.itis.model.ConfigExam;

public class ConfigExamComparator extends UTF8Adapter implements Comparator{

	private String orderBy;
	
	public ConfigExamComparator(String order){
		orderBy = order;
	}
	
	public int compare(Object arg0, Object arg1) {
		if(orderBy.equalsIgnoreCase("sdate")){
			Date date1 = ((ConfigExam)arg0).getStartDate();
			Date date2 = ((ConfigExam)arg1).getStartDate();
			int visibility1 = ((ConfigExam)arg0).getVisibility();
			int visibility2 = ((ConfigExam)arg1).getVisibility();
			if(visibility1==visibility2){
				if(visibility1==1){
					return date1.compareTo(date2);
				}else{
					return 0;
				}
			}else{
				if(visibility1==1){
					return 1;
				}else{
					return -1;
				}
			}
		}
		if(orderBy.equalsIgnoreCase("revsdate")){
			Date date1 = ((ConfigExam)arg0).getStartDateRevision();
			Date date2 = ((ConfigExam)arg1).getStartDateRevision();
			boolean active1 = ((ConfigExam)arg0).isActiveReview();
			boolean active2 = ((ConfigExam)arg1).isActiveReview();
			if(active1==active2){
				if(active1==true){
					return date1.compareTo(date2);
				}else{
					return 0;
				}
			}else{
				if(active1==true){
					return 1;
				}else{
					return -1;
				}
			}
		}
		return 0;
	}

}
