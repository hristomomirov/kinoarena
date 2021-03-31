package com.finals.kinoarena.service;

import com.finals.kinoarena.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class AbstractService {

    @Autowired
    protected JdbcTemplate jdbcTemplate;
    @Autowired
    protected UserRepository userRepository;

    protected boolean isAdmin(int id) {
        return userRepository.findById(id).get().getRoleId() == 2;
    }
}
