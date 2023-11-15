/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Itinerary;
import java.util.List;
import javax.ejb.Local;
import util.exception.CustomerNotFoundException;
import util.exception.ItineraryExistException;
import util.exception.ItineraryNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author xinni
 */
@Local
public interface ItinerarySessionBeanLocal {

    public Itinerary createNewItinerary(Itinerary itinerary, long userId) throws UnknownPersistenceException, CustomerNotFoundException, ItineraryExistException;

    public Itinerary retrieveItineraryByID(long itineraryId) throws ItineraryNotFoundException;

    public List<Itinerary> retrieveItinerariesByCustomerId(Long customerId);
    
}
