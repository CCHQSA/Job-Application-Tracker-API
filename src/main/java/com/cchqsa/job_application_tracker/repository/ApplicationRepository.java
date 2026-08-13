package com.cchqsa.job_application_tracker.repository;

import com.cchqsa.job_application_tracker.entity.Application;
import com.cchqsa.job_application_tracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application,Long> {
    List<Application> getApplicationsByUser(User user);

    Optional<Application> findByIdAndUser(Long id, User user);
}
