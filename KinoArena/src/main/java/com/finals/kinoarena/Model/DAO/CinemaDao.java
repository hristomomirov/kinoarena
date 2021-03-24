package com.finals.kinoarena.Model.DAO;

import com.finals.kinoarena.Model.DTO.CinemaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CinemaDao extends AbstractDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<CinemaDTO> getAllCinemas() throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        ResultSet rs = connection.createStatement().executeQuery("SELECT id,name,city FROM cinemas;");

        List<CinemaDTO> cinemas = new ArrayList<>();
        while (rs.next()) {
            CinemaDTO cinema = new CinemaDTO(rs.getInt(1), rs.getString(2), rs.getString(3));
            cinemas.add(cinema);
        }
        return cinemas;
    }
}
