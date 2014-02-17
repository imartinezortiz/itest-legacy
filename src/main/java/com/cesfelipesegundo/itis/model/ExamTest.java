package com.cesfelipesegundo.itis.model;

import es.itest.engine.test.business.entity.TestSession;

public class ExamTest {
	private TestSession exam;
	private User user;
	private long time;
	private boolean failed;
	
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public boolean isFailed() {
		return failed;
	}
	public void setFailed(boolean failed) {
		this.failed = failed;
	}
	public TestSession getExam() {
		return exam;
	}
	public void setExam(TestSession exam) {
		this.exam = exam;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	
}
