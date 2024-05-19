package com.sep.tripmanagementservice.configuration.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sep.tripmanagementservice.configuration.entity.SiteContent;
import com.sep.tripmanagementservice.configuration.enums.CommonStatus;

@Repository
public interface SiteContentRepository extends JpaRepository<SiteContent, Long> {
	@Query("SELECT COUNT(s) FROM SiteContent s WHERE s.id=:id AND added_by=:user")
	public long findCountByIdForUser(@Param("id") Long id, @Param("user") Long user);
	
	@Query("SELECT s FROM SiteContent s WHERE s.added_by=:userId AND s.status=:status")
	public List<SiteContent> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") CommonStatus status);
	
	@Query("SELECT s FROM SiteContent s WHERE s.id=:id AND s.added_by=:userId AND s.status!=com.sep.tripmanagementservice.configuration.enums.CommonStatus.DELETED")
	public SiteContent findByIdAndUser(@Param("id") Long id, @Param("userId") Long userId);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("UPDATE SiteContent s SET s.mediaURL=:mediaURL WHERE s.id=:id")
	public int updateMediaById(@Param("id") Long id, @Param("mediaURL") String mediaURL);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("UPDATE SiteContent s SET s.status=:status WHERE s.id=:id")
	public int updateStatusById(@Param("status") CommonStatus status, @Param("id") Long id);
	
}
