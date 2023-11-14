/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Itinerary;
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
import util.exception.UnknownPersistenceException;

/**
 *
 * @author xinni
 */
@Stateless
public class ItinerarySessionBean implements ItinerarySessionBeanRemote, ItinerarySessionBeanLocal {

    @EJB
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @PersistenceContext(unitName = "FRS-ejbPU")
    private EntityManager em;

    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public Itinerary createNewItinerary(Itinerary itinerary, long custId) throws UnknownPersistenceException, CustomerNotFoundException, ItineraryExistException {
        Customer cust = customerSessionBeanLocal.retrieveCustomerByCustomerId(custId);
        
        try {
            em.persist(itinerary);

            itinerary.setCustomer(cust);
            cust.getItineraries().add(itinerary);

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

    
    public Itinerary retrieveItineraryByID(long itineraryId) throws ItineraryNotFoundException {
        Itinerary itinerary = em.find(Itinerary.class, itineraryId);
        if (itinerary == null) {
            throw new ItineraryNotFoundException("Itinerary does not exist");
        } else {
            return itinerary;
        }
    }
    
    public List<Itinerary> retrieveItinerariesByCustomerId(Long customerId) {
        Query query = em.createQuery("SELECT r FROM Itinerary r WHERE r.customer.customerId = :id");
        query.setParameter("id", customerId);
        
        return query.getResultList();
    }
    
    
    
    
}
