/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Fare;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import entity.SeatInventory;
import java.util.Date;
import java.util.List;
import javafx.util.Pair;
import javax.ejb.Remote;
import util.enumeration.CabinClassNameEnum;
import util.exception.FlightNotFoundException;
import util.exception.FlightScheduleNotFoundException;
import util.exception.SeatInventoryNotFoundException;
import util.exception.UpdateFlightScheduleException;

/**
 *
 * @author zares
 */
@Remote
public interface FlightScheduleSessionBeanRemote {
    public FlightSchedule retrieveFlightScheduleById(Long flightScheduleID) throws FlightScheduleNotFoundException;
    
    public FlightSchedule createNewSchedule(FlightSchedule schedule, FlightSchedulePlan plan);
    
    public List<FlightSchedule> retrieveListOfFlightSchedule(String originAirport, String destAirport, Date departureDate, CabinClassNameEnum cabinClassName) throws FlightNotFoundException;
    
    public Fare lowestFare(FlightSchedule fs, CabinClassNameEnum cabinClassName) throws FlightScheduleNotFoundException;
    
    public List<Pair<FlightSchedule, FlightSchedule>> retrieveConnectingFlightSchedules(String originAirport, String destAirport, Date departureDate, CabinClassNameEnum cabinClassName) throws FlightNotFoundException;
    
    public FlightSchedule updateFlightSchedule(long flightScheduleId, Date newDepartureDateTime, double newFlightDuration) throws FlightScheduleNotFoundException, UpdateFlightScheduleException;

    public void deleteFlightSchedule(long flightScheduleId) throws FlightScheduleNotFoundException, UpdateFlightScheduleException;

    public void deleteFlightSchedule(List<FlightSchedule> flightSchedule);

    public SeatInventory getValidSeatInventory(FlightSchedule schedule, CabinClassNameEnum cabinClassType) throws FlightScheduleNotFoundException, SeatInventoryNotFoundException;

    public Fare highestFare(FlightSchedule fs, CabinClassNameEnum cabinClassName) throws FlightScheduleNotFoundException;
    
    public List<Pair<FlightSchedule, FlightSchedule>> retrieveConnectingFlightSchedulesDetach(String originAirport, String destAirport, Date departureDate, CabinClassNameEnum cabinClassName) throws FlightNotFoundException;

    public List<FlightSchedule> retrieveListOfFlightScheduleDetach(String originAirport, String destAirport, Date departureDate, CabinClassNameEnum cabinClassName) throws FlightNotFoundException;
}
