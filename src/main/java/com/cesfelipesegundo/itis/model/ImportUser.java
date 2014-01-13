package com.cesfelipesegundo.itis.model;

public class ImportUser extends User{

	private boolean isInDB;
	private boolean imported = false;
	private boolean repeated = false;
	
	
	public String getXML(){
		String xml = "<user>";
		xml+="<persId>"+getPersId()+"</persId>";
		xml+="<name>"+getName()+"</name>";
		xml+="<surname>"+getSurname()+"</surname>";
		xml+="<userName>"+getUserName()+"</userName>";
		String email;
		if(getEmail()!=null)
			email = getEmail();
		else
			email = "";
		xml+="<email>"+email+"</email>";
		xml+="<isInDB>"+isInDB+"</isInDB>";
		xml+="<imported>"+imported+"</imported>";
		xml+="<repeated>"+repeated+"</repeated>";
		xml+="</user>";
		return xml;
	}


	public boolean isInDB() {
		return isInDB;
	}


	public void setInDB(boolean isInDB) {
		this.isInDB = isInDB;
	}
	
	public boolean isImportable(){
		boolean correctName = false;
		if(getName()!=null && !getName().trim().equalsIgnoreCase("") && getName().length()<20)
			correctName = true;
		
		boolean correctSurname = false;
		if(getSurname()!=null && !getSurname().trim().equalsIgnoreCase("") && getSurname().length()<50)
			correctSurname = true;
		
		boolean correctPersId = false;
		if(getPersId()!=null && !getPersId().trim().equalsIgnoreCase("") && getPersId().length()<10)
			correctPersId = true;
		
		boolean correctUserName = false;
		if(getUserName()!=null && !getUserName().trim().equalsIgnoreCase("") && getUserName().length()<20)
			correctUserName = true;
		
		boolean correctEmail = false;
		if((getEmail()!=null && !getEmail().trim().equalsIgnoreCase("") && getEmail().length()<40) || getEmail()==null || (getEmail()!=null && getEmail().trim().equalsIgnoreCase("")))
			correctEmail = true;
		if(getPasswd()==null)
			setPasswd(getUserName());
		
		boolean correctPass = false;
		if(getPasswd()!=null && !getPasswd().trim().equalsIgnoreCase("") && getPasswd().length()<20)
			correctPass = true;
		imported = correctEmail && correctName && correctPersId && correctSurname && correctUserName && correctPass;
		return correctEmail && correctName && correctPersId && correctSurname && correctUserName && correctPass;
	}


	public boolean isImported() {
		return imported;
	}


	public void setImported(boolean imported) {
		this.imported = imported;
	}


	public boolean isRepeated() {
		return repeated;
	}


	public void setRepeated(boolean repeated) {
		this.repeated = repeated;
	}
	
	
}
