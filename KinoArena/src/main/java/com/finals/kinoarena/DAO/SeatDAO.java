package com.finals.kinoarena.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Component
public class    SeatDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addFreeSeats(int projectionId, int capacity) throws SQLException {
        Connection c = jdbcTemplate.getDataSource().getConnection();
        PreparedStatement ps = c.prepareStatement("INSERT INTO projections_have_seats (projection_id,seat_id) VALUES (?,?);");
        for (int i = 1; i <= capacity; i++) {
            ps.setInt(1, projectionId);
            ps.setInt(2, i);
            ps.executeUpdate();
        }
    }

    public void addSeats() throws SQLException {
        Connection c = jdbcTemplate.getDataSource().getConnection();
        PreparedStatement ps = c.prepareStatement("UPDATE seats SET number = ? WHERE id = ?;");
        for (int i = 1; i <= 200; i++) {
            ps.setInt(1, i);
            ps.setInt(2, i);
            ps.executeUpdate();
        }
    }

    public List<Integer> getFreeSeatsForProjection(int projectionId) throws SQLException {
        List<Integer> seats = new ArrayList<>();
        Connection c = jdbcTemplate.getDataSource().getConnection();
        PreparedStatement ps = c.prepareStatement("SELECT seat_id FROM projections_have_seats WHERE projection_id = ?");
        ps.setInt(1, projectionId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            seats.add(rs.getInt("seat_id"));
        }
        return seats;
    }

    //add int count
    public void removeSeat(int seat, int projectionId) throws SQLException {
        Connection c = jdbcTemplate.getDataSource().getConnection();
        PreparedStatement ps = c.prepareStatement("DELETE FROM projections_have_seats WHERE projection_id = ? AND seat_id = ?;");
        ps.setInt(1, projectionId);
        ps.setInt(2, seat);
        ps.executeUpdate();
    }

    public int getMaxSeatNum() throws SQLException {
        Connection c = jdbcTemplate.getDataSource().getConnection();
        PreparedStatement ps = c.prepareStatement("SELECT MAX(number) AS max FROM seats");
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt("max");
    }

    public int getMinSeatNum() throws SQLException {
        Connection c = jdbcTemplate.getDataSource().getConnection();
        PreparedStatement ps = c.prepareStatement("SELECT MIN(number) AS min FROM seats");
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt("min");
    }
}
