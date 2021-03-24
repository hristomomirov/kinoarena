package com.finals.kinoarena.Srvice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class AbstractService {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

}
