package com.finals.kinoarena.DAO;

import com.finals.kinoarena.model.DTO.StatisticsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class StatisticsDAO {

    private static final String SELECT_TICKETS= " SELECT c.name as cinema_name,h.number as hall_number,m.title, count(t.id) as" +
            " total_tickets_sold FROM tickets t " +
            "join projections p on t.projection_id = p.id " +
            "join movies m on p.movie_id = m.id join halls h on p.hall_id=h.id " +
            "join cinemas c on h.cinema_id = c.id group by hall_id;";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<StatisticsDTO> soldTicketsPerProjection(){
            List<StatisticsDTO> statisticsDAOS = new ArrayList<>();

        ResultSet rs = null;
        try (Connection c = jdbcTemplate.getDataSource().getConnection()){
            rs = c.createStatement().executeQuery(SELECT_TICKETS);
            while (rs.next()){
                StatisticsDTO s = new StatisticsDTO();
                s.setCinemaName(rs.getString(1));
                s.setHallNumber(rs.getInt(2));
                s.setTitle(rs.getString(3));
                s.setTotalTicketsSold(rs.getInt(4));
                statisticsDAOS.add(s);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return statisticsDAOS;

    }
}
