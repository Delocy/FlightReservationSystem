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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        
        if (!plan.getFlightSchedule().contains(schedule)) {
            plan.getFlightSchedule().add(schedule);
        }
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
    public List<FlightSchedule> retrieveListOfFlightSchedule(String originAirport, String destAirport, Date departureDate, CabinClassNameEnum cabinClassName) {
        try {
            List<FlightSchedule> schedules = new ArrayList<>();
            List<Flight> flights = flightSessionBeanLocal.retrieveFlightsByFlightRoute(originAirport, destAirport);
            
            for (Flight f : flights) {
                for (FlightSchedulePlan fsp : f.getFlightSchedulePlan()) {
                    if (fsp.isDisabled() == false) {
                        continue;
                    }
                    for (FlightSchedule fs : fsp.getFlightSchedule()) {
                        Calendar desiredDate = Calendar.getInstance();
                        Calendar fsDate = Calendar.getInstance();
                        
                        desiredDate.setTime(departureDate);
                        fsDate.setTime(fs.getDepartureDateTime());
                        
                        if (desiredDate.DAY_OF_YEAR == fsDate.DAY_OF_YEAR && desiredDate.YEAR == fsDate.YEAR) {
                            if (cabinClassName == null) {
                                schedules.add(fs);
                            } else {
                                for (SeatInventory seatInventory : fs.getSeatInventory()) {
                                    if (seatInventory.getCabinClass().getCabinClassName().equals(cabinClassName)) {
                                        schedules.add(fs);
                                        break;
                                    }
                                }
                            }
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
        } catch (FlightNotFoundException ex) {
            Logger.getLogger(FlightScheduleSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @Override
    public Fare lowestFare(FlightSchedule fs, CabinClassNameEnum cabinClassName) throws FlightScheduleNotFoundException {
        FlightSchedule schedule = retrieveFlightScheduleById(fs.getFlightScheduleId());
        List<Fare> fares = schedule.getFlightSchedulePlan().getFares();
        
        Fare lowest = null;
        
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
