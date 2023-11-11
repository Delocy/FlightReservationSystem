/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Fare;
import entity.FlightSchedulePlan;
import javax.ejb.Remote;
import util.exception.FareExistException;
import util.exception.FareNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author zares
 */
@Remote
public interface FareSessionBeanRemote {
    
    public Fare createFare(Fare fare, FlightSchedulePlan plan) throws FareExistException, UnknownPersistenceException;
    
    public Fare retrieveFareByFareId(Long fareID) throws FareNotFoundException;
}
