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
public class CinemaDao extends AbstractDao {

    public List<CinemaDTO> getAllCinemas(){
        //TODO rework with hybernate
        List<CinemaDTO> cinemas = new ArrayList<>();
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();){
            ResultSet rs = connection.createStatement().executeQuery("SELECT id,name,city FROM kinoarena.cinemas;");

            while (rs.next()){
                CinemaDTO cinema = new CinemaDTO(rs.getInt(1),rs.getString(2),rs.getString(3));
                cinemas.add(cinema);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return cinemas;
    }
}
