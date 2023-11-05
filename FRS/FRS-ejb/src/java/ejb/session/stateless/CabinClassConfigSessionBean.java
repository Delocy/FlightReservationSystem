/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftConfig;
import entity.CabinClassConfig;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import util.enumeration.CabinClassNameEnum;
import util.exception.CabinClassNameNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author xinni
 */
@Stateless
public class CabinClassConfigSessionBean implements CabinClassConfigSessionBeanRemote, CabinClassConfigSessionBeanLocal {

    @PersistenceContext(unitName = "FRS-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public Long createCabinClass(CabinClassConfig c, AircraftConfig aircraftConfig) throws UnknownPersistenceException {
        try {
            em.persist(c);
            c.setAircraftConfig(aircraftConfig);
            //em.flush();
            return c.getCabinClassConfigId();
        } catch (PersistenceException ex) {
            throw new UnknownPersistenceException(ex.getMessage());
        }
    }
    
    
    @Override
    public CabinClassNameEnum fetchCabinClassNameEnum(String s) throws CabinClassNameNotFoundException {
        if (s.equalsIgnoreCase("F")) {
            return CabinClassNameEnum.F;
        } else if (s.equalsIgnoreCase("J")) {
            return CabinClassNameEnum.J;
        } else if (s.equalsIgnoreCase("W")) {
            return CabinClassNameEnum.W;
        } else if (s.equalsIgnoreCase("Y")) {
            return CabinClassNameEnum.Y;
        } else {
            throw new CabinClassNameNotFoundException("Cabin class name " + s + " does not exist!");
        }
            
    }
}
