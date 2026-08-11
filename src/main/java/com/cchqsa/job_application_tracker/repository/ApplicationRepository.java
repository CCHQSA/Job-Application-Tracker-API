package com.cchqsa.job_application_tracker.repository;

import com.cchqsa.job_application_tracker.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application,Long> {
}
