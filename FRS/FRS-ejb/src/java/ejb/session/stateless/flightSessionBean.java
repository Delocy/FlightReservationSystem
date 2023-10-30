/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Employee;
import entity.Flight;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.EmployeeNotFoundException;
import util.exception.FlightNotFoundException;
import util.exception.FlightNumberExistException;

/**
 *
 * @author zares
 */
@Stateless
public class FlightSessionBean implements FlightSessionBeanRemote, FlightSessionBeanLocal {

    @PersistenceContext(unitName = "FRS-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public Long createNewFlight(Flight newFlight) throws FlightNumberExistException {
   
        em.persist(newFlight);
        em.flush();

        return newFlight.getFlightId();
        
    }
    
    @Override
    public Flight retrieveFlightByFlightNumber(Long flightNo) throws FlightNotFoundException {
        Query query = em.createQuery("SELECT f FROM Flight f WHERE f.flightnumber = :inFlightnumber");
        query.setParameter("inFlightnumber", flightNo);
        
        try
        {
            return (Flight)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new FlightNotFoundException("Flight " + flightNo + " does not exist!");
        }
    }
}
