/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.AircraftConfig;
import entity.CabinClassConfig;
import java.util.List;
import javax.ejb.Remote;
import util.exception.AircraftConfigNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author xinni
 */
@Remote
public interface AircraftConfigSessionBeanRemote {
    public Long createAircraftConfig(AircraftConfig aircraftConfig, List<CabinClassConfig> cabins, Long aircraftTypeId) throws UnknownPersistenceException;
    public List<AircraftConfig> retrieveAllAircraftConfig() throws AircraftConfigNotFoundException;
    public AircraftConfig retrieveAircraftConfigById(Long id) throws AircraftConfigNotFoundException;
}
