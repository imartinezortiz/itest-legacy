package com.cesfelipesegundo.itis.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.cesfelipesegundo.itis.dao.api.RetrievePasswordDAO;
import com.cesfelipesegundo.itis.model.User;

public class RetrievePasswordDAOImpl extends SqlMapClientDaoSupport implements RetrievePasswordDAO{

	public RetrievePasswordDAOImpl(){
		super();
	}

	public boolean checkTokenRetrievePassord(String token) {
		String result = (String) super.getSqlMapClientTemplate().queryForObject("RetrievePassword.checkToken", token);
		if(result == null)
			return false;
		if(result.isEmpty())
			return false;
		return true;
	}

	public void insertToken(Long id, String token, Date fechaInsert,
			Date fechaCaducidad) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("idusu", id);
		map.put("token", token);
		map.put("fechaInsert", fechaInsert);
		map.put("fechaCaducidad", fechaCaducidad);
		super.getSqlMapClientTemplate().insert("RetrievePassword.insertToken",map);
	}

	public long getTokenUserId(String token) {
		try{
			long id = (Long) super.getSqlMapClientTemplate().queryForObject("RetrievePassword.getTokenUserId",token);
			return id;
		}catch(java.lang.NullPointerException e){
			return -1;
		}
	}

	public Date getTokenDateEnd(String token) {
		return (Date) super.getSqlMapClientTemplate().queryForObject("RetrievePassword.getTokenDateEnd",token);
	}

	public Date getTokenDateChange(String token) {
		return (Date) super.getSqlMapClientTemplate().queryForObject("RetrievePassword.getTokenDateChange",token);
	}

	public void updateTokenDateChange(String token, Date date) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("fecha_cambio", date);
		map.put("token", token);
		super.getSqlMapClientTemplate().update("RetrievePassword.updateTokenDateChange",map);
	}

	public void deleteTokenUser(String token) {
		super.getSqlMapClientTemplate().delete("RetrievePassword.deleteTokenUser",token);
	}
	
	
}
