package com.finals.kinoarena.Controller;


import com.finals.kinoarena.Exceptions.MissingCinemasInDBException;
import com.finals.kinoarena.Model.DAO.CinemaDao;
import com.finals.kinoarena.Model.DTO.CinemaDTO;
import com.finals.kinoarena.Model.Entity.Cinema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;


@Component
@RestController
public class CinemaController {

    @Autowired
    private CinemaDao cinemaDao;


    @GetMapping(value = "/cinemas")
    public List<CinemaDTO> getAllCinemas() throws MissingCinemasInDBException, SQLException {
        if (cinemaDao.getAllCinemas() != null) {
            return this.cinemaDao.getAllCinemas();
        } else {
            throw new MissingCinemasInDBException("No cinemas found in the DB");
        }
    }

    @PostMapping(value = "/admin/addCinema")
    public Cinema addCinema(@RequestBody CinemaDTO cinema, HttpServletRequest request, HttpServletResponse response) {
        //TODO
        return null;
    }

    @GetMapping(value = "/test")
    public String testFunction() {
        return "Tova e prosto show";
    }
}
