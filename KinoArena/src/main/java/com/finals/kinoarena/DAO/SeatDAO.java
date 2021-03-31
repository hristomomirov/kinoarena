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
import java.util.Objects;


@Component
public class SeatDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addFreeSeats(int projectionId, int capacity) throws SQLException {
        Connection c = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
        PreparedStatement ps = c.prepareStatement("INSERT INTO projections_have_seats (projection_id,seat_id) VALUES (?,?);");
        for (int i = 1; i <= capacity; i++) {
            ps.setInt(1, projectionId);
            ps.setInt(2, i);
            ps.executeUpdate();
        }
        c.close();
    }

    public List<Integer> getFreeSeatsForProjection(int projectionId) {
        List<Integer> seats = new ArrayList<>();
        try(Connection c = jdbcTemplate.getDataSource().getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT seat_id FROM projections_have_seats WHERE projection_id = ?")) {
            ps.setInt(1, projectionId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                seats.add(rs.getInt("seat_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seats;
    }

    public void removeSeat(int seat, int projectionId) throws SQLException {
        Connection c = jdbcTemplate.getDataSource().getConnection();
        PreparedStatement ps = c.prepareStatement("DELETE FROM projections_have_seats WHERE projection_id = ? AND seat_id = ?;");
        ps.setInt(1, projectionId);
        ps.setInt(2, seat);
        ps.executeUpdate();
        c.close();
    }

    public int getMaxSeatNum() throws SQLException {
        Connection c = jdbcTemplate.getDataSource().getConnection();
        PreparedStatement ps = c.prepareStatement("SELECT MAX(id) AS max FROM seats");
        ResultSet rs = ps.executeQuery();
        rs.next();
        int max = rs.getInt("max");
        c.close();
        return max;
    }
}
