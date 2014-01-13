package com.cesfelipesegundo.itis.web.comparators;

import java.util.Comparator;

import com.cesfelipesegundo.itis.web.GroupWithTutors;

	/**
	 * Comparator class used to sort Lists of GroupWithTutors using the group's name
	 * @author gcanelada
	 *
	 */
	public class GroupWithTutorsCompareGroups implements Comparator<GroupWithTutors>{

		/**
		 * compare method, required by the interface Comparator<T> compares the group's names of 2 GroupWithTutors
		 * @param o1 
		 * @param o2
		 * @return 0 if they are equal, less than 0 if o1's group name is less than o2's one
		 *  or greater than 0 if 01's group name is greater than o2's
		 */
		public int compare(GroupWithTutors o1, GroupWithTutors o2) {
			try{
				String s1 =o1.getName().toLowerCase();
				String s2 =o2.getName().toLowerCase();
				return s1.compareTo(s2);
			}catch (Exception e){
				return 0;
			}
		}// compare
	}// GroupWithTutorsCompareGroups