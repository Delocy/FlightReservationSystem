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
                throw MaxSeatCapacityExceededException("Cabin configuration has exceeded max seating capacity!");
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

    private Exception MaxSeatCapacityExceededException(String cabin_configuration_has_exceeded_max_seat) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
