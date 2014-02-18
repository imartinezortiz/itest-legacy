package com.cesfelipesegundo.itis.model.comparators;

import java.util.Comparator;

import es.itest.engine.course.business.entity.Group;

public class GroupCourseComparator extends UTF8Adapter implements Comparator {

	public int compare(Object o1, Object o2) {
		String order1 = validate(((Group)o1).getCourse().getName());
		String order2 = validate(((Group)o2).getCourse().getName());
		return order1.compareTo(order2);
	}
}
