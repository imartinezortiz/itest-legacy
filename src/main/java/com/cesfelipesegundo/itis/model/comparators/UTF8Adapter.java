package com.cesfelipesegundo.itis.model.comparators;

public class UTF8Adapter {

	public String validate(String string){
		if(string == null)
			return "";
		string = string.replaceAll("á", "a");
		string = string.replaceAll("é", "e");
		string = string.replaceAll("í", "i");
		string = string.replaceAll("ó", "o");
		string = string.replaceAll("ú", "u");
		// las ñ's no se ordenan bien asique lo podemos cambiar por "nz" 
		string = string.replaceAll("ñ", "nz");
		return string;
	}
}
