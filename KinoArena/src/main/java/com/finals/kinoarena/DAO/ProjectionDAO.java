package com.finals.kinoarena.DAO;

import com.finals.kinoarena.Model.DTO.HallDTO;
import com.finals.kinoarena.Model.DTO.ProjectionDTO;
import com.finals.kinoarena.Model.Entity.Genre;
import com.finals.kinoarena.Model.Entity.Hall;
import com.finals.kinoarena.Model.Entity.Projection;
import com.finals.kinoarena.Model.Repository.CinemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class ProjectionDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ProjectionDTO getProjectionsById(int projectionId) throws SQLException {
        Connection c = jdbcTemplate.getDataSource().getConnection();
        String sql = "SELECT p.id,p.title,p.length,p.description,p.age_restriction,p.genre_id,g.type,p.start_at,p.end_at,p.hall_id,h.number,h.capacity,h.cinema_id\n" +
                " FROM projections AS p JOIN genres g on p.genre_id =g.id \n" +
                " JOIN halls h on p.hall_id = h.id \n" +
                "WHERE p.id = ?";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, projectionId);
        ResultSet rs = ps.executeQuery();
        rs.next();
        ProjectionDTO projectionDTO = new ProjectionDTO();
        projectionDTO.setId(rs.getInt(1));
        projectionDTO.setTitle(rs.getString(2));
        projectionDTO.setLength(rs.getInt(3));
        projectionDTO.setDescription(rs.getString(4));
        projectionDTO.setAgeRestriction(rs.getInt(5));
        Genre g = new Genre();
        g.setId(rs.getInt(6));
        g.setType(rs.getString(7));
        projectionDTO.setGenre(g);
        projectionDTO.setStartAt(rs.getTimestamp(8).toLocalDateTime());
        projectionDTO.setStartAt(rs.getTimestamp(9).toLocalDateTime());
        HallDTO hall = new HallDTO();
        hall.setId(rs.getInt(10));
        hall.setNumber(rs.getInt(11));
        hall.setCapacity(rs.getInt(12));
        int cinemaId = rs.getInt(13);
        //hall.setCinema(cinemaId);
        projectionDTO.setHall(hall);
        return projectionDTO;
    }

    public boolean isProjectionInCinema(int cinemaId, int projectionId) throws SQLException {
        Connection c = jdbcTemplate.getDataSource().getConnection();
        String sql = "SELECT * FROM projections AS p JOIN halls AS h ON p.hall_id = h.id JOIN cinemas AS c ON h.cinema_id = c.id WHERE c.id = ? AND p.id = ?";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, cinemaId);
        ps.setInt(2,projectionId);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }
    //TODO finish and replace in projectionService
    public List<Projection> findProjectionsInCity(String city) throws SQLException {
        Connection c = jdbcTemplate.getDataSource().getConnection();
        String sql = "SELECT p.id,title,length,p.description,g.type,start_at,h.number AS hall_number,c.name AS cinema_name \n" +
                "FROM projections AS p \n" +
                "JOIN genres AS g \n" +
                "ON g.id = p.genre_id \n" +
                "JOIN halls AS h \n" +
                "ON h.id = p.hall_id \n" +
                "JOIN cinemas AS c\n" +
                "ON c.id = h.cinema_id\n" +
                "WHERE city = ?";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setString(1, city);
        ResultSet rs = ps.executeQuery();
        return null;
    }



}
