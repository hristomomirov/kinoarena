package com.finals.kinoarena.DAO;

import com.finals.kinoarena.DTO.CinemaDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CinemaDao {

    private JdbcTemplate jdbcTemplate;

    public List<CinemaDTO> getAllCinemas() throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ResultSet rs = connection.createStatement().executeQuery("SELECT id,name,city FROM cinema;");

        List<CinemaDTO> cinemas = new ArrayList<>();
        while (rs.next()){
            CinemaDTO cinema = new CinemaDTO(rs.getInt(1),rs.getString(2),rs.getString(3));
            cinemas.add(cinema);
        }
        return cinemas;
    }
}
