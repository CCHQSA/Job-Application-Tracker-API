package com.cchqsa.job_application_tracker.mapper.Impl;

import com.cchqsa.job_application_tracker.dto.UserDto;
import com.cchqsa.job_application_tracker.entity.User;
import com.cchqsa.job_application_tracker.enums.Role;
import com.cchqsa.job_application_tracker.mapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements ModelMapper<User, UserDto> {

    @Override
    public UserDto mapTo(User user) {
        if (user == null) {
            return null;
        }
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        return dto;
    }


    @Override
    public User mapFrom(UserDto dto) {
        if (dto == null) {
            return null;
        }
        User user = new User();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setRole(Role.ROLE_USER);
        return user;
    }
}
