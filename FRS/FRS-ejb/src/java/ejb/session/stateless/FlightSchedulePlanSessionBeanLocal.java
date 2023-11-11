/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Fare;
import entity.FlightSchedulePlan;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;
import javax.ejb.Local;
import util.exception.FareExistException;
import util.exception.FlightNotFoundException;
import util.exception.FlightSchedulePlanExistException;
import util.exception.FlightSchedulePlanNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author zares
 */
@Local
public interface FlightSchedulePlanSessionBeanLocal {

    public FlightSchedulePlan retrieveFlightSchedulePlanById(Long flightSchedulePlanID) throws FlightSchedulePlanNotFoundException;

    public FlightSchedulePlan createNewFlightSchedulePlan(FlightSchedulePlan plan, List<Fare> fares, long flightID, Pair<Date, Double> info, int recurrentDays) throws FareExistException, UnknownPersistenceException, FlightSchedulePlanExistException, FlightNotFoundException;

    public FlightSchedulePlan createNewFlightSchedulePlanMultiple(FlightSchedulePlan plan, List<Fare> fares, long flightID, List<Pair<Date, Double>> info) throws FareExistException, UnknownPersistenceException, FlightNotFoundException, FlightSchedulePlanExistException;

    public FlightSchedulePlan createNewFlightSchedulePlanWeekly(FlightSchedulePlan plan, List<Fare> fares, long flightID, Pair<Date, Double> pair, int recurrent) throws FareExistException, UnknownPersistenceException, FlightNotFoundException, FlightSchedulePlanExistException;

    public void associateExistingPlanToComplementaryPlan(Long sourcFlightSchedulePlanID, Long returnFlightSchedulePlanID) throws FlightSchedulePlanNotFoundException;
    
}
