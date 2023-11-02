/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftType;
import javax.ejb.Remote;
import util.exception.AircraftTypeNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author xinni
 */
@Remote
public interface AircraftTypeSessionBeanRemote {
    public Long createNewAircraftType(AircraftType aircraftType) throws UnknownPersistenceException;

    public AircraftType retrieveAircraftTypeByAircraftTypeId(Long aircraftTypeId) throws AircraftTypeNotFoundException;

    public AircraftType retrieveAircraftTypeByAircraftTypeName(String aircraftTypeName) throws AircraftTypeNotFoundException;
}
