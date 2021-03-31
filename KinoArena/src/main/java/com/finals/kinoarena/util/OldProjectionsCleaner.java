package com.finals.kinoarena.util;

import com.finals.kinoarena.model.DTO.ProjectionToCleanDTO;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.LinkedList;

public class OldProjectionsCleaner implements Runnable {

    private static final int DAEMON_THREAD_CLEANER_TIME_TO_SLEEP_ONE_DAY_MILLIS = 1000*60*60*24;
    private static final String SELECT_ALL_PROJECTIONS = "SELECT * FROM `kinoarena`.projections";
    private static final String DELETE_PROJECTION = "DELETE FROM projections WHERE id = ";

    private List<ProjectionToCleanDTO> projections = new LinkedList<>();
    private Statement statement;
    private Statement statement1;

    public OldProjectionsCleaner() throws SQLException, ClassNotFoundException{
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://84.238.145.199:7777/kinoarena?user=cadet&password=survivor2021&useLegacyDatetimeCode=false&serverTimezone=EET");
        statement = con.createStatement();
        statement1 = con.createStatement();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(DAEMON_THREAD_CLEANER_TIME_TO_SLEEP_ONE_DAY_MILLIS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ResultSet rs = null;
            try {
                rs = statement.executeQuery( SELECT_ALL_PROJECTIONS+ ";");
            } catch (SQLException e) {
                System.out.println("SQL exception in old projections cleaner thread");
                e.printStackTrace();
            }
            try {
                while (rs.next()) {
                    ProjectionToCleanDTO proj = new ProjectionToCleanDTO(rs.getInt(1), rs.getDate(5));
                    this.projections.add(proj);
                    LocalDate projDate = proj.getDate().toLocalDate();
                    LocalDate dateToCompare = LocalDate.now();
                    if(projDate.isBefore(dateToCompare)) {
                        System.out.println("Deleted projection with id  " + proj.getId());
                        statement1.executeUpdate(DELETE_PROJECTION  + proj.getId() + ";");
                    }
                }
                rs.close();
            } catch (SQLException e) {
                System.out.println("SQL exception in old offer cleaner thread");
                e.printStackTrace();
            }
        }
    }
}
