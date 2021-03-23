package com.finals.kinoarena.Controller;

import com.finals.kinoarena.DAO.CinemaDao;
import com.finals.kinoarena.DTO.CinemaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@Component
@RestController
public class CinemaController {

    @Autowired
    private CinemaDao cinemaDao;


    @GetMapping(value = "/cinemas")
    public List<CinemaDTO> getAllCinemas(){

        return this.cinemaDao.getAllCinemas();
    }

    @GetMapping(value = "/test")
    public String testFunction(){
        return "Tova e prosto show";
    }
}
