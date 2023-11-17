/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Fare;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import entity.Flight;
import entity.FlightReservation;
import entity.SeatInventory;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import entity.SeatInventory;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.CabinClassNameEnum;
import util.enumeration.ScheduleTypeEnum;
import util.exception.FlightNotFoundException;
import util.exception.FlightScheduleNotFoundException;
import util.exception.SeatInventoryNotFoundException;
import util.exception.UpdateFlightScheduleException;

/**
 *
 * @author zares
 */
@Stateless
public class FlightScheduleSessionBean implements FlightScheduleSessionBeanRemote, FlightScheduleSessionBeanLocal {

    @EJB
    private FlightSessionBeanLocal flightSessionBeanLocal;

    @EJB
    private SeatInventorySessionBeanLocal seatInventorySessionBeanLocal;

    @PersistenceContext(unitName = "FRS-ejbPU")
    private EntityManager em;

    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public FlightSchedule createNewSchedule(FlightSchedule schedule, FlightSchedulePlan plan) {
        em.persist(schedule);

        //need remove next time cuz i lazy delete table again
        ScheduleTypeEnum scheduleType = plan.getScheduleType();
        schedule.setScheduleType(scheduleType);
        
        // original
        plan.getFlightSchedule().add(schedule);

        schedule.setScheduleType(scheduleType);
        schedule.setFlightSchedulePlan(plan);

        return schedule;
    }
    
    @Override
    public FlightSchedule retrieveFlightScheduleById(Long flightScheduleID) throws FlightScheduleNotFoundException {
        FlightSchedule schedule = em.find(FlightSchedule.class, flightScheduleID);
        
        if(schedule != null) {
            return schedule;
        } else {
            throw new FlightScheduleNotFoundException("Flight Schedule " + flightScheduleID + " not found!");      
        }
        
    }
    
    @Override
    public FlightSchedule retrieveFlightScheduleByIdDetached(Long flightScheduleID) throws FlightScheduleNotFoundException {
        FlightSchedule schedule = em.find(FlightSchedule.class, flightScheduleID);
        FlightSchedulePlan plan = schedule.getFlightSchedulePlan();
        List<SeatInventory> seats = schedule.getSeatInventory();
        List<FlightReservation> reservations = schedule.getReservations();
        
        if(schedule != null) {
            em.detach(schedule);
            em.detach(plan);
            for (SeatInventory seat : seats) {
                em.detach(seats);
            }
            for (FlightReservation res : reservations) {
                em.detach(res);
            }
            return schedule;
        } else {
            throw new FlightScheduleNotFoundException("Flight Schedule " + flightScheduleID + " not found!");      
        }
        
    }
    
    @Override
    public List<FlightSchedule> retrieveListOfFlightSchedule(String originAirport, String destAirport, Date departureDate, CabinClassNameEnum cabinClassName) throws FlightNotFoundException {
        List<FlightSchedule> schedules;
        schedules = new ArrayList<>();
        List<Flight> flight = flightSessionBeanLocal.retrieveFlightsByFlightRoute(originAirport, destAirport);
        for (Flight f : flight) {
            for (FlightSchedulePlan fsp : f.getFlightSchedulePlan()) {
                if (fsp.isDisabled()) {
                    continue;
                }
                
                for (FlightSchedule fs : fsp.getFlightSchedule()) {
                    boolean include = false;
                    if (cabinClassName == null) {
                        include = true;
                    } else {
                        for (SeatInventory seatInventory: fs.getSeatInventory()) {
                            if (seatInventory.getCabinClass().getCabinClassName().equals(cabinClassName)) {
                                include = true;
                            }
                        }
                    }
                    Calendar c1 = Calendar.getInstance();
                    Calendar c2 = Calendar.getInstance();
                    c1.setTime(fs.getDepartureDateTime());
                    c2.setTime(departureDate);
                    if (c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR) && c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) {
                        include = true;
                    } else {
                        include = false;
                    }
                    
                    if (include) {
                        schedules.add(fs);
                    }
                }
            }
        } 
           
                
        Collections.sort(schedules, new Comparator<FlightSchedule>() {
            @Override
            public int compare(FlightSchedule f1, FlightSchedule f2) {
                return f1.getDepartureDateTime().compareTo(f2.getDepartureDateTime());
            }
        });
        return schedules;
           
    }
    
    @Override
    public List<FlightSchedule> retrieveListOfFlightScheduleDetach(String originAirport, String destAirport, Date departureDate, CabinClassNameEnum cabinClassName) throws FlightNotFoundException {
        List<FlightSchedule> schedules;
        schedules = new ArrayList<>();
        List<Flight> flight = flightSessionBeanLocal.retrieveFlightsByFlightRoute(originAirport, destAirport);
        for (Flight f : flight) {
            for (FlightSchedulePlan fsp : f.getFlightSchedulePlan()) {
                if (fsp.isDisabled()) {
                    continue;
                }
                
                for (FlightSchedule fs : fsp.getFlightSchedule()) {
                    boolean include = false;
                    if (cabinClassName == null) {
                        include = true;
                    } else {
                        for (SeatInventory seatInventory: fs.getSeatInventory()) {
                            if (seatInventory.getCabinClass().getCabinClassName().equals(cabinClassName)) {
                                include = true;
                            }
                        }
                    }
                    Calendar c1 = Calendar.getInstance();
                    Calendar c2 = Calendar.getInstance();
                    c1.setTime(fs.getDepartureDateTime());
                    c2.setTime(departureDate);
                    if (c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR) && c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) {
                        include = true;
                    } else {
                        include = false;
                    }
                    
                    if (include) {
                        em.detach(fs);
                        em.detach(fs.getFlightSchedulePlan());
                        List<FlightReservation> reservations = fs.getReservations();
                        for (FlightReservation res : reservations) {
                            em.detach(res);
                        }
                        List<SeatInventory> seats = fs.getSeatInventory();
                        for (SeatInventory seat : seats) {
                            em.detach(seat);
                        }
                        schedules.add(fs);
                    }
                }
            }
        }
           
                
        Collections.sort(schedules, new Comparator<FlightSchedule>() {
            @Override
            public int compare(FlightSchedule f1, FlightSchedule f2) {
                return f1.getDepartureDateTime().compareTo(f2.getDepartureDateTime());
            }
        });
        return schedules;
           
    }
    
    @Override
    public List<Pair<FlightSchedule, FlightSchedule>> retrieveConnectingFlightSchedules(String originAirport, String destAirport, Date departureDate, CabinClassNameEnum cabinClassName) throws FlightNotFoundException {
            
        List<Pair<FlightSchedule, FlightSchedule>> schedules = new ArrayList<>();
        List<Flight[]> flights = flightSessionBeanLocal.retrieveConnectingFlightsByFlightRoute(originAirport, destAirport);

        for (Object[] cf : flights) {
            Flight f1 = (Flight) cf[0];
            Flight f2 = (Flight) cf[1];
            for (FlightSchedulePlan fsp: f1.getFlightSchedulePlan()) {
                if (fsp.isDisabled() == true) {
                    continue;
                }
                for (FlightSchedule fs: fsp.getFlightSchedule()) {
                    for (FlightSchedulePlan fsp2: f2.getFlightSchedulePlan()) {
                        if (fsp2.isDisabled()) {
                            continue;
                        }
                        for (FlightSchedule fs2 : fsp2.getFlightSchedule()) {
                            boolean include = false;
                            if (cabinClassName == null) {
                                include = true;
                            } else {
                                for (SeatInventory seats: fs.getSeatInventory()) {
                                    for (SeatInventory seats2: fs2.getSeatInventory()) {
                                        if (seats.getCabinClass().getCabinClassName().equals(cabinClassName) && seats2.getCabinClass().getCabinClassName().equals(cabinClassName)) {
                                        include = true;
                                        }
                                    }                           
                                }
                            }

                            Calendar c1 = Calendar.getInstance();
                            Calendar c2 = Calendar.getInstance();
                            c1.setTime(fs.getDepartureDateTime());
                            c2.setTime(departureDate);

                            if (c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR) &&
                                              c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)){
                                include = true;
                            } else {
                                include = false;
                            }

                            Calendar c3 = Calendar.getInstance();
                            c3.setTime(fs.getDepartureDateTime());
                            double duration = fs.getFlightDuration();
                            int hour = (int) duration;
                            int min = (int) (duration % 1 * 60);
                            c3.add(Calendar.HOUR_OF_DAY, hour);
                            c3.add(Calendar.MINUTE, min);               
                            int diff1 = fs.getFlightSchedulePlan().getFlight().getFlightRoute().getDestinationAirport().getGmt() - 
                            fs.getFlightSchedulePlan().getFlight().getFlightRoute().getOriginAirport().getGmt();
                            c3.add(Calendar.HOUR_OF_DAY, diff1);

                            Calendar c4 = Calendar.getInstance();
                            c4.setTime(fs2.getDepartureDateTime());
                            long layover = Duration.between(c3.toInstant(), c4.toInstant()).toHours();
                            if (layover < 2l || layover > 12l) {
                                include = false;
                            }


                            if (include) {
                                schedules.add(new Pair(fs, fs2));
                            }

                        }
                    }
                }
            }
        }

        Collections.sort(schedules, new Comparator<Pair<FlightSchedule, FlightSchedule>>() {
            @Override
            public int compare(Pair<FlightSchedule, FlightSchedule> p1, Pair<FlightSchedule, FlightSchedule> p2) {
                return p1.getKey().getDepartureDateTime().compareTo(p2.getKey().getDepartureDateTime());
            }
        });
        return schedules;
    }
    
    @Override
    public List<Pair<FlightSchedule, FlightSchedule>> retrieveConnectingFlightSchedulesDetach(String originAirport, String destAirport, Date departureDate, CabinClassNameEnum cabinClassName) throws FlightNotFoundException {
            
        List<Pair<FlightSchedule, FlightSchedule>> schedules = new ArrayList<>();
        List<Flight[]> flights = flightSessionBeanLocal.retrieveConnectingFlightsByFlightRoute(originAirport, destAirport);

        for (Object[] cf : flights) {
            Flight f1 = (Flight) cf[0];
            Flight f2 = (Flight) cf[1];
            for (FlightSchedulePlan fsp: f1.getFlightSchedulePlan()) {
                if (fsp.isDisabled() == true) {
                    continue;
                }
                for (FlightSchedule fs: fsp.getFlightSchedule()) {
                    for (FlightSchedulePlan fsp2: f2.getFlightSchedulePlan()) {
                        if (fsp2.isDisabled()) {
                            continue;
                        }
                        for (FlightSchedule fs2 : fsp2.getFlightSchedule()) {
                            boolean include = false;
                            if (cabinClassName == null) {
                                include = true;
                            } else {
                                for (SeatInventory seats: fs.getSeatInventory()) {
                                    for (SeatInventory seats2: fs2.getSeatInventory()) {
                                        if (seats.getCabinClass().getCabinClassName().equals(cabinClassName) && seats2.getCabinClass().getCabinClassName().equals(cabinClassName)) {
                                        include = true;
                                        }
                                    }                           
                                }
                            }

                            Calendar c1 = Calendar.getInstance();
                            Calendar c2 = Calendar.getInstance();
                            c1.setTime(fs.getDepartureDateTime());
                            c2.setTime(departureDate);

                            if (c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR) &&
                                              c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)){
                                include = true;
                            } else {
                                include = false;
                            }

                            System.out.println(include + " HERE");
                            Calendar c3 = Calendar.getInstance();
                            c3.setTime(fs.getDepartureDateTime());
                            double duration = fs.getFlightDuration();
                            int hour = (int) duration;
                            int min = (int) (duration % 1 * 60);
                            c3.add(Calendar.HOUR_OF_DAY, hour);
                            c3.add(Calendar.MINUTE, min);               
                            int diff1 = fs.getFlightSchedulePlan().getFlight().getFlightRoute().getDestinationAirport().getGmt() - 
                            fs.getFlightSchedulePlan().getFlight().getFlightRoute().getOriginAirport().getGmt();
                            c3.add(Calendar.HOUR_OF_DAY, diff1);

                            Calendar c4 = Calendar.getInstance();
                            c4.setTime(fs2.getDepartureDateTime());
                            long layover = Duration.between(c3.toInstant(), c4.toInstant()).toHours();
                            if (layover < 2l || layover > 12l) {
                                include = false;
                            }


                            if (include) {
                                em.detach(fs);
                                em.detach(fs.getFlightSchedulePlan());
                                List<FlightReservation> reservations = fs.getReservations();
                                for (FlightReservation res : reservations) {
                                    em.detach(res);
                                }
                                List<SeatInventory> seats = fs.getSeatInventory();
                                for (SeatInventory seat : seats) {
                                    em.detach(seat);
                                }
                                
                                em.detach(fs2);
                                em.detach(fs2.getFlightSchedulePlan());
                                List<FlightReservation> r2 = fs2.getReservations();
                                for (FlightReservation res : r2) {
                                    em.detach(res);
                                }
                                List<SeatInventory> seats2 = fs2.getSeatInventory();
                                for (SeatInventory seat : seats2) {
                                    em.detach(seat);
                                }
                                schedules.add(new Pair(fs, fs2));
                            }

                        }
                    }
                }
            }
        }
        
        Collections.sort(schedules, new Comparator<Pair<FlightSchedule, FlightSchedule>>() {
            @Override
            public int compare(Pair<FlightSchedule, FlightSchedule> p1, Pair<FlightSchedule, FlightSchedule> p2) {
                return p1.getKey().getDepartureDateTime().compareTo(p2.getKey().getDepartureDateTime());
            }
        });
        return schedules;
    }
    
    @Override
    public Fare lowestFare(FlightSchedule fs, CabinClassNameEnum cabinClassName) throws FlightScheduleNotFoundException {
        FlightSchedule schedule = retrieveFlightScheduleById(fs.getFlightScheduleId());
        List<Fare> fares = schedule.getFlightSchedulePlan().getFares();
        
        Fare lowest = fares.get(0);
        
        for (Fare f : fares) {
            if (f.getCabinClassName().equals(cabinClassName)) {
                if (lowest == null) {
                    lowest = f;
                    continue;
                } else if (f.getFare().compareTo(lowest.getFare()) < 0) {
                    lowest = f;
                }
            }
            
           
        }
        
        return lowest;
    }
    
    @Override
    public Fare highestFare(FlightSchedule fs, CabinClassNameEnum cabinClassName) throws FlightScheduleNotFoundException {
        FlightSchedule schedule = retrieveFlightScheduleById(fs.getFlightScheduleId());
        List<Fare> fares = schedule.getFlightSchedulePlan().getFares();
        
        Fare highest = fares.get(0);
        
        for (Fare f : fares) {
            if (f.getCabinClassName().equals(cabinClassName)) {
                if (highest == null) {
                    highest = f;
                    continue;
                } else if (f.getFare().compareTo(highest.getFare()) > 0) {
                    highest = f;
                }
            }
            
           
        }
        em.detach(highest);
        return highest;
    }

    @Override
    public FlightSchedule updateFlightSchedule(long flightScheduleId, Date newDepartureDateTime, double newFlightDuration) throws FlightScheduleNotFoundException, UpdateFlightScheduleException {
        FlightSchedule flightSchedule = retrieveFlightScheduleById(flightScheduleId);
    
         // Check no overlaps with already existing flight plans associated with the flight
        for (FlightSchedulePlan fsp: flightSchedule.getFlightSchedulePlan().getFlight().getFlightSchedulePlan()) {
            for (FlightSchedule fs: fsp.getFlightSchedule()) {
                if (fs.getFlightScheduleId() == flightScheduleId) {
                    continue;
                }
                Date startA = fs.getDepartureDateTime();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(startA);
                double duration = fs.getFlightDuration();
                int hour = (int) duration;
                int min = (int) (duration % 1 * 60);
                calendar.add(Calendar.HOUR_OF_DAY, hour);
                calendar.add(Calendar.MINUTE, min);
                Date endA = calendar.getTime();
            
                Calendar c = Calendar.getInstance();
                c.setTime(newDepartureDateTime);
                double durationB = newFlightDuration;
                int hourB = (int) durationB;
                int minB = (int) (duration % 1 * 60);
                c.add(Calendar.HOUR_OF_DAY, hourB);
                c.add(Calendar.MINUTE, minB);
                Date endB = c.getTime();
                    
                if (startA.before(endB) && newDepartureDateTime.before(endA)) {
                    //System.out.println("calling one");
                    throw new UpdateFlightScheduleException("New fight schedule conflicts with existing flight schedules");
                }   
            }
        }
        
        flightSchedule.setDepartureDateTime(newDepartureDateTime);
        flightSchedule.setFlightDuration(newFlightDuration);
        em.flush();
        return flightSchedule;
    }
    
    @Override
    public void deleteFlightSchedule(long flightScheduleId)  throws FlightScheduleNotFoundException, UpdateFlightScheduleException  {
        FlightSchedule flightSchedule = retrieveFlightScheduleById(flightScheduleId);
        if (!flightSchedule.getReservations().isEmpty()) {
            throw new UpdateFlightScheduleException("Reservations has already been made for this flight schedule, unable to delete");
        } else {
            flightSchedule.getFlightSchedulePlan().getFlightSchedule().remove(flightSchedule);
            for (SeatInventory seats: flightSchedule.getSeatInventory()) {    
                em.remove(seats);
            }
            em.remove(flightSchedule);
        }
    }
    
    @Override
    public void deleteFlightSchedule(List<FlightSchedule> flightSchedule) {
       
        for(FlightSchedule schedule : flightSchedule) {           
           seatInventorySessionBeanLocal.deleteSeatInventory(schedule.getSeatInventory()); 
           em.remove(schedule);
       }
    }
    
    @Override
    public SeatInventory getValidSeatInventory(FlightSchedule schedule, CabinClassNameEnum cabinClassType) throws FlightScheduleNotFoundException, SeatInventoryNotFoundException {
        FlightSchedule latestSchedule = retrieveFlightScheduleById(schedule.getFlightScheduleId());
        for (SeatInventory seatInventory : latestSchedule.getSeatInventory()) {
            if (seatInventory.getCabinClass().getCabinClassName() == cabinClassType) {
                return seatInventory;
            }
        }
        throw new SeatInventoryNotFoundException("Seat inventory not found");
    }
    
    @Override
    public SeatInventory getValidSeatInventoryDetached(FlightSchedule schedule, CabinClassNameEnum cabinClassType) throws FlightScheduleNotFoundException, SeatInventoryNotFoundException {
        FlightSchedule latestSchedule = retrieveFlightScheduleById(schedule.getFlightScheduleId());
        em.detach(latestSchedule);
        em.detach(latestSchedule.getFlightSchedulePlan());
        List<FlightReservation> reservations = latestSchedule.getReservations();
        for (FlightReservation res : reservations) {
            em.detach(res);
        }
        List<SeatInventory> seats = latestSchedule.getSeatInventory();
        for (SeatInventory seat : seats) {
            em.detach(seat);
        }
        
        for (SeatInventory seatInventory : latestSchedule.getSeatInventory()) {
            if (seatInventory.getCabinClass().getCabinClassName() == cabinClassType) {
                em.detach(seatInventory);
                return seatInventory;
            }
        }
        
        throw new SeatInventoryNotFoundException("Seat inventory not found");
    }
   
}
