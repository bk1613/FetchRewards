package com.fetch.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fetch.dao.PointDAO;
import com.fetch.domain.Points;
import com.fetch.repository.PointRepository;

@Service
public class PointService {
	
	@Autowired
	PointRepository pointrepo;

	
	@Autowired
	PointDAO pointdao;
	
	public Points save(Points point) {
		return pointrepo.save(point);
	}
	
	public Points findpointsbypayer(String name) {
		return pointrepo.findByPayer(name);
	}
	
	public List<Points> findAllPoints(){
		return pointrepo.findAll();
	}
}
