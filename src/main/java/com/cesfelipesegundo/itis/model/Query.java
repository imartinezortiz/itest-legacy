package com.cesfelipesegundo.itis.model;

public class Query {

	public enum OrderBy { ID, SUBJECT, TITLE, TEXT, TITLE_XOR_TEXT, DIFFICULTY, SCOPE, GROUP	};

	/**
	 * Id of the question.
	 * <p>If <code>null</code> do not consider in the query.</p>
	 */
	private Long id;
	
	/**
	 * Title of the question.
	 * <p>If <code>null</code> the title is not included in the query.
	 */
	private String title;
	
	/**
	 * Prompt of the question.
	 * <p>If <code>null</code> do not consider in the query.</p>
	 */
	private String text;
	
	/**
	 * Subject of the question.
	 * <p>If <code>null</code> do not consider in the query.</p>
	 */
	private Long subject;
	
	/**
	 * Associated question group
	 * <p>If <code>null</code> do not consider in the query.</p> 
	 */
	private Long group;

	/**
	 * Question difficulty.
	 * <p>If <code>null</code> do not consider in the query.</p>
	 */
	private Short difficulty;
	
	/**
	 * Flag that point out if the question is active.
	 * <p>If <code>null</code> do not consider in the query.</p>
	 */
	private Boolean active;
	
	/**
	 * Institution
	  * <p>If <code>null</code> do not consider in the query.</p>
	 */
	private Long institution;
	
	/**
	 * text of theme
	 * <p>If <code>null</code> do not consider in the query.</p>
	 */
	private String textTheme;
	
	/**
	 * Row position of the first result to be shown.
	 */
	private int firstResult;
	
	/**
	 * Max number of rows to be shown, starting at @see #firstResult 
	 */
	private Integer maxResultCount;
	
	/**
	 * Results order
	 */
	private OrderBy order;
	
	private Short scope;

	/**
	 * Exclude the questions in this group
	 * <p>If <code>null</code> do not consider in the query.</p> 
	 */
	private Long excludeGroup;
	
	/**
	 * Gets the questions of the groups teached by the user. Just to import questions
	 * <p>If <code>null</code> do not consider in the query.</p> 
	 */
	private Long userId;
	
	
	/**
	 * Gets the same type questions
	 * <p>If <code>null</code> do not consider in the query.</p> 
	 */
	private Integer questionType;
	
	public Query(){
		order = OrderBy.SUBJECT;
		firstResult = 0;
		maxResultCount = null;
	}
	
	
	public Integer getQuestionType() {
		return questionType;
	}


	public void setQuestionType(Integer questionType) {
		this.questionType = questionType;
	}


	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	
	public Long getInstitution() {
		return institution;
	}

	public void setInstitution(Long institution) {
		this.institution = institution;
	}
	
	public String getTextTheme() {
		return textTheme;
	}

	public void setTextTheme(String textTheme) {
		this.textTheme = textTheme;
	}

	public Short getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Short difficulty) {
		this.difficulty = difficulty;
	}

	public Long getGroup() {
		return group;
	}

	public void setGroup(Long group) {
		this.group = group;
	}

	public Long getSubject() {
		return subject;
	}

	public void setSubject(Long subject) {
		this.subject = subject;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getFirstResult() {
		return firstResult;
	}

	public void setFirstResult(int firstResult) {
		this.firstResult = firstResult;
	}

	public Integer getMaxResultCount() {
		return maxResultCount;
	}

	public void setMaxResultCount(Integer maxResultCount) {
		this.maxResultCount = maxResultCount;
	}

	public OrderBy getOrder() {
		return order;
	}

	public void setOrder(OrderBy order) {
		this.order = order;
	}

	public Short getScope() {
		return scope;
	}

	public void setScope(Short scope) {
		this.scope = scope;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getExcludeGroup() {
		return excludeGroup;
	}

	public void setExcludeGroup(Long excludeGroup) {
		this.excludeGroup = excludeGroup;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
		
}
