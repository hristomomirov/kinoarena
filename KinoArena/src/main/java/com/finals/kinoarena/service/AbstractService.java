package com.finals.kinoarena.service;

import com.finals.kinoarena.DAO.SeatDAO;
import com.finals.kinoarena.DAO.StatisticsDAO;
import com.finals.kinoarena.model.repository.*;
import com.finals.kinoarena.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class AbstractService {

    @Autowired
    protected JdbcTemplate jdbcTemplate;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected CinemaRepository cinemaRepository;
    @Autowired
    protected HallRepository hallRepository;
    @Autowired
    protected GenreRepository genreRepository;
    @Autowired
    protected MovieRepository movieRepository;
    @Autowired
    protected ProjectionRepository projectionRepository;
    @Autowired
    protected TicketRepository ticketRepository;
    @Autowired
    protected SeatDAO seatDAO;
    @Autowired
    protected StatisticsDAO statisticsDAO;

    protected boolean isAdmin(int id) {
        return userRepository.findById(id).get().getRoleId() == Constants.ROLE_ADMIN;
    }
}
