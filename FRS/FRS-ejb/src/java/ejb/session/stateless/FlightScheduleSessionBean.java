/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Fare;
import entity.FlightSchedule;
import entity.FlightSchedulePlan;
import entity.Flight;
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
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.CabinClassNameEnum;
import util.exception.FlightNotFoundException;
import util.exception.FlightScheduleNotFoundException;

/**
 *
 * @author zares
 */
@Stateless
public class FlightScheduleSessionBean implements FlightScheduleSessionBeanRemote, FlightScheduleSessionBeanLocal {

    @EJB
    private FlightSessionBeanLocal flightSessionBeanLocal;

    @PersistenceContext(unitName = "FRS-ejbPU")
    private EntityManager em;

    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public FlightSchedule createNewSchedule(FlightSchedule schedule, FlightSchedulePlan plan) {
        em.persist(schedule);
        
        plan.getFlightSchedule().add(schedule);

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
    
    public List<Pair<FlightSchedule, FlightSchedule>> retrieveConnectingFlightSchedules(String originAirport, String destAirport, Date departureDate, CabinClassNameEnum cabinClassName) throws FlightNotFoundException {
            
        List<Pair<FlightSchedule, FlightSchedule>> schedules = new ArrayList<>();
        List<Flight[]> flights = flightSessionBeanLocal.retrieveConnectingFlightsByFlightRoute(originAirport, destAirport);
            
        /*
            for (Object[] cf : flights) {
                Flight f1 = (Flight) cf[0];
                Flight f2 = (Flight) cf[1];
                for (FlightSchedulePlan fsp : f1.getFlightSchedulePlan()) {
                    if (fsp.isDisabled() == true) {
                        continue;
                    }
                    
                    for (FlightSchedule fs : fsp.getFlightSchedule()) {
                        for (FlightSchedulePlan fsp2 : f2.getFlightSchedulePlan()) {
                            if (fsp2.isDisabled() == true) {
                                continue;
                            }
                            for (FlightSchedule fs2 : fsp2.getFlightSchedule()) {
                                Calendar desiredDate = Calendar.getInstance();
                                Calendar fsDate = Calendar.getInstance();
                                desiredDate.setTime(departureDate);
                                fsDate.setTime(fs.getDepartureDateTime());
                                
                                Calendar c = Calendar.getInstance();
                                c.setTime(fs.getDepartureDateTime());
                                double duration = fs.getFlightDuration();
                                int hour = (int) duration;
                                int min = (int) (duration % 1 * 60);
                                c.add(Calendar.HOUR_OF_DAY, hour);
                                c.add(Calendar.MINUTE, min);
                                int diff1 = fs.getFlightSchedulePlan().getFlight().getFlightRoute().getDestinationAirport().getGmt() -
                                    fs.getFlightSchedulePlan().getFlight().getFlightRoute().getOriginAirport().getGmt();
                                c.add(Calendar.HOUR_OF_DAY, diff1);
                                
                                Calendar c2 = Calendar.getInstance();
                                c2.setTime(fs2.getDepartureDateTime());
                                long layoverTime = Duration.between(c.toInstant(), c2.toInstant()).toHours();
                                
                                if (desiredDate.DAY_OF_YEAR == fsDate.DAY_OF_YEAR && desiredDate.YEAR == fsDate.YEAR && !(layoverTime < 2l || layoverTime > 12l)) {
                                    if (cabinClassName == null) {
                                        schedules.add(new Pair(fs, fs2));
                                    } else {
                                        for (SeatInventory seatInventory : fs.getSeatInventory()) {
                                            if (seatInventory.getCabinClass().getCabinClassName().equals(cabinClassName)) {
                                                schedules.add(new Pair(fs, fs2));
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } */
        

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
                                    System.out.println("adddddded");
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
}
