package com.cesfelipesegundo.itis.test_DEPRECATED;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import com.cesfelipesegundo.itis.dao.TemplateGradeDAOImpl;
import com.cesfelipesegundo.itis.dao.api.TemplateGradeDAO;
import com.cesfelipesegundo.itis.model.QueryGrade;
import com.cesfelipesegundo.itis.model.TemplateGrade;

public class TemplateGradeDAOTest extends AbstractDependencyInjectionSpringContextTests {
	
	protected String[] getConfigLocations() {
		return new String [] {"classpath:com/cesfelipesegundo/itis/dao/config/spring-dao.xml"};    
	}
	
	public void testFind(){
		System.out.println("testFind:");
        TemplateGradeDAO templateGradeDAO = (TemplateGradeDAOImpl)this.applicationContext.getBean("templateGradeDAO");
        // Construyo el query
        QueryGrade query = new QueryGrade();
        query.setOrder(QueryGrade.OrderBy.TIME);
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        	Date begin = formatter.parse("2007-02-27 11:00");
        	Date end = formatter.parse("2007-02-27 18:00");
        	query.setBegin(begin);
        	query.setEnd(end);
        }
        catch(Exception ee) {
        	ee.printStackTrace();
        }
        //query.setGrade(Double.valueOf(100));
        List<TemplateGrade> grades = templateGradeDAO.find(query);
        for(TemplateGrade grade : grades) {
        	System.out.println("Id: " + grade.getId());
        	System.out.println("Ip: " + grade.getIp());
        	System.out.println("Begin: " + grade.getBegin());
        	System.out.println("End: " + grade.getEnd());
        	System.out.println("Grade: " + grade.getGrade());
        	System.out.println("Id: " + grade.getTime());
        	// Print ConfigExam
        	System.out.println("IdExam: " + grade.getExam().getId());
        	System.out.println("TitleExam: " + grade.getExam().getTitle());
        	System.out.println("GroupExam: " + grade.getExam().getGroup().getId());
        	// Print Learner
        	System.out.println("IdLearner: " + grade.getLearner().getId());
        	System.out.println("SurnameLearner: " + grade.getLearner().getSurname());
        	System.out.println("=====================================================");
        }
        
        // Pruebo con un id de examen: 1
        System.out.println("Fin de test..." );
	  }
}
