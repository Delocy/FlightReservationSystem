/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Airport;
import javax.ejb.Remote;
import util.exception.AirportNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author xinni
 */
@Remote
public interface AirportSessionBeanRemote {
    public Long createNewAirport(Airport airport) throws UnknownPersistenceException;

    public Airport retrieveAirportByAirportId(Long airportId) throws AirportNotFoundException;

    public Airport retrieveAirportByAirportCode(String airportCode) throws AirportNotFoundException;
}
