package com.sep.tripmanagementservice.configuration.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sep.tripmanagementservice.configuration.entity.Test;

public interface TestRepository extends JpaRepository<Test, Long> {
	public Test save(Test test);
}
