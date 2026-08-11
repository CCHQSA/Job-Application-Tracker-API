package com.cchqsa.job_application_tracker.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String name;
    private String lastName;
    private String email;

}