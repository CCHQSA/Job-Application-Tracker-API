package com.cchqsa.job_application_tracker.repository;

import com.cchqsa.job_application_tracker.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("DELETE FROM Application a WHERE a.id = :id AND a.user = :user")
    void deleteByIdAndUser(@Param("id") Long id, @Param("user") User user);
}

