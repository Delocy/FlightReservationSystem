/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Flight;
import java.util.List;
import javax.ejb.Local;
import util.exception.AircraftConfigNotFoundException;
import util.exception.FlightExistException;
import util.exception.FlightNotFoundException;
import util.exception.FlightNumberExistException;
import util.exception.FlightRouteNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author zares
 */
@Local
public interface FlightSessionBeanLocal {

//    public Long createNewFlight(Flight newFlight) throws FlightExistException, UnknownPersistenceException;

    public Flight retrieveFlightByFlightNumber(String flightNo) throws FlightNotFoundException;

    //public List<Flight> viewAllFlights();

    //public Flight retrieveFlightByFlightID(Long flightId);

    public Flight retrieveFlightByFlightID(Long id) throws FlightNotFoundException;

    public List<Flight> viewAllFlights();

    public void deleteFlight(Long flightId);

    public void updateFlight(Long existingFlightID, Flight newFlight);

    public Long createNewFlight(Flight newFlight, Long configId, Long routeId) throws FlightExistException, UnknownPersistenceException, FlightRouteNotFoundException, AircraftConfigNotFoundException;

    public void associateOriginalFlightWithReturnFlight(Long originalFlightID, Long returnFlightID) throws FlightNotFoundException;
    
}
