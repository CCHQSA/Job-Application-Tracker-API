package com.cchqsa.job_application_tracker.repository;

import com.cchqsa.job_application_tracker.entity.Application;
import com.cchqsa.job_application_tracker.entity.Interview;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InterviewRepository extends JpaRepository<Interview,Long> {
    List<Interview> findByApplicationIn(List<Application> applications);

    List<Interview> findByApplicationInOrderByDateTimeDesc(List<Application> applications);

    @Modifying
    @Transactional
    @Query("DELETE FROM Interview i WHERE i.application IN :applications")
    void deleteAllByApplicationIn(@Param("applications") List<Application> applicationList);
}
