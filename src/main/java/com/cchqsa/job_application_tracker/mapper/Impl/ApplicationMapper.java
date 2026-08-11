package com.cchqsa.job_application_tracker.mapper;

import com.cchqsa.job_application_tracker.dto.ApplicationDto;
import com.cchqsa.job_application_tracker.entity.Application;
import org.springframework.stereotype.Component;

@Component
public class ApplicationMapper implements ModelMapper<Application, ApplicationDto> {

    @Override
    public ApplicationDto mapTo(Application application) {
        if (application == null) {
            return null;
        }

        ApplicationDto dto = new ApplicationDto();
        dto.setId(application.getId());
        dto.setCompany(application.getCompany());
        dto.setPosition(application.getPosition());
        dto.setLocation(application.getLocation());
        dto.setSalaryFrom(application.getSalaryFrom());
        dto.setSalaryTo(application.getSalaryTo());
        dto.setStatus(application.getStatus());
        dto.setCurrency(application.getCurrency());
        dto.setApplicationDate(application.getApplicationDate());
        dto.setJobUrl(application.getJobUrl());
        dto.setDescription(application.getDescription());
        dto.setNotes(application.getNotes());
        dto.setCreatedAt(application.getCreatedAt());
        dto.setUpdatedAt(application.getUpdatedAt());

        return dto;
    }

    @Override
    public Application mapFrom(ApplicationDto dto) {
        if (dto == null) {
            return null;
        }

        Application application = new Application();
        application.setId(dto.getId());
        application.setCompany(dto.getCompany());
        application.setPosition(dto.getPosition());
        application.setLocation(dto.getLocation());
        application.setSalaryFrom(dto.getSalaryFrom());
        application.setSalaryTo(dto.getSalaryTo());
        application.setStatus(dto.getStatus());
        application.setCurrency(dto.getCurrency());
        application.setApplicationDate(dto.getApplicationDate());
        application.setJobUrl(dto.getJobUrl());
        application.setDescription(dto.getDescription());
        application.setNotes(dto.getNotes());
        application.setCreatedAt(dto.getCreatedAt());
        application.setUpdatedAt(dto.getUpdatedAt());
        return application;
    }
}
