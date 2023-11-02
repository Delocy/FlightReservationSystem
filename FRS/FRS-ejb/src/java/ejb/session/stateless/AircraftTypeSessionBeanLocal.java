/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftType;
import javax.ejb.Local;
import util.exception.AircraftTypeNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author xinni
 */
@Local
public interface AircraftTypeSessionBeanLocal {

    public Long createNewAircraftType(AircraftType aircraftType) throws UnknownPersistenceException;

    public AircraftType retrieveAircraftTypeByAircraftTypeId(Long aircraftTypeId) throws AircraftTypeNotFoundException;

    public AircraftType retrieveAircraftTypeByAircraftTypeName(String aircraftTypeName) throws AircraftTypeNotFoundException;
    
}
