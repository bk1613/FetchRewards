package com.fetch.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fetch.domain.Points;

@Repository
public class PointDAO {

	@Autowired
	SessionFactory sessionfactory;
	Session s = null;
	
	public List<Points> findAllPoints(){
		
		List<Points> li = new ArrayList<>();
		try (Session se = sessionfactory.openSession();){
			se.beginTransaction();
			li = se.createQuery("from Points").list();
			li.forEach(a->System.out.println(a.getId() + ", " + a.getPayer()+ ", " +a.getPoints()+ ", " +a.getTimestamp()));
		}
		return li;
	}
	
	public Points findByPayer(String payer) {
		
		Points po = null;
		
		try(Session session = sessionfactory.openSession();){
			session.beginTransaction();
			po = session.get(Points.class, payer);
		}catch(Exception ex) {
			System.out.println("Problem in checking transaction");
			ex.printStackTrace();
		}
		
		return po;
	}
	
}
