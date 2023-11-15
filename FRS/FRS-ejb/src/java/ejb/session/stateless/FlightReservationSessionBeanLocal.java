/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.FlightReservation;
import entity.Passenger;
import java.util.List;
import javax.ejb.Local;
import util.exception.FlightReservationExistException;
import util.exception.FlightReservationNotFoundException;
import util.exception.FlightScheduleNotFoundException;
import util.exception.ItineraryNotFoundException;
import util.exception.SeatInventoryNotFoundException;
import util.exception.SeatsBookedException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author xinni
 */
@Local
public interface FlightReservationSessionBeanLocal {

    public FlightReservation retrieveFlightReservationByReserverationId(long id) throws FlightReservationNotFoundException;

    public long createNewReservation(FlightReservation reservation, List<Passenger> passengers, long flightScheduleId, long itineraryId) throws FlightReservationExistException, UnknownPersistenceException, FlightScheduleNotFoundException, SeatInventoryNotFoundException, SeatsBookedException, ItineraryNotFoundException;
    
}
