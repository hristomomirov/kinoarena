package com.finals.kinoarena.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class AbstractService {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

}
