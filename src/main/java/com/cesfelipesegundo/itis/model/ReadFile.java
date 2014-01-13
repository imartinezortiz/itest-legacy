package com.cesfelipesegundo.itis.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * 
 * 
 * */
public class ReadFile {

	/**
	 * Gets a CSV file and return a list of students from file
	 * @param f The CSV file
	 * @param log The log class
	 * @return a list of ImportUser from file
	 * */
	public static List<ImportUser> readFileCSVUserList(File f, Log log){
		List<ImportUser> users = new ArrayList<ImportUser>();
		StringBuilder contents = new StringBuilder();
		try {
		      //use buffering, reading one line at a time
		      //FileReader always assumes default encoding is OK!
		      BufferedReader input =  new BufferedReader(new FileReader(f));
		      try {
		    	  ImportUser user = null;
		    	  int cont = 1;
		    	  String line = null; //not declared within while loop
		    	  	/*
					* readLine is a bit quirky :
					* it returns the content of a line MINUS the newline.
					* it returns null only for the END of the stream.
					* it returns an empty String if two newlines appear in a row.
					*/
		        while (( line = input.readLine()) != null){
			        String[] userFields = line.split(";");
			        if(userFields.length >= 5){
			        	user = new ImportUser();
			        	user.setPersId(userFields[0]);
			        	user.setName(userFields[1]);
			        	user.setSurname(userFields[2]);
			        	user.setEmail(userFields[3]);
			        	user.setUserName(userFields[4]);
			        	if(userFields.length==5){
			        		user.setPasswd(userFields[4]);
			        	}else{
			        		user.setPasswd(userFields[5]);
			        	}
			        	user.setRepeated(false);
			        	for(ImportUser u : users){
			        		if(u.getUserName().equalsIgnoreCase(user.getUserName())){
			        			user.setRepeated(true);
			        			break;
			        		}
			        	}
			        	/*
						 * Este metodo inicializa la variable de ImportUser que utilizamos para saber si se va a importar
						 * o no el usuario
						 * */
						user.isImportable();
						
			        	users.add(user);
			        }else{
			        	log.error("Usuario recibido no válido en la linea "+cont+" del fichero");
			        }
			        cont++;
		        }
		      }
		      finally {
		    	  input.close();
		      }
		    }
		    catch (IOException ex){
		    	ex.printStackTrace();
		    }
		    
		    return users;
	}
	
	/**
	 * Gets a XLS file and return a list of students from file
	 * @param f The XLS file
	 * @param log The log class
	 * @return a list of ImportUser from file
	 * */
	public static List<ImportUser> readFileXLSUserList(File f, Log log){
		List<ImportUser> users = new ArrayList<ImportUser>();
		HSSFWorkbook workBook = null;
		ImportUser user = null;
		int cont = 0;
		/**
		* Create a new instance for cellDataList
		*/
		try {
			FileInputStream fileInputStream = new FileInputStream(f);
			POIFSFileSystem fsFileSystem = new POIFSFileSystem(fileInputStream);
			workBook = new HSSFWorkbook(fsFileSystem);
		} catch (FileNotFoundException e) {
			log.error("Archivo xls no encontrado");
			e.printStackTrace();
		} catch (IOException e) {
			log.error("Error al abrir fichero xls");
		}
		if(workBook!=null){
			HSSFSheet hssfSheet = workBook.getSheetAt(0);
			/**
			* Iterate the rows and cells of the spreadsheet
			* to get all the datas.
			*/
			Iterator rowIterator = hssfSheet.rowIterator();
			while (rowIterator.hasNext()){
				boolean breaked = false;
				HSSFRow hssfRow = (HSSFRow) rowIterator.next();
				Iterator iterator = hssfRow.cellIterator();
				user = new ImportUser();
				cont = 0;
				while (iterator.hasNext()){
					HSSFCell hssfCell = (HSSFCell) iterator.next();
					String cellValue = hssfCell.toString();
					int num = hssfCell.getCellNum();
					if(num<0 || num > 5){
						user.setImported(false);
						log.error("xls mal formado, no coincide con el formato de la plantilla");
						breaked = true;
						break;
					}
					switch (cont){
						case 0: setUserPersId(cellValue,user);
							break;
						case 1: user.setName(cellValue);
							break;
						case 2: user.setSurname(cellValue);
							break;
						case 3: user.setEmail(cellValue);
							break;
						case 4: user.setUserName(cellValue);
							break;
						case 5: setUserPassword(cellValue,user);
							break;
						default:break;
					}
					cont++;
				}
				/*
				 * Comprobamos que no haya usuarios repetidos
				 * */
				user.setRepeated(false);
	        	for(ImportUser u : users){
	        		if(u.getUserName().equalsIgnoreCase(user.getUserName())){
	        			user.setRepeated(true);
	        			break;
	        		}
	        	}
	        	
	        	//Si no hay contraseña se pone por defecto el userName como contraseña
	        	if(user.getPasswd()==null || (user.getPasswd()!=null && user.getPasswd().trim().equalsIgnoreCase("")))
	        		user.setPasswd(user.getUserName());
	        	
	        	/*
				 * Este metodo inicializa la variable de ImportUser que utilizamos para saber si se va a importar
				 * o no el usuario
				 * */
	        	
	        	user.isImportable();
				if(!breaked)
					users.add(user);
			}
		}
		return users;
	}

	private static void setUserPassword(String cellValue, ImportUser user) {
		if(cellValue!=null && !cellValue.trim().equalsIgnoreCase("")){
			user.setPasswd(cellValue);
		}else{
			user.setPasswd(user.getUserName());
		}
	}

	private static void setUserPersId(String cellValue, ImportUser user) {
		/*
		 * Lo hago así para que en persId no salgan decimales (aveces si el dni no tiene letra salen decimales)
		 * */
		try{
			double dniD = Double.parseDouble(cellValue);
			Integer dni = (int)dniD;
			if(user!=null)
				user.setPersId(dni.toString());
		}catch(Exception e){
			user.setPersId(cellValue);
		}
	}
}
