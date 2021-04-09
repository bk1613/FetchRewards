package com.fetch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fetch.domain.Points;

public interface PointRepository extends JpaRepository<Points, Integer> {

	public Points findByPayer(String payer);
	
}
