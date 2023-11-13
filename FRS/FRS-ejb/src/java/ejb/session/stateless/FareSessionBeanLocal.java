/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Fare;
import entity.FlightSchedulePlan;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Local;
import util.exception.FareExistException;
import util.exception.FareNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateFareException;

/**
 *
 * @author zares
 */
@Local
public interface FareSessionBeanLocal {

    public Fare createFare(Fare fare, FlightSchedulePlan plan) throws FareExistException, UnknownPersistenceException;

    public Fare retrieveFareByFareId(Long fareID) throws FareNotFoundException;

    public Fare updateFare(long fareID, BigDecimal newCost) throws FareNotFoundException, UpdateFareException;

    public void deleteFares(List<Fare> fares);
    
}
