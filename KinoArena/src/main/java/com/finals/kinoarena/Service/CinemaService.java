package com.finals.kinoarena.Service;

import com.finals.kinoarena.Exceptions.BadCredentialsException;
import com.finals.kinoarena.Exceptions.CinemaAlreadyExistException;
import com.finals.kinoarena.Exceptions.MissingCinemasInDBException;
import com.finals.kinoarena.Exceptions.NotFoundException;
import com.finals.kinoarena.Model.DTO.CinemaDTO;
import com.finals.kinoarena.Model.Entity.Cinema;
import com.finals.kinoarena.Model.Repository.CinemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CinemaService extends AbstractService {


    @Autowired
    private CinemaRepository cinemaRepository;

    public List<CinemaDTO> getAllCinemas() throws MissingCinemasInDBException {
        List<Cinema> cinemas = cinemaRepository.findAll();
        if(cinemas.isEmpty()){
            throw new MissingCinemasInDBException("No found cinemas");
        }
        List<CinemaDTO> cinemaDTOS = new ArrayList<>();
        for (Cinema c:cinemas
             ) {
            cinemaDTOS.add(new CinemaDTO(c));
        }
        return cinemaDTOS;
    }

    public CinemaDTO getCinemaByID(int id){
        Optional<Cinema> schrodingerCinema = cinemaRepository.findById(id);
        if(schrodingerCinema.isPresent()){
            return new CinemaDTO(schrodingerCinema.get());
        }else{
            throw new NotFoundException("Cinema not found");
        }
    }

    public List<CinemaDTO> getAllCinemasByCity(String city) throws MissingCinemasInDBException {
        List<Cinema> cinemas = cinemaRepository.findByCity(city);
        if(cinemas.isEmpty()){
            throw new MissingCinemasInDBException("No found cinemas in this city");
        }
        List<CinemaDTO> cinemaDTOS = new ArrayList<>();
        for (Cinema c:cinemas
        ) {
            cinemaDTOS.add(new CinemaDTO(c));
        }
        return cinemaDTOS;

    }


    public CinemaDTO addCinema(CinemaDTO cinemaDTO) throws CinemaAlreadyExistException {
        if(cinemaExist(cinemaDTO)){
            throw new CinemaAlreadyExistException("There is already a cinema with that name in that city");
        }
        Cinema c =cinemaRepository.save(new Cinema(cinemaDTO.getName(),cinemaDTO.getCity()));
        return new CinemaDTO(c);


    }

    public boolean cinemaExist(CinemaDTO cinemaDTO){
        List<Cinema> cinemas =cinemaRepository.findByCity(cinemaDTO.getCity());
        for (Cinema c: cinemas
             ) {
            if(c.getName().equals(cinemaDTO.getName())){
                return true;
            }
        }
        return false;
    }

    public void removeCinema(int id) {
        CinemaDTO cinemaforDelete = getCinemaByID(id);
        cinemaRepository.deleteById(cinemaforDelete.getId());

    }

    public CinemaDTO editCinema(CinemaDTO cinemaDTO,int id) throws BadCredentialsException {
        Optional<Cinema> sCinema = cinemaRepository.findById(id);
        if(!sCinema.isPresent()){
            throw new NotFoundException("Cinema is not found");
        }
        Cinema cinema = sCinema.get();
        if(cinema.getName().equals(cinemaDTO.getName()) && cinema.getCity().equals(cinemaDTO.getCity())){
            throw new BadCredentialsException("You need to change the fields for an edit");
        }
        cinema.setCity(cinemaDTO.getCity());
        cinema.setName(cinemaDTO.getName());
        cinemaRepository.save(cinema);
        return new CinemaDTO(cinemaRepository.findById(id).get());
    }
}
