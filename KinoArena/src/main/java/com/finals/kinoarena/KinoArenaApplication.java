package com.finals.kinoarena;


import com.finals.kinoarena.util.OldProjectionsCleaner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.sql.SQLException;

@SpringBootApplication
public class KinoArenaApplication {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        
        SpringApplication.run(KinoArenaApplication.class, args);
        Thread cleaner = new Thread(new OldProjectionsCleaner());
        cleaner.setDaemon(true);
        cleaner.start();
    }

}
