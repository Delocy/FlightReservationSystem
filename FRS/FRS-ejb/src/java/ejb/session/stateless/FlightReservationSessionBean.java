/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.FlightReservation;
import entity.FlightSchedule;
import entity.Itinerary;
import entity.Passenger;
import entity.SeatInventory;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;
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
@Stateless
public class FlightReservationSessionBean implements FlightReservationSessionBeanRemote, FlightReservationSessionBeanLocal {

    @EJB
    private SeatInventorySessionBeanLocal seatInventorySessionBeanLocal;

    @EJB
    private ItinerarySessionBeanLocal itinerarySessionBeanLocal;

    @EJB
    private FlightScheduleSessionBeanLocal flightScheduleSessionBeanLocal;

    @PersistenceContext(unitName = "FRS-ejbPU")
    private EntityManager em;
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public long createNewReservation(FlightReservation reservation, List<Passenger> passengers, long flightScheduleId, long itineraryId) throws FlightReservationExistException, UnknownPersistenceException, FlightScheduleNotFoundException, SeatInventoryNotFoundException, SeatsBookedException, ItineraryNotFoundException {
     
        try {
            FlightSchedule flightSchedule = flightScheduleSessionBeanLocal.retrieveFlightScheduleById(flightScheduleId);
            Itinerary itinerary = itinerarySessionBeanLocal.retrieveItineraryByID(itineraryId);

            SeatInventory seat = null;
            for (SeatInventory seats: flightSchedule.getSeatInventory()) {
                if (seats.getCabinClass().getCabinClassName()== reservation.getCabinClassName()) {
                    seat = seats;
                }
            } 
            if (seat == null) {
                throw new SeatInventoryNotFoundException("Seat Inventory for specified cabin class not found");
            }

            em.persist(reservation);

            for (Passenger passenger: passengers) {
                em.persist(passenger);
                reservation.getPassengers().add(passenger);
                seatInventorySessionBeanLocal.bookSeat(seat.getSeatInventoryId(), passenger.getSeatNumber());
            }

            flightSchedule.getReservations().add(reservation);
            reservation.setFlightSchedule(flightSchedule);

            reservation.setItinerary(itinerary);
            itinerary.getReservations().add(reservation);

            em.flush();

            return reservation.getFlightReservationId();   
        } catch (PersistenceException ex) {              
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                     throw new FlightReservationExistException("Flight Reservation already exist");
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }                          
        }
    }
    
    @Override
    public long createNewReservationDetached(FlightReservation reservation, List<Passenger> passengers, long flightScheduleId, long itineraryId) throws FlightReservationExistException, UnknownPersistenceException, FlightScheduleNotFoundException, SeatInventoryNotFoundException, SeatsBookedException, ItineraryNotFoundException {
     
        try {
            FlightSchedule flightSchedule = flightScheduleSessionBeanLocal.retrieveFlightScheduleById(flightScheduleId);
            Itinerary itinerary = itinerarySessionBeanLocal.retrieveItineraryByID(itineraryId);

            SeatInventory seat = null;
            for (SeatInventory seats: flightSchedule.getSeatInventory()) {
                if (seats.getCabinClass().getCabinClassName()== reservation.getCabinClassName()) {
                    seat = seats;
                }
            } 
            if (seat == null) {
                throw new SeatInventoryNotFoundException("Seat Inventory for specified cabin class not found");
            }

            em.persist(reservation);

            for (Passenger passenger: passengers) {
                em.persist(passenger);
                reservation.getPassengers().add(passenger);
                seatInventorySessionBeanLocal.bookSeat(seat.getSeatInventoryId(), passenger.getSeatNumber());
            }

            flightSchedule.getReservations().add(reservation);
            reservation.setFlightSchedule(flightSchedule);

            reservation.setItinerary(itinerary);
            itinerary.getReservations().add(reservation);

            em.flush();
            
            em.detach(reservation);

            return reservation.getFlightReservationId();   
        } catch (PersistenceException ex) {              
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                     throw new FlightReservationExistException("Flight Reservation already exist");
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }                          
        }
    }
    
    @Override
    public FlightReservation retrieveFlightReservationByReserverationId(long id) throws FlightReservationNotFoundException {
        FlightReservation reservation = em.find(FlightReservation.class, id);
        
        if(reservation != null) {
            return reservation;
        } else {
            throw new FlightReservationNotFoundException("Reservation does not exist!");
        }
    } 
        
} 
   
    
      

