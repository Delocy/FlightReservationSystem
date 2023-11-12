/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Flight;
import java.util.List;
import javax.ejb.Remote;
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
@Remote
public interface FlightSessionBeanRemote {
//    public Long createNewFlight(Flight newFlight) throws FlightExistException, UnknownPersistenceException;
    public Long createNewFlight(Flight newFlight, Long configId, Long routeId) throws FlightExistException, UnknownPersistenceException, FlightRouteNotFoundException, AircraftConfigNotFoundException;
    
    public Flight retrieveFlightByFlightNumber(String flightNo) throws FlightNotFoundException;
    
    public Flight retrieveFlightByFlightID(Long id) throws FlightNotFoundException;
    
    public List<Flight> viewAllFlights();
    
    public void deleteFlight(Long flightId);
    
    public void updateFlight(Long existingFlightID, Flight newFlight);
    
    public void associateOriginalFlightWithReturnFlight(Long originalFlightID, Long returnFlightID) throws FlightNotFoundException;
    
    public List<Flight> retrieveFlightsByFlightRoute(String origin, String destination) throws FlightNotFoundException;

    public List<Flight[]> retrieveConnectingFlightsByFlightRoute(String origin, String destination) throws FlightNotFoundException;
}
