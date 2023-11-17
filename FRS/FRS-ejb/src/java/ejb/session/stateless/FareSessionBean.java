/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Fare;
import entity.FlightSchedulePlan;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import util.exception.FareExistException;
import util.exception.FareNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateFareException;

/**
 *
 * @author zares
 */
@Stateless
public class FareSessionBean implements FareSessionBeanRemote, FareSessionBeanLocal {

    @PersistenceContext(unitName = "FRS-ejbPU")
    private EntityManager em;
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    public Fare createFare(Fare fare, FlightSchedulePlan plan) throws FareExistException, UnknownPersistenceException {
        try {
            em.persist(fare);

            plan.getFares().add(fare);
            fare.setFlightSchedulePlan(plan);
            
            em.flush();
            return fare;
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new FareExistException("There is an existing fare basis code");
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        }
    
    @Override
    public Fare retrieveFareByFareId(Long fareID) throws FareNotFoundException {
        Fare fare = em.find(Fare.class, fareID);
        if (fare != null) {
            return fare;
        } else {
            throw new FareNotFoundException("Fare " + fareID + " not found!");
        }
    }
    
    @Override
    public Fare updateFare(long fareID, BigDecimal newCost) throws FareNotFoundException, UpdateFareException {
        try {
            Fare fare = retrieveFareByFareId(fareID);
            fare.setFare(newCost);
            em.flush();
            return fare;
        } catch (PersistenceException ex) {
            throw new UpdateFareException("Invalid new cost input");
        }
    }
    
    @Override
    public void deleteFares(List<Fare> fares) {
        
        for(Fare fare : fares) {
            try {
                Long id = fare.getFareId();
                em.remove(retrieveFareByFareId(id));
            } catch (FareNotFoundException ex) {
                Logger.getLogger(FareSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

