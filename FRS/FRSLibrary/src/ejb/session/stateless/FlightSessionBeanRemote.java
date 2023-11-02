/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Flight;
import java.util.List;
import javax.ejb.Remote;
import util.exception.FlightNotFoundException;
import util.exception.FlightNumberExistException;

/**
 *
 * @author zares
 */
@Remote
public interface FlightSessionBeanRemote {
    public Long createNewFlight(Flight newFlight) throws FlightNumberExistException;
    
    public Flight retrieveFlightByFlightNumber(Long flightNo) throws FlightNotFoundException;
    
    // public List<Flight> viewAllFlights();
}
