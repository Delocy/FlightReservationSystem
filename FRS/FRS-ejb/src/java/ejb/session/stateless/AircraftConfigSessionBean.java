/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftConfig;
import entity.AircraftType;
import entity.CabinClassConfig;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.AircraftConfigNotFoundException;
import util.exception.AircraftTypeNotFoundException;
import util.exception.MaxSeatCapacityExceededException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author xinni
 */
@Stateless
public class AircraftConfigSessionBean implements AircraftConfigSessionBeanRemote, AircraftConfigSessionBeanLocal {

    @EJB
    private CabinClassConfigSessionBeanLocal cabinClassConfigSessionBeanLocal;

    @EJB
    private AircraftTypeSessionBeanLocal aircraftTypeSessionBeanLocal;

    @PersistenceContext(unitName = "FRS-ejbPU")
    private EntityManager em;

    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public Long createAircraftConfig(AircraftConfig aircraftConfig, List<CabinClassConfig> cabins, Long aircraftTypeId) throws UnknownPersistenceException {
        try {
            em.persist(aircraftConfig);
            
            AircraftType type = aircraftTypeSessionBeanLocal.retrieveAircraftTypeByAircraftTypeId(aircraftTypeId);
            aircraftConfig.setAircraftType(type);
            type.getAircraftConfig().add(aircraftConfig);
            
            
            int seatCapacity = 0;
            for (CabinClassConfig c : cabins) {
                seatCapacity += c.getMaxSeatCapacity();
                cabinClassConfigSessionBeanLocal.createCabinClass(c, aircraftConfig);
            }
            
            if (seatCapacity <= type.getMaxCapacity())  {
                em.flush();
                return aircraftConfig.getAircraftConfigId();
            } else {
                throw new MaxSeatCapacityExceededException("Cabin configuration has exceeded max seating capacity!");
            }
        } catch (PersistenceException ex) {
            throw new UnknownPersistenceException(ex.getMessage());
        } catch (AircraftTypeNotFoundException ex) {
            Logger.getLogger(AircraftConfigSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(AircraftConfigSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public AircraftConfig retrieveAircraftConfigById(Long id) throws AircraftConfigNotFoundException {
        AircraftConfig aircraftConfig = em.find(AircraftConfig.class, id);
        if (aircraftConfig != null) {
            return aircraftConfig;
        } else {
            throw new AircraftConfigNotFoundException("Aircraft Configuration " + id + " does not exist!");
        }
    }
    
    @Override
    public List<AircraftConfig> retrieveAllAircraftConfig() throws AircraftConfigNotFoundException {
        Query query = em.createQuery("SELECT a FROM AircraftConfig a ORDER BY a.aircraftType ASC, a.aircraftConfigName ASC");
        
        List<AircraftConfig> aircraftConfigs = query.getResultList();
        
        if (!aircraftConfigs.isEmpty()) {
            return aircraftConfigs;
        } else {
            throw new AircraftConfigNotFoundException("No aircraft configurations found!");
        }
    }
}
