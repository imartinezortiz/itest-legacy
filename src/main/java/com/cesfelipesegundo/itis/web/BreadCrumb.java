package com.cesfelipesegundo.itis.web;

import java.lang.StringBuffer;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.StringTokenizer;
import java.util.Locale;

public class BreadCrumb {
	private StringBuffer text;
	
	private ResourceBundle re;
	
	protected static final Log logger = LogFactory.getLog(BreadCrumb.class);
		
	public BreadCrumb() {
		text = new StringBuffer();
		re = ResourceBundle.getBundle("com.cesfelipesegundo.itis.web.bundles.Mensajes");
	}
	
	public BreadCrumb(String languages) {
		text = new StringBuffer();
		if (languages == null)
			languages = "";
		StringTokenizer st = new StringTokenizer(languages,",");
		re = null;
		do {
			if (st.hasMoreTokens()){
				String lan = st.nextToken();
				Locale locale = getLocale(lan);
				try {
					re = ResourceBundle.getBundle("com.cesfelipesegundo.itis.web.bundles.Mensajes",locale);
				} catch (Exception e) {
					re = null;
				}
			}
			else {
				re = ResourceBundle.getBundle("com.cesfelipesegundo.itis.web.bundles.Mensajes");
			}
		} while (re == null);
	}
	
	private Locale getLocale(String languageString) {
		String depuredLanguageString="";
		// First we take only the substring before ';'
		StringTokenizer stDeleteEnd = new StringTokenizer(languageString,";");
		if (stDeleteEnd.hasMoreTokens())
			depuredLanguageString = stDeleteEnd.nextToken();
		// Now we split language, country and variant
		String language, country, variant;
		StringTokenizer stSplit = new StringTokenizer(depuredLanguageString,"-");
		if (!stSplit.hasMoreTokens())
			return new Locale("");
		language = stSplit.nextToken();
		if (!stSplit.hasMoreTokens())
			return new Locale(language);
		country = stSplit.nextToken();
		if (!stSplit.hasMoreTokens())
			return new Locale(language, country);
		variant = stSplit.nextToken();
		return new Locale(language, country.toUpperCase(), variant.toUpperCase());
	}
	
	public void addStep(String title, String link) {
		if (text.length()>0)
			text.append(" -> ");
		if (!link.equals("")) {
			text.append("<a href='");
			text.append(link);
			text.append("' />");
		}
		text.append(title);
		if (!link.equals("")) {
			text.append("</a>");
		}
	}
	
	public void addBundleStep(String title, String link) {
		try {
			addStep(re.getString(title),link);
		} catch (Exception e) {
			addStep("#"+title+"#",link);
		}
	}
	
	public void addBundleAndTextStep(String bundle, String text, String link) {
		try {
			addStep(re.getString(bundle)+text,link);
		} catch (Exception e) {
			addStep("#"+bundle+"#"+text,link);
		}
	}

	public String getText() {
		return text.toString();
	}
	
}
