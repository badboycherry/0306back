package com.eco.eco_Api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.eco.eco_Api.entity.Mission;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Long> {
    
    @Query(value = "SELECT * FROM mission ORDER BY RAND() LIMIT 5", nativeQuery = true)
    List<Mission> findRandomMissions();
    
}
