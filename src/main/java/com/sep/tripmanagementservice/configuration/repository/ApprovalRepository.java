package com.sep.tripmanagementservice.configuration.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sep.tripmanagementservice.configuration.entity.Approval;
import com.sep.tripmanagementservice.configuration.enums.ApprovalStatus;

@Repository
public interface ApprovalRepository extends JpaRepository<Approval, Long> {

	List<Approval> findByApprovalStatus(ApprovalStatus approvalStatus, Pageable pageable);

	Approval findByIdAndEmail(Long id, String email);

}
