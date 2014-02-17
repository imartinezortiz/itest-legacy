package com.cesfelipesegundo.itis.model.comparators;

import java.util.Comparator;

import es.itest.engine.course.business.entity.Group;

	/**
	 * Comparator class used to sort Lists of Group using the institution's name
	 * @author gcanelada
	 *
	 */
	public class GroupInstitutionNameComparator extends UTF8Adapter implements Comparator<Group>{

		/**
		 * compare method, required by the interface Comparator<T> compares the institution's names of 2 Groups
		 * @param o1 
		 * @param o2
		 * @return 0 if they are equal, less than 0 if o1's group name is less than o2's one
		 *  or greater than 0 if 01's group name is greater than o2's
		 */
		public int compare(Group o1, Group o2) {
			try{
				String s1 = validate(o1.getName().toLowerCase());
				String s2 = validate(o2.getName().toLowerCase());
				return s1.compareTo(s2);
			}catch (Exception e){
				return 0;
			}
		}// compare
	}// GroupWithTutorsCompareGroups