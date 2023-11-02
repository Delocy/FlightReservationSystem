/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package frsmanagementclient;

import ejb.session.stateless.AircraftTypeSessionBeanRemote;
import ejb.session.stateless.AirportSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.FlightRouteSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;
import javax.ejb.EJB;
import util.exception.AirportNotFoundException;
import util.exception.FlightRouteExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author zares
 */
public class Main {

    @EJB
    private static AircraftTypeSessionBeanRemote aircraftTypeSessionBeanRemote;

    @EJB
    private static AirportSessionBeanRemote airportSessionBeanRemote;

    @EJB
    private static FlightSessionBeanRemote flightSessionBeanRemote;

    @EJB
    private static FlightRouteSessionBeanRemote flightRouteSessionBeanRemote;
    
    @EJB
    private static EmployeeSessionBeanRemote employeeSessionBeanRemote;
    
    
    public static void main(String[] args) throws AirportNotFoundException, FlightRouteExistException, UnknownPersistenceException {
        MainApp mainApp = new MainApp(employeeSessionBeanRemote, flightSessionBeanRemote, flightRouteSessionBeanRemote , airportSessionBeanRemote, aircraftTypeSessionBeanRemote);
        mainApp.runApp();
    }
    
}
