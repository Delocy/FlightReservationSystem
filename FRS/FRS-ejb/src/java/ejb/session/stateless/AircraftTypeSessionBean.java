/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftType;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.AircraftTypeNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author xinni
 */
@Stateless
public class AircraftTypeSessionBean implements AircraftTypeSessionBeanRemote, AircraftTypeSessionBeanLocal {

    @PersistenceContext(unitName = "FRS-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public Long createNewAircraftType(AircraftType aircraftType) throws UnknownPersistenceException {
        try {
            em.persist(aircraftType);
            em.flush();
            return aircraftType.getAircraftTypeId();
        } catch (PersistenceException ex) {
            throw new UnknownPersistenceException(ex.getMessage());
        }
    }
    
    @Override
    public AircraftType retrieveAircraftTypeByAircraftTypeId(Long aircraftTypeId) throws AircraftTypeNotFoundException {
        AircraftType aircraftType = em.find(AircraftType.class, aircraftTypeId);
        if (aircraftType != null) {
            return aircraftType;
        } else {
            throw new AircraftTypeNotFoundException("Aircraft Type ID " + aircraftTypeId + " does not exist!");
        }
    }
    
    @Override
    public AircraftType retrieveAircraftTypeByAircraftTypeName(String aircraftTypeName) throws AircraftTypeNotFoundException {
        Query query = em.createQuery("SELECT a FROM AircraftType a WHERE a.aircraftTypeName = :inAircraftTypeName");
        query.setParameter("inAircraftTypeName", aircraftTypeName);
        
        try {
            return (AircraftType)query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new AircraftTypeNotFoundException("Aircraft Type Name " + aircraftTypeName + " does not exist!");
        }
    }
}
