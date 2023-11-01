/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Airport;
import javax.ejb.Local;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author xinni
 */
@Local
public interface AirportSessionBeanLocal {

    public Long createNewAirport(Airport airport) throws UnknownPersistenceException;
    
}
