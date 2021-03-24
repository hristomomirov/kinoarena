package com.finals.kinoarena.Model.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class AbstractDao {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

}
