package com.cchqsa.job_application_tracker.service.Impl;

import com.cchqsa.job_application_tracker.entity.Application;
import com.cchqsa.job_application_tracker.entity.Interview;
import com.cchqsa.job_application_tracker.repository.InterviewRepository;
import com.cchqsa.job_application_tracker.service.InterviewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InterviewServiceImpl implements InterviewService {
    private final InterviewRepository interviewRepository;

    public InterviewServiceImpl(InterviewRepository interviewRepository) {
        this.interviewRepository = interviewRepository;
    }


    @Override
    public List<Interview> findByApplicationIn(List<Application> applications) {
        return interviewRepository.findByApplicationIn(applications);
    }

    @Override
    public List<Interview> findAllByApplicationIn(List<Application> applications) {
        return interviewRepository.findByApplicationInOrderByDateTimeDesc(applications);
    }

    @Override
    @Transactional
    public Interview save(Interview interview) {
        return interviewRepository.saveAndFlush(interview);
    }

    @Override
    public void delete(Interview interview) {
        interviewRepository.delete(interview);
    }

    @Override
    public Optional<Interview> findById(Long interviewId) {
        return interviewRepository.findById(interviewId);
    }
}
