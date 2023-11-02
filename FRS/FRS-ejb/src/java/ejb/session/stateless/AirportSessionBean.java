/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Airport;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import util.exception.AirportNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author xinni
 */
@Stateless
public class AirportSessionBean implements AirportSessionBeanRemote, AirportSessionBeanLocal {

    @PersistenceContext(unitName = "FRS-ejbPU")
    private EntityManager em;


    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    public Long createNewAirport(Airport airport) throws UnknownPersistenceException {
        try {
            em.persist(airport);
            em.flush();
            return airport.getAirportId();
        } catch (PersistenceException ex) {
            throw new UnknownPersistenceException(ex.getMessage());
        }
    }
    
    public Airport retrieveAirportByAirportId(Long airportID) throws AirportNotFoundException { 
        Airport airport = em.find(Airport.class, airportID);
        if (airport != null) {
            return airport;
        } else {
            throw new AirportNotFoundException("Airport ID " + airportID + " does not exist!");
        }
    }
    
    public Airport retrieveAirportByAirportCode(String airportCode) throws AirportNotFoundException {
        TypedQuery<Airport> query = em.createQuery("SELECT a FROM Airport a WHERE a.airportCode = :inAirportCode", Airport.class);
        query.setParameter("inAirportCode", airportCode);
        
        try {
            return query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new AirportNotFoundException("Airport code " + airportCode + " does not exist!");
        }
    }
}
