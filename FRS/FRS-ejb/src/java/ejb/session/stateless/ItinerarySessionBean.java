/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Fare;
import entity.FlightReservation;
import entity.Itinerary;
import entity.Person;
import entity.SeatInventory;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.CustomerNotFoundException;
import util.exception.ItineraryExistException;
import util.exception.ItineraryNotFoundException;
import util.exception.PersonNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author xinni
 */
@Stateless
public class ItinerarySessionBean implements ItinerarySessionBeanRemote, ItinerarySessionBeanLocal {

    @EJB
    private PersonSessionBeanLocal personSessionBeanLocal;

//    @EJB
//    private CustomerSessionBeanLocal customerSessionBeanLocal;

    
    @PersistenceContext(unitName = "FRS-ejbPU")
    private EntityManager em;

    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Itinerary createNewItinerary(Itinerary itinerary, long custId) throws UnknownPersistenceException, PersonNotFoundException, ItineraryExistException {
//        Customer cust = customerSessionBeanLocal.retrieveCustomerByCustomerId(custId);
        Person person = personSessionBeanLocal.retrievePersonById(custId);
        
        try {
            em.persist(itinerary);

            itinerary.setPerson(person);
            person.getItineraries().add(itinerary);

            em.flush();
            return itinerary;
        } catch (PersistenceException ex) { 
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new ItineraryExistException("Itinerary already exists");
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    
    }
    
    @Override
    public Itinerary createNewItineraryDetached(Itinerary itinerary, long custId) throws UnknownPersistenceException, PersonNotFoundException, ItineraryExistException {
//        Customer cust = customerSessionBeanLocal.retrieveCustomerByCustomerId(custId);
        Person person = personSessionBeanLocal.retrievePersonById(custId);
        
        try {
            em.persist(itinerary);

            itinerary.setPerson(person);
            person.getItineraries().add(itinerary);

            em.flush();
            
            
            em.detach(itinerary);
            for (FlightReservation res : itinerary.getReservations()) {
                em.detach(res);
            }
            return itinerary;
        } catch (PersistenceException ex) { 
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new ItineraryExistException("Itinerary already exists");
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    
    }

    
    @Override
    public Itinerary retrieveItineraryByID(long itineraryId) throws ItineraryNotFoundException {
        Itinerary itinerary = em.find(Itinerary.class, itineraryId);
        if (itinerary == null) {
            throw new ItineraryNotFoundException("Itinerary does not exist");
        } else {
            return itinerary;
        }
    }
    
    @Override
    public Itinerary retrieveItineraryByIDDetached(long itineraryId) throws ItineraryNotFoundException {
        Itinerary itinerary = em.find(Itinerary.class, itineraryId);
        if (itinerary == null) {
            throw new ItineraryNotFoundException("Itinerary does not exist");
        } else {
//            em.detach(itinerary);
//            if (!itinerary.getReservations().isEmpty()) {
//                for (FlightReservation res : itinerary.getReservations()) {
//                    em.detach(res);
//                }
//            }
            em.detach(itinerary);
            em.detach(itinerary.getPerson());
            if (!itinerary.getReservations().isEmpty()) {
                for (FlightReservation res : itinerary.getReservations()) {
                    em.detach(res);
                    em.detach(res.getFlightSchedule());
                    em.detach(res.getFlightSchedule().getFlightSchedulePlan());
                    em.detach(res.getFlightSchedule().getFlightSchedulePlan().getFlight());
                    em.detach(res.getFlightSchedule().getFlightSchedulePlan().getFlight().getFlightRoute());
                    for (Fare fare : res.getFlightSchedule().getFlightSchedulePlan().getFares()) {
                        em.detach(fare);
                    }
                    
                    for (SeatInventory seatInventory : res.getFlightSchedule().getSeatInventory()) {
                        em.detach(seatInventory.getCabinClass());
                        em.detach(seatInventory);
                    }
                }
            }
            return itinerary;
        }
    }
    
    @Override
    public List<Itinerary> retrieveItinerariesByPersonId(Long personId) {
        Query query = em.createQuery("SELECT r FROM Itinerary r WHERE r.person.personId = :id");
        query.setParameter("id", personId);
        
        return query.getResultList();
    }
    
    @Override
    public List<Itinerary> retrieveItinerariesByPersonIdDetached(Long personId) {
        Query query = em.createQuery("SELECT r FROM Itinerary r WHERE r.person.personId = :id");
        query.setParameter("id", personId);
        
        List<Itinerary> list =  query.getResultList();
        for (Itinerary itinerary: list) {
            em.detach(itinerary);
            em.detach(itinerary.getPerson());
            if (!itinerary.getReservations().isEmpty()) {
                for (FlightReservation res : itinerary.getReservations()) {
                    em.detach(res);
                    em.detach(res.getFlightSchedule());
                    em.detach(res.getFlightSchedule().getFlightSchedulePlan());
                    em.detach(res.getFlightSchedule().getFlightSchedulePlan().getFlight());
                    em.detach(res.getFlightSchedule().getFlightSchedulePlan().getFlight().getFlightRoute());
                    
                    for (Fare fare : res.getFlightSchedule().getFlightSchedulePlan().getFares()) {
                        em.detach(fare);
                    }
                    
                    for (SeatInventory seatInventory : res.getFlightSchedule().getSeatInventory()) {
                        em.detach(seatInventory.getCabinClass());
                        em.detach(seatInventory);
                    }
                }
            }
        }
        return list;
    }
    
    
    
}
