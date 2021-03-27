package com.finals.kinoarena.Service;

import com.finals.kinoarena.Exceptions.BadRequestException;
import com.finals.kinoarena.Model.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class AbstractService {

    @Autowired
    protected JdbcTemplate jdbcTemplate;
    @Autowired
    protected UserRepository userRepository;

    protected boolean isAdmin(int id) throws BadRequestException {
        return userRepository.findById(id).get().getRoleId() == 2;
    }
}
