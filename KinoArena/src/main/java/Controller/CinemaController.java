package Controller;

import DAO.CinemaDao;
import DTO.CinemaDTO;
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

        try {
            return this.cinemaDao.getAllCinemas();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }
}
