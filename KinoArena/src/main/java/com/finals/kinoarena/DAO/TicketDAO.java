package com.finals.kinoarena.DAO;

import com.finals.kinoarena.Exceptions.NotFoundException;
import com.finals.kinoarena.Model.Entity.Projection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TicketDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean getProjectionsInCinema(int cinemaId, int projectionId) throws SQLException {
        Connection c = jdbcTemplate.getDataSource().getConnection();
        String sql = "SELECT * FROM projections AS p JOIN halls AS h ON p.hall_id = h.id JOIN cinemas AS c ON h.cinema_id = c.id WHERE c.id = ? AND p.id = ?";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, cinemaId);
        ps.setInt(2,projectionId);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }
}
