package com.fetch.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fetch.domain.Points;
import com.fetch.service.PointService;

@Controller
public class PointController {

	@Autowired
	PointService poinSer;
	
	@PostMapping("adduserpoint")
	public ResponseEntity<Points> addUserPoints(@RequestBody Points point){
			
			point = poinSer.save(point);
			return new ResponseEntity<Points>(point, HttpStatus.OK);
		

	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("spendpoint/{points}")
	public ResponseEntity<?> spendUserPoints(@PathVariable("points") int points){
		
		
		
		List<Points> listPayers = poinSer.findAllPoints();
		Collections.sort(listPayers,  Comparator.comparing(Points::getTimestamp));
		System.out.println(listPayers);
		
		for(int i = 0; i < listPayers.size(); i++) {
			if(listPayers.get(i).getPoints() < 0) {
				int currentI = i;
				int neg = Math.abs(listPayers.get(i).getPoints());
				listPayers.get(i).setPoints(0);
				String payerName = listPayers.get(i).getPayer();
				i--;
				while(neg > 0 && i >= 0) {
					if(payerName.equals(listPayers.get(i).getPayer()) && listPayers.get(i).getPoints() > 0) {
						int updatedPoints = (listPayers.get(i).getPoints() - neg > 0) ? listPayers.get(i).getPoints() - neg : 0;;
						neg = listPayers.get(i).getPoints() - neg > 0 ? 0 : neg - listPayers.get(i).getPoints(); 
						listPayers.get(i).setPoints(updatedPoints);
					}
					i--;
				}
				i = currentI;
			}

		}
		
		int idx = 0;
		Map<String, Integer> hm = new LinkedHashMap<>();
		while(points > 0 && idx < listPayers.size()) {
			String payerName = listPayers.get(idx).getPayer();
			if(points - listPayers.get(idx).getPoints() > 0) {								
				if(hm.containsKey(payerName)) {
					hm.put(payerName, hm.get(payerName) + listPayers.get(idx).getPoints());
				}
				else {
					hm.put(payerName, listPayers.get(idx).getPoints());
				}
				
				points -= listPayers.get(idx).getPoints();
				listPayers.get(idx).setPoints(0);				
			}
			else if(points - listPayers.get(idx).getPoints() <= 0){								
				if(hm.containsKey(payerName)) {
					hm.put(payerName, hm.get(payerName) + points);
				}
				else {
					hm.put(payerName, points);
				}
												
				listPayers.get(idx).setPoints(listPayers.get(idx).getPoints() - points);
				points = 0;			
			}						
			
			idx++;
		}
				
		
		System.out.println(hm);
		List<List<Object>> results = new ArrayList<>();
		for(Map.Entry<String, Integer> m : hm.entrySet()) {
			@SuppressWarnings("rawtypes")
			List temp = new ArrayList();
			temp.add(m.getKey());
			temp.add(-1*m.getValue() +" points");
			temp.add(new java.util.Date());
			results.add(temp);
		}
		System.out.println(listPayers);
		System.out.println(results);
		
		for(Points poin : listPayers) {
			poinSer.save(poin);
		}
		
		return new ResponseEntity<>(results, HttpStatus.OK);
		
	}
	
	@GetMapping("getpointbalance")
	public ResponseEntity<Map<String, Integer>> getAllUserPoints(){
		
		List<Points> listPayers = poinSer.findAllPoints();
		Map<String, Integer> lhm = new LinkedHashMap<>();
		
		for(Points p : listPayers) {
			System.out.println(p);
			if(lhm.containsKey(p.getPayer())) {
				lhm.put(p.getPayer(), lhm.get(p.getPayer()) + p.getPoints());
			}else {
				lhm.put(p.getPayer(), p.getPoints());
			}
		}
		System.out.println(lhm);
		return new ResponseEntity<Map<String, Integer>>(lhm, HttpStatus.OK);
		
	}
	
}
