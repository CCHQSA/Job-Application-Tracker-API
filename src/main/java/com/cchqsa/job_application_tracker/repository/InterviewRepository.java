package com.cchqsa.job_application_tracker.repository;

import com.cchqsa.job_application_tracker.entity.Application;
import com.cchqsa.job_application_tracker.entity.Interview;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InterviewRepository extends JpaRepository<Interview, Long> {

    List<Interview> findByApplicationIn(List<Application> applications);

    List<Interview> findByApplicationInOrderByDateTimeDesc(List<Application> applications);

    @Query("""
        SELECT i
        FROM Interview i
        JOIN FETCH i.application a
        JOIN FETCH a.user
        WHERE i.application IN :applications
        ORDER BY i.dateTime DESC
    """)
    List<Interview> findByApplicationInWithApplication(
            @Param("applications") List<Application> applications
    );

    @Query("""
        SELECT i
        FROM Interview i
        JOIN FETCH i.application a
        JOIN FETCH a.user
        WHERE i.id = :interviewId
    """)
    Optional<Interview> findByIdWithApplicationAndUser(@Param("interviewId") Long interviewId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Interview i WHERE i.application IN :applications")
    void deleteAllByApplicationIn(@Param("applications") List<Application> applicationList);
}
