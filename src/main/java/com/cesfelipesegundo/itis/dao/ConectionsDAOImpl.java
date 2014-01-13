package com.cesfelipesegundo.itis.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.cesfelipesegundo.itis.dao.api.ConectionsDAO;
import com.cesfelipesegundo.itis.model.Conection;
import com.cesfelipesegundo.itis.model.User;


public class ConectionsDAOImpl extends SqlMapClientDaoSupport implements ConectionsDAO{

	public ConectionsDAOImpl(){
		super();
	}
	
	public List<Conection> get100LastConections() {
		List<Conection> list = null;
		list = (List<Conection>)super.getSqlMapClientTemplate().queryForList("Conections.100LastConections");
		return list;
	}

	public void addNewConection(User user, String ip) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("idUsuario", user.getId());
		map.put("ip", ip);
		super.getSqlMapClientTemplate().insert("Conections.addNewConection", map);
	}

	public List<Conection> runFilterAndSearch100Conections(Long idConection,
			String userNameConection, Date date1, Date date2) {
		HashMap<String,Object> parameters = new HashMap<String,Object>();
		if(idConection!=null){
			parameters.put("idConection", "%"+idConection+"%");
		}else{
			parameters.put("idConection", null);
		}
		if(userNameConection!=null){
			if(userNameConection.trim().equalsIgnoreCase("")){
				parameters.put("userNameConection",null);
			}else{
				parameters.put("userNameConection","%"+userNameConection+"%");
			}
		}else{
			parameters.put("userNameConection",null);
		}
		if(date1!=null){
			parameters.put("date1",date1);
		}else{
			parameters.put("date1",null);
		}
		if(date2!=null){
			parameters.put("date2",date2);
		}else{
			parameters.put("date2",null);
		}
		List<Conection> list = super.getSqlMapClientTemplate().queryForList("Conections.runFilterAndSearch100Conections",parameters);
		return list;
	}

	public List<Conection> show5LastConections(Long id) {
		return super.getSqlMapClientTemplate().queryForList("Conections.show5LastConections",id);
	}

}
