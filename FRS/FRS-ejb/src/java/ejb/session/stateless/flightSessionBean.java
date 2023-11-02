/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Employee;
import entity.Flight;
import entity.FlightRoute;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
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
    
//    @Override
//    public Flight retrieveFlightByFlightID(Long flightId) {
//        return em.find(Flight.class, flightId);
//    }
//    
//    @Override
//    public List<Flight> viewAllFlights() {
//        TypedQuery<Flight> query = em.createQuery("SELECT f FROM Flight f ORDER BY f.flightNumber ASC", Flight.class);
//        List<Flight> allFlights = query.getResultList();
//        
//        List<Flight> orderedFlights = new ArrayList<>();
//        
//        for (Flight flight : allFlights) {
//           if (!orderedFlights.contains(flight)) {
//               FlightRoute flightRoute = flight.getFlightRoute();
//               if (flightRoute.isHasComplementaryReturnRoute()) {
//                   Flight complementaryReturnFlight = flightRoute.getComplementaryReturnFlight();
//               }
//               
//           }
//        }
//    }
    
}
