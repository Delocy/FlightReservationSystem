/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Airport;
import entity.FlightRoute;
import java.util.List;
import javax.ejb.Remote;
import util.exception.AirportNotFoundException;
import util.exception.FlightRouteExistException;
import util.exception.FlightRouteNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author zares
 */
@Remote
public interface FlightRouteSessionBeanRemote {
    public FlightRoute createFlightRoute(Long originAirportID, Long destinationAirportID) throws AirportNotFoundException, FlightRouteExistException, UnknownPersistenceException;
    
    public List<FlightRoute> viewAllFlightRoutes();
    
    public void deleteFlightRoute(Long routeID) throws FlightRouteNotFoundException;
    
    public FlightRoute retrieveFlightRouteByRouteID(Long routeID) throws FlightRouteNotFoundException;
}
