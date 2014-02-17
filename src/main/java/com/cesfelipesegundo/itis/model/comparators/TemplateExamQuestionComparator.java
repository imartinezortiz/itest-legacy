package com.cesfelipesegundo.itis.model.comparators;

import java.util.Comparator;

import es.itest.engine.test.business.entity.Item;

public class TemplateExamQuestionComparator extends UTF8Adapter implements Comparator{

	private String orderBy;
	
	public TemplateExamQuestionComparator(String order){
		orderBy = order;
	}
	
	public int compare(Object arg0, Object arg1) {
		Item t1 = (Item) arg0;
		Item t2 = (Item) arg1;
		
		if(orderBy.equalsIgnoreCase("id")){
			if(t1.getId()==t2.getId()){
				return 0;
			}else{
				if(t1.getId()>t2.getId()){
					return 1;
				}else{
					return -1;
				}
			}
		}
		if(orderBy.equalsIgnoreCase("text")){
			String text1 = validate(t1.getTitle());
			String text2 = validate(t2.getTitle());
			if(text1.trim().equalsIgnoreCase("")){
				text1 = validate(t1.getText());
			}
			if(text2.trim().equalsIgnoreCase("")){
				text2 = validate(t2.getText());
			}
			return text1.compareTo(text2);
		}
		if(orderBy.equalsIgnoreCase("subject")){
			String subject = validate(t1.getSubject().getSubject());
			String subject2 = validate(t2.getSubject().getSubject());
			return subject.compareTo(subject2);
		}
		if(orderBy.equalsIgnoreCase("diff")){
			if(t1.getDifficulty()==t2.getDifficulty()){
				return 0;
			}else{
				if(t1.getDifficulty()>t2.getDifficulty()){
					return 1;
				}else{
					return -1;
				}
			}
		}
		if(orderBy.equalsIgnoreCase("scope")){
			Integer v1 = t1.getVisibility();
			Integer v2 = t2.getVisibility();
			return v1.compareTo(v2);
		}
		if(orderBy.equalsIgnoreCase("resp")){
			Integer answers1 = t1.getAnswers().size();
			Integer answers2 = t2.getAnswers().size();
			return answers1.compareTo(answers2);
		}
		if(orderBy.equalsIgnoreCase("type")){
			Integer type1 = t1.getType();
			Integer type2 = t2.getType();
			return type1.compareTo(type2);
		}
		return 0;
	}

}
