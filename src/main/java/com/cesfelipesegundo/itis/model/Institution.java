package com.cesfelipesegundo.itis.model;

public class Institution {
	private Long id;
	private String code;
	private String name;
	private String address;
	private String city;
	private String zipCode;
	private String state;
	private InstitutionStudies studies;
	
	/**
	 * Phone number.
	 */
	private String phone;
	
	/**
	 * Fax number.
	 */
	private String fax;
	
	/**
	 * Electronic mail address.
	 */
	private String email;
	
	/**
	 * Web url.
	 */
	private String web;
	
	/**
	 * Full name of a contact person.
	 */
	private String contactPerson;
	
	/**
	 * 
	 * */
	private String certification;
	
	/**
	 * Phone number of the contact person.
	 */
	private String contactPhone;
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public String getZipCode() {
		return zipCode;
	}
	
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	public String getCertification() {
		return certification;
	}

	public void setCertification(String certification) {
		this.certification = certification;
	}

	public boolean equals(Object o ){
		boolean result = o instanceof Institution;
		
		if(result){
			Institution i = (Institution)o;
			result = i.id.equals(id);
		}
		
		return result;
	}
	
	public int hashCode(){
		int result = 345;
		
		if(id != null) {
			result += 23*id.hashCode();
		}
		
		if(code != null ){
			result += 32*code.hashCode();
		}
		
		if(name != null ){
			result += 81*name.hashCode();
		}
		
		if(address != null ){
			result += 76*address.hashCode();
		}
		
		if(city != null ){
			result += 47*city.hashCode();
		}
		
		if(zipCode != null ){
			result += 83*zipCode.hashCode();
		}
		
		if(state != null ){
			result += 49*state.hashCode();
		}
		
		return result;
	}
	
	public String toString(){
		return "Institution: "+name+" Code: "+code;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWeb() {
		return web;
	}

	public void setWeb(String web) {
		this.web = web;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public InstitutionStudies getStudies() {
		return studies;
	}

	public void setStudies(InstitutionStudies studies) {
		this.studies = studies;
	}
	
}
